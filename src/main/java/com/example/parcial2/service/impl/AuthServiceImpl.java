package com.example.parcial2.service.impl;

import com.example.parcial2.dto.request.LoginRequest;
import com.example.parcial2.dto.request.RegisterRequest;
import com.example.parcial2.dto.response.AuthResponse;
import com.example.parcial2.entity.User;
import com.example.parcial2.exception.custom.DuplicateResourceException;
import com.example.parcial2.mapper.UserMapper;
import com.example.parcial2.repository.UserRepository;
import com.example.parcial2.security.JwtService;
import com.example.parcial2.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtService jwtService,
                           UserMapper userMapper,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email " + request.getEmail() + " already exists");
        }

        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(savedUser);

        return AuthResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpiration())
                .role(savedUser.getRole())
                .user(userMapper.toResponse(savedUser))
                .build();
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return AuthResponse.builder()
                .token(token)
                .expiresIn(jwtService.getExpiration())
                .role(user.getRole())
                .user(userMapper.toResponse(user))
                .build();
    }
}
