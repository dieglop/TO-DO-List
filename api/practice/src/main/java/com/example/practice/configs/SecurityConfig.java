package com.example.practice.configs;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.practice.security.JWTAuthenticationFilter;
import com.example.practice.security.JWTAuthorizationFilter;
import com.example.practice.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private AuthenticationManager authenticationManager;
    
    private final UserDetailsService userDetailsService;

    private final JWTUtil jwtUtil;
    
    public SecurityConfig(UserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    private static final String[] PUBLIC_MATCHERS = {
        "/"
    };

    private static final String[] PUBLIC_MATCHERS_POST = {
        "/user",
        "/login"
    };
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http.cors(cors -> cors.configurationSource(configurationSource())).csrf(csrf -> csrf.disable());

        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(this.userDetailsService)
            .passwordEncoder(bCryptPasswordEncoder());

        this.authenticationManager = authenticationManagerBuilder.build();

        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
            .anyRequest().authenticated()
        ).authenticationManager(authenticationManager);

        http.addFilter(new JWTAuthenticationFilter(this.authenticationManager, this.jwtUtil));
        http.addFilter(new JWTAuthorizationFilter(this.authenticationManager, this.jwtUtil, this.userDetailsService));

        http.sessionManagement(scp -> scp.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource configurationSource(){

        CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE"));
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
    
        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
