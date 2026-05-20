package com.papeleria.inteligente.config;

import com.papeleria.inteligente.entity.*;
import com.papeleria.inteligente.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedData(UserRepository userRepository,
                               CategoriaRepository categoriaRepository,
                               ProveedorRepository proveedorRepository,
                               ProductoRepository productoRepository,
                               ClienteRepository clienteRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepository.existsByCorreoIgnoreCase("admin@papeleria.com")) {
                userRepository.save(User.builder()
                        .nombre("Administrador Papelería")
                        .correo("admin@papeleria.com")
                        .password(passwordEncoder.encode("Admin123*"))
                        .rol(Role.ADMIN)
                        .estado(true)
                        .build());
            }
            if (!userRepository.existsByCorreoIgnoreCase("empleado@papeleria.com")) {
                userRepository.save(User.builder()
                        .nombre("Empleado Papelería")
                        .correo("empleado@papeleria.com")
                        .password(passwordEncoder.encode("Empleado123*"))
                        .rol(Role.EMPLEADO)
                        .estado(true)
                        .build());
            }

            Categoria utiles = categoriaRepository.findByNombreIgnoreCase("Útiles escolares")
                    .orElseGet(() -> categoriaRepository.save(Categoria.builder().nombre("Útiles escolares").descripcion("Cuadernos, lápices, colores y artículos escolares").activa(true).build()));
            Categoria oficina = categoriaRepository.findByNombreIgnoreCase("Oficina")
                    .orElseGet(() -> categoriaRepository.save(Categoria.builder().nombre("Oficina").descripcion("Insumos de oficina y papelería empresarial").activa(true).build()));
            Categoria arte = categoriaRepository.findByNombreIgnoreCase("Arte")
                    .orElseGet(() -> categoriaRepository.save(Categoria.builder().nombre("Arte").descripcion("Cartulinas, marcadores y materiales artísticos").activa(true).build()));

            Proveedor proveedor = proveedorRepository.findByNombreIgnoreCase("Distribuidora Escolar Meta")
                    .orElseGet(() -> proveedorRepository.save(Proveedor.builder()
                            .nombre("Distribuidora Escolar Meta")
                            .telefono("608-555-0101")
                            .correo("ventas@escolarmeta.com")
                            .direccion("Villavicencio, Meta")
                            .activo(true)
                            .build()));

            crearProductoSiNoExiste(productoRepository, proveedor, utiles, "Cuaderno universitario", "Cuaderno de 100 hojas rayado", new BigDecimal("4500.00"), 80, 20);
            crearProductoSiNoExiste(productoRepository, proveedor, utiles, "Lápiz HB", "Lápiz grafito HB", new BigDecimal("1200.00"), 150, 40);
            crearProductoSiNoExiste(productoRepository, proveedor, oficina, "Resma carta", "Papel carta 75g", new BigDecimal("17500.00"), 15, 10);
            crearProductoSiNoExiste(productoRepository, proveedor, arte, "Cartulina escolar", "Cartulina colores surtidos", new BigDecimal("1800.00"), 25, 12);

            if (clienteRepository.count() == 0) {
                clienteRepository.saveAll(List.of(
                        Cliente.builder().nombre("Cliente ocasional").documento("0000").telefono("N/A").correo(null).activo(true).build(),
                        Cliente.builder().nombre("Colegio El Saber").documento("900100200").telefono("3100000000").correo("compras@elsaber.edu.co").activo(true).build()
                ));
            }
        };
    }

    private void crearProductoSiNoExiste(ProductoRepository productoRepository,
                                         Proveedor proveedor,
                                         Categoria categoria,
                                         String nombre,
                                         String descripcion,
                                         BigDecimal precio,
                                         int stock,
                                         int stockMinimo) {
        productoRepository.findByNombreIgnoreCase(nombre).orElseGet(() -> {
            Producto producto = Producto.builder()
                    .nombre(nombre)
                    .descripcion(descripcion)
                    .categoria(categoria)
                    .precio(precio)
                    .stock(stock)
                    .stockMinimo(stockMinimo)
                    .activo(true)
                    .build();
            producto.getProveedores().add(proveedor);
            return productoRepository.save(producto);
        });
    }
}
