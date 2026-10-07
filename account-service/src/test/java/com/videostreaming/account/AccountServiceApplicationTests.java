package com.videostreaming.account;

import com.videostreaming.account.model.dto.LoginRequest;
import com.videostreaming.account.model.dto.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AccountServiceApplicationTests {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        // Fails fast if the Spring context can't wire up against the real Postgres container
    }

    @Test
    void fullUserLifecycle_registerThenLogin_succeeds() {
        UserRequest registerRequest = new UserRequest();
        registerRequest.setEmail("integration@example.com");
        registerRequest.setPassword("integrationPass123");
        registerRequest.setUserName("integrationUser");

        ResponseEntity<Map> registerResponse = restTemplate.postForEntity(
                "/users", registerRequest, Map.class);

        assertThat(registerResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(registerResponse.getBody()).containsEntry("userName", "integrationUser");

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("integrationUser");
        loginRequest.setPassword("integrationPass123");

        ResponseEntity<Map> loginResponse = restTemplate.postForEntity(
                "/users/login", loginRequest, Map.class);

        assertThat(loginResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(loginResponse.getBody()).containsKey("token");
    }
}
