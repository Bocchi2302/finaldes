package com.example.parcial2.service;

import com.example.parcial2.entity.User;
import com.example.parcial2.exception.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {
            throw new ResourceNotFoundException("No se pudo identificar el usuario autenticado");
        }
        return user;
    }
}
