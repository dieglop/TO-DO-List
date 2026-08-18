package com.example.practice.dtos;

import com.example.practice.models.User;

public record UserDTO(Long id, String username, String password) {

    public UserDTO(User user){
        this(user.getId(), user.getUsername(), user.getPassword());
    }
    
}
