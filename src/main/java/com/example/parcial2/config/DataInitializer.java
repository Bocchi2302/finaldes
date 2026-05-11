package com.example.parcial2.config;

import com.example.parcial2.entity.Producto;
import com.example.parcial2.entity.Role;
import com.example.parcial2.entity.User;
import com.example.parcial2.repository.ProductoRepository;
import com.example.parcial2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               ProductoRepository productoRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByEmail("admin@inventario.com")) {
                userRepository.save(User.builder()
                        .fullName("Admin Inventario")
                        .email("admin@inventario.com")
                        .password(passwordEncoder.encode("Admin123*"))
                        .role(Role.ROLE_ADMIN)
                        .build());
            }

            if (!userRepository.existsByEmail("empleado@inventario.com")) {
                userRepository.save(User.builder()
                        .fullName("Empleado Demo")
                        .email("empleado@inventario.com")
                        .password(passwordEncoder.encode("Empleado123*"))
                        .role(Role.ROLE_EMPLEADO)
                        .build());
            }

            if (productoRepository.count() == 0) {
                productoRepository.save(Producto.builder()
                        .nombre("Cuaderno profesional")
                        .categoria("Papeleria")
                        .precioUnitario(new BigDecimal("3.50"))
                        .stock(45)
                        .stockMinimo(10)
                        .activo(true)
                        .build());

                productoRepository.save(Producto.builder()
                        .nombre("Lapicero negro")
                        .categoria("Escritura")
                        .precioUnitario(new BigDecimal("0.80"))
                        .stock(120)
                        .stockMinimo(25)
                        .activo(true)
                        .build());

                productoRepository.save(Producto.builder()
                        .nombre("Resma carta")
                        .categoria("Oficina")
                        .precioUnitario(new BigDecimal("6.25"))
                        .stock(18)
                        .stockMinimo(8)
                        .activo(true)
                        .build());
            }
        };
    }
}
