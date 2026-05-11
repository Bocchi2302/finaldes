package com.example.parcial2.controller;

import com.example.parcial2.config.ApiResponseBuilder;
import com.example.parcial2.dto.request.LoginRequest;
import com.example.parcial2.dto.request.RegisterRequest;
import com.example.parcial2.dto.response.ApiResponse;
import com.example.parcial2.dto.response.AuthResponse;
import com.example.parcial2.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final ApiResponseBuilder responseBuilder;

    public AuthController(AuthService authService, ApiResponseBuilder responseBuilder) {
        this.authService = authService;
        this.responseBuilder = responseBuilder;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(responseBuilder.success(HttpStatus.CREATED, "Usuario registrado correctamente", response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(responseBuilder.success(HttpStatus.OK, "Inicio de sesión exitoso", response));
    }
}
