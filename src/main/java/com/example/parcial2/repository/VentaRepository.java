package com.papeleria.inteligente.repository;

import com.papeleria.inteligente.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    List<Venta> findTop20ByOrderByCreadoEnDesc();
    List<Venta> findByFechaBetweenOrderByFechaDesc(LocalDate desde, LocalDate hasta);
}
