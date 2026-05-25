package com.learn2earn.movie_api.controller;


import com.learn2earn.movie_api.dto.LoginRequestDTO;
import com.learn2earn.movie_api.security.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationConfiguration authenticationConfiguration, JwtUtil jwtUtil) {
        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/api/v1/auth/login")
    public Map<String, String> login(@RequestBody LoginRequestDTO request) throws Exception {

        authenticationConfiguration.getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String token = jwtUtil.generateToken(request.username());

        return Map.of("token", token);
    }
}
