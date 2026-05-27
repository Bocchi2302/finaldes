# Notas de refactorización

El proyecto original estaba centrado en productos y ventas simples. La refactorización amplía el dominio para alinearlo con el documento de Papelería Inteligente.

## Cambios principales

1. Se cambió el paquete base a `com.example.parcial2`.
2. Se actualizó el proyecto a Java 21 y Spring Boot 3.x.
3. Se organizó el backend por capas: controladores, servicios, repositorios, entidades, DTOs, mappers y seguridad.
4. Se agregaron entidades para categorías, proveedores, clientes, compras, detalles de compra, ventas con detalles y movimientos de inventario.
5. Se agregó dashboard de reportes y predicción de demanda.
6. Se corrigieron vistas que estaban mezclando sintaxis Jinja con Thymeleaf.
7. Se agregó dump PostgreSQL funcional con datos de prueba.

## Alcance cubierto

- Autenticación JWT.
- Roles `ADMIN` y `EMPLEADO`.
- CRUD principal para productos, categorías, proveedores y clientes.
- Registro de compras con aumento automático de stock.
- Registro de ventas con validación y descuento automático de stock.
- Movimientos de inventario por entrada, salida y ajuste.
- Reportes básicos.
- Predicción de demanda por producto.

## Pendientes sugeridos para una versión posterior

- Pruebas unitarias e integración.
- Paginación y filtros avanzados.
- Exportación de reportes en PDF/Excel.
- Auditoría detallada por usuario.
- Despliegue en nube.
- Facturación electrónica, si el proyecto se lleva a producción real.
