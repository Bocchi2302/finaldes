package com.example.parcial2.repository;

import com.example.parcial2.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    boolean existsByNombreIgnoreCase(String nombre);
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    List<Producto> findByStockLessThanEqual(Integer stockMinimo);
}