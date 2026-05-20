package com.papeleria.inteligente.repository;

import com.papeleria.inteligente.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findTop20ByOrderByCreadoEnDesc();
}
