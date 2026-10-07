package com.videostreaming.account.service;

import com.videostreaming.account.exception.InvalidPasswordException;
import com.videostreaming.account.exception.UserNotFoundException;
import com.videostreaming.account.model.User;
import com.videostreaming.account.model.dto.ChangePasswordRequest;
import com.videostreaming.account.model.dto.LoginRequest;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import com.videostreaming.account.repository.UserRepository;
import com.videostreaming.account.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository repository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;

    @InjectMocks
    private UserServiceImpl userService;

    private User existingUser;

    @BeforeEach
    void setUp() {
        existingUser = new User("alice@example.com", "hashed-password", "alice");
        existingUser.setId(1L);
    }

    @Test
    void getAllUsers_returnsMappedResponses() {
        when(repository.findAll()).thenReturn(List.of(existingUser));

        List<UserResponse> result = userService.getAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getUserName()).isEqualTo("alice");
    }

    @Test
    void getUserById_found_returnsResponse() {
        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));

        UserResponse response = userService.getUserById(1L);

        assertThat(response.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void getUserById_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void createUser_encodesPasswordAndSaves() {
        UserRequest request = new UserRequest();
        request.setEmail("bob@example.com");
        request.setPassword("plainPassword123");
        request.setUserName("bob");

        when(passwordEncoder.encode("plainPassword123")).thenReturn("encoded-password");
        when(repository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(2L);
            return u;
        });

        UserResponse response = userService.createUser(request);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getUserName()).isEqualTo("bob");
        verify(passwordEncoder).encode("plainPassword123");
    }

    @Test
    void updateUser_correctPassword_updatesFields() {
        UserRequest request = new UserRequest();
        request.setPassword("correct-plain-password");
        request.setEmail("newemail@example.com");
        request.setUserName("alice-updated");

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("correct-plain-password", "hashed-password")).thenReturn(true);
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(1L, request);

        assertThat(response.getEmail()).isEqualTo("newemail@example.com");
        assertThat(response.getUserName()).isEqualTo("alice-updated");
    }

    @Test
    void updateUser_wrongPassword_throwsInvalidPasswordException() {
        UserRequest request = new UserRequest();
        request.setPassword("wrong-password");

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong-password", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> userService.updateUser(1L, request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void changePassword_correctOldPassword_updatesToNewEncodedPassword() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("old-plain");
        request.setNewPassword("new-plain");

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("old-plain", "hashed-password")).thenReturn(true);
        when(passwordEncoder.encode("new-plain")).thenReturn("new-hashed-password");
        when(repository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.changePassword(1L, request);

        verify(passwordEncoder).encode("new-plain");
    }

    @Test
    void changePassword_wrongOldPassword_throwsException() {
        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword("wrong-old");
        request.setNewPassword("new-plain");

        when(repository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong-old", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(1L, request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void login_validCredentials_returnsToken() {
        LoginRequest request = new LoginRequest();
        request.setUserName("alice");
        request.setPassword("correct-plain");

        when(repository.findByUserName("alice")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("correct-plain", "hashed-password")).thenReturn(true);
        when(jwtUtil.generateToken(1L, "alice@example.com", "alice")).thenReturn("fake-jwt-token");

        String token = userService.login(request);

        assertThat(token).isEqualTo("fake-jwt-token");
    }

    @Test
    void login_unknownUsername_throwsInvalidPasswordException() {
        LoginRequest request = new LoginRequest();
        request.setUserName("ghost");
        request.setPassword("whatever");

        when(repository.findByUserName("ghost")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void login_wrongPassword_throwsInvalidPasswordException() {
        LoginRequest request = new LoginRequest();
        request.setUserName("alice");
        request.setPassword("wrong-plain");

        when(repository.findByUserName("alice")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong-plain", "hashed-password")).thenReturn(false);

        assertThatThrownBy(() -> userService.login(request))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    void deleteUser_existing_deletesSuccessfully() {
        when(repository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void deleteUser_notFound_throwsException() {
        when(repository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUser(99L))
                .isInstanceOf(UserNotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}