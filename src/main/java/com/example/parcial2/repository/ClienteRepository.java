package com.example.parcial2.repository;

import com.example.parcial2.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByDocumento(String documento);
    List<Cliente> findAllByOrderByNombreAsc();
}
