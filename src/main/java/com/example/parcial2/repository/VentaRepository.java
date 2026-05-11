package com.example.parcial2.repository;

import com.example.parcial2.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findTop20ByOrderByFechaVentaDesc();
}