package com.example.parcial2.service.impl;

import com.example.parcial2.entity.Role;
import com.example.parcial2.entity.User;
import com.example.parcial2.exception.custom.UnauthorizedAccessException;
import com.example.parcial2.service.ProductoPermissionService;
import org.springframework.stereotype.Service;

@Service
public class ProductoPermissionServiceImpl implements ProductoPermissionService {

    @Override
    public void validarPermisoEliminarProducto(User user) {
        if (user == null || user.getRole() != Role.ROLE_ADMIN) {
            throw new UnauthorizedAccessException("Solo un ADMIN puede desactivar productos");
        }
    }
}