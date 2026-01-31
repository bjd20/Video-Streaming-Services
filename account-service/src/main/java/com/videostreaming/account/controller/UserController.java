package com.videostreaming.account.controller;

import com.videostreaming.account.model.dto.ChangePasswordRequest;
import com.videostreaming.account.model.dto.LoginRequest;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import com.videostreaming.account.service.UserService;
import com.videostreaming.account.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserResponse> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest userRequest){
        UserResponse created = userService.createUser(userRequest);
        return ResponseEntity.status(HttpStatus.SC_CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,@Valid @RequestBody UserRequest userRequest){
        return ResponseEntity.ok(userService.updateUser(id,userRequest));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponse> changePassword(@PathVariable Long id, @Valid @RequestBody ChangePasswordRequest passwordRequest){
        return ResponseEntity.ok(userService.changePassword(id, passwordRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest loginRequest){
        String token = userService.login(loginRequest);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("username", loginRequest.getUserName());

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
