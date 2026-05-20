package com.papeleria.inteligente.service;

import com.papeleria.inteligente.dto.request.LoginRequest;
import com.papeleria.inteligente.dto.request.RegisterRequest;
import com.papeleria.inteligente.dto.response.AuthResponse;
import com.papeleria.inteligente.entity.Role;
import com.papeleria.inteligente.entity.User;
import com.papeleria.inteligente.exception.DuplicateResourceException;
import com.papeleria.inteligente.mapper.UserMapper;
import com.papeleria.inteligente.repository.UserRepository;
import com.papeleria.inteligente.security.JwtService;
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
