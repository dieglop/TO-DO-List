package com.example.practice.services;

import java.util.List;
import java.util.Objects;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.practice.dtos.UserDTO;
import com.example.practice.enums.ProfileEnum;
import com.example.practice.models.User;
import com.example.practice.repositories.UserRepository;
import com.example.practice.security.UserSpringSecurity;
import com.example.practice.services.exceptions.AuthorizationException;
import com.example.practice.services.exceptions.DataBindingViolationException;
import com.example.practice.services.exceptions.ObjectNotFoundException;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    } 


    public User findById(Long id){
        
        UserSpringSecurity userSpringSecurity = authenticate();

        if(!Objects.nonNull(userSpringSecurity)){
            throw new AuthorizationException("Acesso Negado! Usuário não autenticado");
        }

        if(!userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Acesso Negado!");
        }

        return this.userRepository.findById(id)
                                    .orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado! id: " + id));
    }

    public List<User> findAllUsers(){
        
        UserSpringSecurity userSpringSecurity = authenticate();

        if(!userSpringSecurity.hasRole(ProfileEnum.ADMIN)){
            throw new AuthorizationException("Ação não permitida!");
        }
        
        List<User> usersList = this.userRepository.findAll();
        return usersList;
    }

    @Transactional
    public UserDTO create(User user){
        user.setId(null);
        user.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        user.addProfile(ProfileEnum.USER);
        user = this.userRepository.save(user);

        UserDTO userDTO = new UserDTO(user);

        return userDTO;
    }

    @Transactional
    public UserDTO update(User user){
        
        User newUser = findById(user.getId()); 
        newUser.setPassword(user.getPassword());
        newUser.setPassword(this.bCryptPasswordEncoder.encode(user.getPassword()));
        this.userRepository.save(newUser);
        
        UserDTO userDTO = new UserDTO(newUser);
        
        return userDTO;
    }

    public UserDTO delete (Long id){
        try {
            User user = findById(id);
            
            this.userRepository.deleteById(id);
            UserDTO userDTO = new UserDTO(user);

            return userDTO;          
            
        } catch (Exception e) {
            throw new DataBindingViolationException("Não possível apagar usuário!");
        }
    }

    public static UserSpringSecurity authenticate(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        } catch (Exception e) {

            return null;
        }

    }
}
