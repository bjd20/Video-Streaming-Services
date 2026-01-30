package com.videostreaming.account.service;

import com.videostreaming.account.exception.UserNotFoundException;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import java.util.List;

public interface UserService {

    List<UserResponse> getAllUsers();
    UserResponse getUserById(Long id);

    UserResponse createUser(UserRequest userRequest);
    UserResponse updateUser(Long id, UserRequest userRequest);

    void deleteUser(Long id);

//    TODO : Login, Change Password
}
