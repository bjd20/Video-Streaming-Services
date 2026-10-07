package com.videostreaming.account.repository;

import com.videostreaming.account.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @org.springframework.beans.factory.annotation.Autowired
    private UserRepository userRepository;

    @Test
    void saveAndFindByUserName_returnsSavedUser() {
        User user = new User("carol@example.com", "hashed-pw", "carol");
        userRepository.save(user);

        Optional<User> found = userRepository.findByUserName("carol");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("carol@example.com");
    }

    @Test
    void findByEmail_notFound_returnsEmpty() {
        Optional<User> found = userRepository.findByEmail("nonexistent@example.com");

        assertThat(found).isEmpty();
    }

    @Test
    void uniqueConstraint_duplicateUserName_throwsException() {
        userRepository.save(new User("dave@example.com", "hashed-pw", "dave"));
        userRepository.flush();

        User duplicate = new User("dave2@example.com", "hashed-pw", "dave");

        org.junit.jupiter.api.Assertions.assertThrows(
                org.springframework.dao.DataIntegrityViolationException.class,
                () -> {
                    userRepository.save(duplicate);
                    userRepository.flush();
                });
    }
}
