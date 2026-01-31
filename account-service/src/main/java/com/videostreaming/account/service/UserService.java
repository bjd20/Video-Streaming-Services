package com.videostreaming.account.service;

import com.videostreaming.account.exception.UserNotFoundException;
import com.videostreaming.account.model.dto.ChangePasswordRequest;
import com.videostreaming.account.model.dto.LoginRequest;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long id, UserRequest userRequest);
    UserResponse changePassword(Long id, ChangePasswordRequest passwordRequest);

    String login(LoginRequest loginRequest);

    void deleteUser(Long id);

//    TODO : Login, Change Password
}
