package com.videostreaming.account.service;

import com.videostreaming.account.config.SecurityConfig;
import com.videostreaming.account.exception.InvalidPasswordException;
import com.videostreaming.account.exception.UserNotFoundException;
import com.videostreaming.account.model.User;
import com.videostreaming.account.model.dto.ChangePasswordRequest;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import com.videostreaming.account.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    public UserResponse getUserById(Long id){
        User user =   repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponse(user);
    }

    @Override
    public UserResponse createUser(UserRequest userRequest) {
        User user = toEntity(userRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return toResponse(repository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest){
        User updated = repository.findById(id)
                .map(u -> {
                    if(!passwordEncoder.matches(userRequest.getPassword(), u.getPassword()))
                        throw new InvalidPasswordException("Invalid Password");
//                    if(userRequest.getEmail()!=null && !userRequest.getEmail().isEmpty())
                    u.setEmail(userRequest.getEmail());
                    u.setUserName(userRequest.getUserName());
                    return repository.save(u);
                })
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponse(updated);
    }

    @Override
    public UserResponse changePassword(Long id, ChangePasswordRequest passwordRequest) {
        User updated = repository.findById(id)
                .map(u -> {
                    if(passwordEncoder.matches(passwordRequest.getOldPassword(), u.getPassword())){
                        u.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
                    }else{
                        throw new InvalidPasswordException("Wrong old password");
                    }
                    return repository.save(u);
                })
                .orElseThrow(() -> new UserNotFoundException(id));

        return toResponse(updated);
    }

    @Override
    public void deleteUser(Long id){
        if(!repository.existsById(id)){
            throw new UserNotFoundException(id);
        }

        repository.deleteById(id);
    }

    // Entity -> Response OR Alternate way: Configuring a ModelMapper Bean (MapStruct).
    private UserResponse toResponse(User user){
        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setUserName(user.getUserName());
        response.setCreatedAt(user.getCreatedAt());
        response.setUpdatedAt(user.getUpdatedAt());

        return response;
    }

    // UserRequest -> Entity
    private User toEntity(UserRequest userRequest){
        return new User(
                userRequest.getEmail(),
                userRequest.getPassword(),
                userRequest.getUserName());
    }
}
