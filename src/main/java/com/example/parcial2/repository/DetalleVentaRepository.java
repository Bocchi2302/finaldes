package com.papeleria.inteligente.repository;

import com.papeleria.inteligente.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Long> {

    @Query("""
            select d
            from DetalleVenta d
            join fetch d.venta v
            join fetch d.producto p
            where p.id = :productoId and v.estado = com.papeleria.inteligente.entity.EstadoOperacion.REGISTRADA
            order by v.fecha asc
            """)
    List<DetalleVenta> findHistorialProducto(@Param("productoId") Long productoId);

    @Query("""
            select d
            from DetalleVenta d
            join fetch d.venta v
            join fetch d.producto p
            where v.fecha between :desde and :hasta and v.estado = com.papeleria.inteligente.entity.EstadoOperacion.REGISTRADA
            """)
    List<DetalleVenta> findDetallesByFechaBetween(@Param("desde") LocalDate desde, @Param("hasta") LocalDate hasta);
}
