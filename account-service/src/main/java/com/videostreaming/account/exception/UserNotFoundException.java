package com.videostreaming.account.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String message) {
        super(message);
    }
    // Usage: throw new UserNotFoundException("User not found");

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    // Usage: throw new UserNotFoundException("DB error", sqlException);

    public UserNotFoundException(Long id){
        super("User with ID " + id + " not found");
    }
    // Usage: throw new UserNotFoundException(userId);

}
