package com.example.parcial2.service;

import com.example.parcial2.dto.response.DashboardResponse;
import com.example.parcial2.dto.response.ProductoMasVendidoResponse;
import com.example.parcial2.entity.DetalleVenta;
import com.example.parcial2.entity.Venta;
import com.example.parcial2.mapper.ProductoMapper;
import com.example.parcial2.repository.DetalleVentaRepository;
import com.example.parcial2.repository.ProductoRepository;
import com.example.parcial2.repository.VentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class ReporteService {
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoMapper productoMapper;

    public ReporteService(ProductoRepository productoRepository,
                          VentaRepository ventaRepository,
                          DetalleVentaRepository detalleVentaRepository,
                          ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.ventaRepository = ventaRepository;
        this.detalleVentaRepository = detalleVentaRepository;
        this.productoMapper = productoMapper;
    }

    public DashboardResponse dashboard() {
        LocalDate hoy = LocalDate.now();
        YearMonth mesActual = YearMonth.from(hoy);
        LocalDate inicioMes = mesActual.atDay(1);
        LocalDate finMes = mesActual.atEndOfMonth();

        BigDecimal ventasDelDia = ventaRepository.findByFechaBetweenOrderByFechaDesc(hoy, hoy).stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal ingresosDelMes = ventaRepository.findByFechaBetweenOrderByFechaDesc(inicioMes, finMes).stream()
                .map(Venta::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<ProductoMasVendidoResponse> topProductos = productosMasVendidos(inicioMes, finMes, 5);

        return new DashboardResponse(
                productoRepository.countByActivoTrue(),
                productoRepository.findProductosBajoStock().size(),
                ventasDelDia,
                ingresosDelMes,
                productoRepository.findProductosBajoStock().stream().limit(8).map(productoMapper::toResponse).toList(),
                topProductos
        );
    }

    public List<ProductoMasVendidoResponse> productosMasVendidos(LocalDate desde, LocalDate hasta, int limite) {
        Map<Long, ProductoAcumulado> acumulado = new LinkedHashMap<>();
        for (DetalleVenta detalle : detalleVentaRepository.findDetallesByFechaBetween(desde, hasta)) {
            ProductoAcumulado row = acumulado.computeIfAbsent(detalle.getProducto().getId(), id -> new ProductoAcumulado(id, detalle.getProducto().getNombre()));
            row.unidades += detalle.getCantidad();
            row.ingresos = row.ingresos.add(detalle.getSubtotal());
        }
        return acumulado.values().stream()
                .sorted(Comparator.comparingInt(ProductoAcumulado::unidades).reversed())
                .limit(limite)
                .map(row -> new ProductoMasVendidoResponse(row.productoId, row.producto, row.unidades, row.ingresos))
                .toList();
    }

    private static final class ProductoAcumulado {
        private final Long productoId;
        private final String producto;
        private int unidades;
        private BigDecimal ingresos = BigDecimal.ZERO;

        private ProductoAcumulado(Long productoId, String producto) {
            this.productoId = productoId;
            this.producto = producto;
        }

        private int unidades() {
            return unidades;
        }
    }
}
