package com.example.parcial2.repository;

import com.example.parcial2.entity.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
    Optional<Proveedor> findByNombreIgnoreCase(String nombre);
    List<Proveedor> findAllByOrderByNombreAsc();
}
