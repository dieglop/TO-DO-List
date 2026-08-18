package com.example.practice.security;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.practice.exceptions.GlobalExceptionHandler;
import com.example.practice.models.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    
    private AuthenticationManager authenticationManager;

    private JWTUtil jwtUtil;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {

        setAuthenticationFailureHandler(new GlobalExceptionHandler());
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response ) throws AuthenticationException{

        try {
            User userCredentials = new ObjectMapper().readValue(request.getInputStream(), User.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                                                userCredentials.getUsername(), 
                                                                userCredentials.getPassword(), 
                                                                new ArrayList<>());
        
            Authentication authentication = this.authenticationManager.authenticate(authToken);
            
            return authentication;

        } catch (IOException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, 
                                                FilterChain filterChain, Authentication authentication)throws IOException, ServletException{
        
        
        UserSpringSecurity userSpringSecurity = (UserSpringSecurity)authentication.getPrincipal();
        String username = userSpringSecurity.getUsername();
        String token = this.jwtUtil.generateToken(username);

        Cookie jwtCookie = new Cookie("jwt_token", token);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(false);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(86400);

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("access-control-expose-headers", "Authorization");
        response.addCookie(jwtCookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String jsonResponseBody = String.format("{\"username\": \"%s\"}", username);
        response.getWriter().write(jsonResponseBody);

    }
}
