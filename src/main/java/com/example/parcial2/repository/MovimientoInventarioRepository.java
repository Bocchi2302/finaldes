package com.example.parcial2.repository;

import com.example.parcial2.entity.MovimientoInventario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
    List<MovimientoInventario> findTop50ByOrderByFechaDesc();
    List<MovimientoInventario> findByProductoIdOrderByFechaDesc(Long productoId);
}
