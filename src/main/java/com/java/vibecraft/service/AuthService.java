package com.java.vibecraft.service;

import com.java.vibecraft.dto.auth.AuthResponse;
import com.java.vibecraft.dto.auth.LoginRequest;
import com.java.vibecraft.dto.auth.SignupRequest;

public interface AuthService {
    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
