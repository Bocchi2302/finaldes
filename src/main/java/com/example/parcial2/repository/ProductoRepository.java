package com.papeleria.inteligente.repository;

import com.papeleria.inteligente.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Optional<Producto> findByNombreIgnoreCase(String nombre);
    List<Producto> findAllByOrderByNombreAsc();
    List<Producto> findByActivoTrueOrderByNombreAsc();

    @Query("select p from Producto p where p.activo = true and p.stock <= p.stockMinimo order by p.stock asc, p.nombre asc")
    List<Producto> findProductosBajoStock();

    long countByActivoTrue();
}
