package com.example.practice.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.practice.models.User;
import com.example.practice.repositories.UserRepository;
import com.example.practice.security.UserSpringSecurity;

@Service
public class UserDetailsServiceImp implements UserDetailsService{

    private UserRepository userRepository;

    public UserDetailsServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
   
   
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userOptional = this.userRepository.findByUsername(username);

        if(userOptional.isEmpty()){

            throw new UsernameNotFoundException("Usuário " + username + " não encontrado!");
        }

        User user = userOptional.get();

        return new UserSpringSecurity(user.getId(), user.getUsername(), user.getPassword(), user.getProfiles());
    }


    
}
