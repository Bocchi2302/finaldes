package com.example.parcial2.service;

import com.example.parcial2.dto.request.LoginRequest;
import com.example.parcial2.dto.request.RegisterRequest;
import com.example.parcial2.dto.response.AuthResponse;
import com.example.parcial2.entity.Role;
import com.example.parcial2.entity.User;
import com.example.parcial2.exception.DuplicateResourceException;
import com.example.parcial2.mapper.UserMapper;
import com.example.parcial2.repository.UserRepository;
import com.example.parcial2.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService,
                       UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByCorreoIgnoreCase(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario con el correo " + request.correo());
        }
        User user = User.builder()
                .nombre(request.nombre())
                .correo(request.correo())
                .password(passwordEncoder.encode(request.password()))
                .rol(request.rol() == null ? Role.EMPLEADO : request.rol())
                .estado(true)
                .build();
        User saved = userRepository.save(user);
        String token = jwtService.generateToken(saved);
        return new AuthResponse(token, jwtService.getExpiration(), saved.getRol(), userMapper.toResponse(saved));
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.correo(), request.password()));
        User user = userRepository.findByCorreoIgnoreCase(request.correo())
                .orElseThrow(() -> new DuplicateResourceException("Credenciales inválidas"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, jwtService.getExpiration(), user.getRol(), userMapper.toResponse(user));
    }
}
