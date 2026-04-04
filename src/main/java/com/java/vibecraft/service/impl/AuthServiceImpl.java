package com.java.vibecraft.service.impl;

import com.java.vibecraft.dto.auth.AuthResponse;
import com.java.vibecraft.dto.auth.LoginRequest;
import com.java.vibecraft.dto.auth.SignupRequest;
import com.java.vibecraft.service.AuthService;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Override
    public AuthResponse signup(SignupRequest request) {
        return null;
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        return null;
    }
}
