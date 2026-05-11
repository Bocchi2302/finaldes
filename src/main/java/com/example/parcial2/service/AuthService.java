package com.example.parcial2.service;

import com.example.parcial2.dto.request.LoginRequest;
import com.example.parcial2.dto.request.RegisterRequest;
import com.example.parcial2.dto.response.AuthResponse;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
