package com.videostreaming.account.service;

import com.videostreaming.account.exception.UserNotFoundException;
import com.videostreaming.account.model.User;
import com.videostreaming.account.model.dto.UserRequest;
import com.videostreaming.account.model.dto.UserResponse;
import com.videostreaming.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
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
        return toResponse(repository.save(user));
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest userRequest){
        User updated = repository.findById(id)
                .map(u -> {
                    if(userRequest.getEmail()!=null) u.setEmail(userRequest.getEmail());
                    if(userRequest.getPassword()!=null) u.setPassword(userRequest.getPassword());
                    u.setUserName(userRequest.getUserName());
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
