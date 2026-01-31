package com.videostreaming.account.controller;

import com.videostreaming.account.model.dto.ChangePasswordRequest;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import com.videostreaming.account.service.UserService;
import com.videostreaming.account.service.UserServiceImpl;
import jakarta.validation.Valid;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
