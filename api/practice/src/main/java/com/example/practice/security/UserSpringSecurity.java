package com.example.practice.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.practice.enums.ProfileEnum;


public class UserSpringSecurity implements UserDetails{

    private Long id;
    private String username;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;
    
    
    public UserSpringSecurity() {
    }

    
    public UserSpringSecurity(Long id, String username, String password, Set<ProfileEnum> profileEnums) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = profileEnums.stream().map(x -> new SimpleGrantedAuthority(x.getDescription())).collect(Collectors.toList());
    }

    public Long getId() {
        return this.id;
    }
    
    @Override
    public String getUsername() {
        return this.username;
    }
    
    @Override
    public String getPassword() {
        return this.password;
    }
    
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }
    
    public boolean hasRole(ProfileEnum profileEnum){
        return getAuthorities().contains(new SimpleGrantedAuthority(profileEnum.getDescription()));
    }

}
