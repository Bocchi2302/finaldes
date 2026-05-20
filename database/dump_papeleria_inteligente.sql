-- Dump funcional PostgreSQL para Papelería Inteligente
-- Ejecución recomendada desde una base administrativa:
--   psql -U postgres -f database/dump_papeleria_inteligente.sql

DROP DATABASE IF EXISTS papeleria_inteligente;
CREATE DATABASE papeleria_inteligente WITH ENCODING 'UTF8' TEMPLATE template0;
\connect papeleria_inteligente

SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('ADMIN', 'EMPLEADO')),
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    activa BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE proveedores (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(40),
    correo VARCHAR(150),
    direccion VARCHAR(255),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    documento VARCHAR(40) UNIQUE,
    telefono VARCHAR(40),
    correo VARCHAR(150),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE productos (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    precio NUMERIC(12,2) NOT NULL CHECK (precio > 0),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    stock_minimo INTEGER NOT NULL DEFAULT 0 CHECK (stock_minimo >= 0),
    activo BOOLEAN NOT NULL DEFAULT TRUE,
    fecha_vencimiento DATE,
    categoria_id BIGINT NOT NULL REFERENCES categorias(id),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now(),
    actualizado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE productos_proveedores (
    producto_id BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
    proveedor_id BIGINT NOT NULL REFERENCES proveedores(id) ON DELETE CASCADE,
    PRIMARY KEY (producto_id, proveedor_id)
);

CREATE TABLE compras (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    total NUMERIC(12,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL CHECK (estado IN ('REGISTRADA', 'ANULADA')),
    proveedor_id BIGINT NOT NULL REFERENCES proveedores(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE detalles_compra (
    id BIGSERIAL PRIMARY KEY,
    compra_id BIGINT NOT NULL REFERENCES compras(id) ON DELETE CASCADE,
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario > 0),
    subtotal NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0)
);

CREATE TABLE ventas (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL DEFAULT CURRENT_DATE,
    total NUMERIC(12,2) NOT NULL DEFAULT 0,
    estado VARCHAR(30) NOT NULL CHECK (estado IN ('REGISTRADA', 'ANULADA')),
    cliente_id BIGINT REFERENCES clientes(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    creado_en TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE detalles_venta (
    id BIGSERIAL PRIMARY KEY,
    venta_id BIGINT NOT NULL REFERENCES ventas(id) ON DELETE CASCADE,
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    precio_unitario NUMERIC(12,2) NOT NULL CHECK (precio_unitario > 0),
    subtotal NUMERIC(12,2) NOT NULL CHECK (subtotal >= 0)
);

CREATE TABLE movimientos_inventario (
    id BIGSERIAL PRIMARY KEY,
    tipo_movimiento VARCHAR(20) NOT NULL CHECK (tipo_movimiento IN ('ENTRADA', 'SALIDA', 'AJUSTE')),
    cantidad INTEGER NOT NULL CHECK (cantidad > 0),
    fecha TIMESTAMPTZ NOT NULL DEFAULT now(),
    motivo VARCHAR(255) NOT NULL,
    referencia VARCHAR(80),
    stock_resultante INTEGER NOT NULL CHECK (stock_resultante >= 0),
    producto_id BIGINT NOT NULL REFERENCES productos(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id)
);

CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_stock ON productos(stock, stock_minimo);
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_detalles_venta_producto ON detalles_venta(producto_id);
CREATE INDEX idx_movimientos_producto_fecha ON movimientos_inventario(producto_id, fecha DESC);

INSERT INTO usuarios (id, nombre, correo, password, rol, estado, creado_en, actualizado_en) VALUES
(1, 'Administrador Papelería', 'admin@papeleria.com', '$2y$12$/Pp1JClP/IKLlihpkq02SOTU1LnM.YZSynEMw6ClLPxNYrklsWq9.', 'ADMIN', TRUE, now(), now()),
(2, 'Empleado Papelería', 'empleado@papeleria.com', '$2y$12$f7oK6LaXcSAmt4s2T7yNde792Imzn8Q0bzQvNNp69.8.vXCFh9c1S', 'EMPLEADO', TRUE, now(), now());

INSERT INTO categorias (id, nombre, descripcion, activa, creado_en, actualizado_en) VALUES
(1, 'Útiles escolares', 'Cuadernos, lápices, colores y artículos escolares', TRUE, now(), now()),
(2, 'Oficina', 'Insumos de oficina y papelería empresarial', TRUE, now(), now()),
(3, 'Arte', 'Cartulinas, marcadores, témperas y materiales artísticos', TRUE, now(), now()),
(4, 'Papelería general', 'Productos de consumo general de papelería', TRUE, now(), now());

INSERT INTO proveedores (id, nombre, telefono, correo, direccion, activo, creado_en, actualizado_en) VALUES
(1, 'Distribuidora Escolar Meta', '608-555-0101', 'ventas@escolarmeta.com', 'Centro, Villavicencio', TRUE, now(), now()),
(2, 'Ofisuministros del Llano', '608-555-0202', 'contacto@ofillano.com', 'Barzal, Villavicencio', TRUE, now(), now()),
(3, 'Arte y Color Mayorista', '608-555-0303', 'pedidos@artecolor.com', 'San Benito, Villavicencio', TRUE, now(), now());

INSERT INTO clientes (id, nombre, documento, telefono, correo, activo, creado_en, actualizado_en) VALUES
(1, 'Cliente ocasional', '0000', 'N/A', NULL, TRUE, now(), now()),
(2, 'Colegio El Saber', '900100200', '3100000000', 'compras@elsaber.edu.co', TRUE, now(), now()),
(3, 'María Rodríguez', '11223344', '3001112233', 'maria.rodriguez@example.com', TRUE, now(), now());

INSERT INTO productos (id, nombre, descripcion, precio, stock, stock_minimo, activo, fecha_vencimiento, categoria_id, creado_en, actualizado_en) VALUES
(1, 'Cuaderno universitario', 'Cuaderno de 100 hojas rayado', 4500.00, 72, 20, TRUE, NULL, 1, now(), now()),
(2, 'Lápiz HB', 'Lápiz grafito HB', 1200.00, 135, 40, TRUE, NULL, 1, now(), now()),
(3, 'Lapicero negro', 'Lapicero tinta negra punta media', 1800.00, 90, 35, TRUE, NULL, 1, now(), now()),
(4, 'Borrador nata', 'Borrador blanco escolar', 1000.00, 45, 25, TRUE, NULL, 1, now(), now()),
(5, 'Resma carta', 'Papel carta 75g x 500 hojas', 17500.00, 8, 10, TRUE, NULL, 2, now(), now()),
(6, 'Carpeta oficio', 'Carpeta oficio plástica', 2500.00, 18, 15, TRUE, NULL, 2, now(), now()),
(7, 'Cartulina escolar', 'Cartulina colores surtidos', 1800.00, 11, 12, TRUE, NULL, 3, now(), now()),
(8, 'Marcador permanente', 'Marcador permanente negro', 4200.00, 20, 12, TRUE, NULL, 3, now(), now());

INSERT INTO productos_proveedores (producto_id, proveedor_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 2), (6, 2), (7, 3), (8, 3);

INSERT INTO compras (id, fecha, total, estado, proveedor_id, usuario_id, creado_en) VALUES
(1, CURRENT_DATE - INTERVAL '20 days', 535000.00, 'REGISTRADA', 1, 1, now() - INTERVAL '20 days'),
(2, CURRENT_DATE - INTERVAL '15 days', 410000.00, 'REGISTRADA', 2, 1, now() - INTERVAL '15 days'),
(3, CURRENT_DATE - INTERVAL '10 days', 285000.00, 'REGISTRADA', 3, 1, now() - INTERVAL '10 days');

INSERT INTO detalles_compra (id, compra_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 80, 3200.00, 256000.00),
(2, 1, 2, 150, 650.00, 97500.00),
(3, 1, 3, 100, 950.00, 95000.00),
(4, 1, 4, 50, 550.00, 27500.00),
(5, 2, 5, 20, 12500.00, 250000.00),
(6, 2, 6, 40, 4000.00, 160000.00),
(7, 3, 7, 60, 950.00, 57000.00),
(8, 3, 8, 60, 3800.00, 228000.00);

INSERT INTO ventas (id, fecha, total, estado, cliente_id, usuario_id, creado_en) VALUES
(1, CURRENT_DATE - INTERVAL '9 days', 45000.00, 'REGISTRADA', 1, 2, now() - INTERVAL '9 days'),
(2, CURRENT_DATE - INTERVAL '8 days', 54000.00, 'REGISTRADA', 1, 2, now() - INTERVAL '8 days'),
(3, CURRENT_DATE - INTERVAL '7 days', 67500.00, 'REGISTRADA', 2, 2, now() - INTERVAL '7 days'),
(4, CURRENT_DATE - INTERVAL '6 days', 76500.00, 'REGISTRADA', 1, 2, now() - INTERVAL '6 days'),
(5, CURRENT_DATE - INTERVAL '5 days', 90000.00, 'REGISTRADA', 3, 2, now() - INTERVAL '5 days'),
(6, CURRENT_DATE - INTERVAL '4 days', 40000.00, 'REGISTRADA', 1, 2, now() - INTERVAL '4 days'),
(7, CURRENT_DATE - INTERVAL '3 days', 55200.00, 'REGISTRADA', 2, 2, now() - INTERVAL '3 days'),
(8, CURRENT_DATE - INTERVAL '2 days', 81900.00, 'REGISTRADA', 1, 2, now() - INTERVAL '2 days'),
(9, CURRENT_DATE - INTERVAL '1 day', 96400.00, 'REGISTRADA', 3, 2, now() - INTERVAL '1 day'),
(10, CURRENT_DATE, 117300.00, 'REGISTRADA', 1, 2, now());

INSERT INTO detalles_venta (id, venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 10, 4500.00, 45000.00),
(2, 2, 1, 12, 4500.00, 54000.00),
(3, 3, 1, 15, 4500.00, 67500.00),
(4, 4, 1, 17, 4500.00, 76500.00),
(5, 5, 1, 20, 4500.00, 90000.00),
(6, 6, 5, 2, 17500.00, 35000.00),
(7, 6, 4, 5, 1000.00, 5000.00),
(8, 7, 7, 12, 1800.00, 21600.00),
(9, 7, 8, 6, 4200.00, 25200.00),
(10, 7, 2, 7, 1200.00, 8400.00),
(11, 8, 3, 18, 1800.00, 32400.00),
(12, 8, 6, 8, 2500.00, 20000.00),
(13, 8, 5, 1, 17500.00, 17500.00),
(14, 8, 2, 10, 1200.00, 12000.00),
(15, 9, 1, 8, 4500.00, 36000.00),
(16, 9, 3, 20, 1800.00, 36000.00),
(17, 9, 4, 10, 1000.00, 10000.00),
(18, 9, 7, 8, 1800.00, 14400.00),
(19, 10, 1, 18, 4500.00, 81000.00),
(20, 10, 2, 10, 1200.00, 12000.00),
(21, 10, 8, 4, 4200.00, 16800.00),
(22, 10, 6, 3, 2500.00, 7500.00);

INSERT INTO movimientos_inventario (tipo_movimiento, cantidad, fecha, motivo, referencia, stock_resultante, producto_id, usuario_id) VALUES
('ENTRADA', 80, now() - INTERVAL '20 days', 'Compra a proveedor', 'COMPRA-1', 80, 1, 1),
('ENTRADA', 150, now() - INTERVAL '20 days', 'Compra a proveedor', 'COMPRA-1', 150, 2, 1),
('ENTRADA', 100, now() - INTERVAL '20 days', 'Compra a proveedor', 'COMPRA-1', 100, 3, 1),
('ENTRADA', 50, now() - INTERVAL '20 days', 'Compra a proveedor', 'COMPRA-1', 50, 4, 1),
('ENTRADA', 20, now() - INTERVAL '15 days', 'Compra a proveedor', 'COMPRA-2', 20, 5, 1),
('ENTRADA', 40, now() - INTERVAL '15 days', 'Compra a proveedor', 'COMPRA-2', 40, 6, 1),
('ENTRADA', 60, now() - INTERVAL '10 days', 'Compra a proveedor', 'COMPRA-3', 60, 7, 1),
('ENTRADA', 60, now() - INTERVAL '10 days', 'Compra a proveedor', 'COMPRA-3', 60, 8, 1),
('SALIDA', 10, now() - INTERVAL '9 days', 'Venta registrada', 'VENTA-1', 70, 1, 2),
('SALIDA', 12, now() - INTERVAL '8 days', 'Venta registrada', 'VENTA-2', 58, 1, 2),
('SALIDA', 15, now() - INTERVAL '7 days', 'Venta registrada', 'VENTA-3', 43, 1, 2),
('SALIDA', 17, now() - INTERVAL '6 days', 'Venta registrada', 'VENTA-4', 26, 1, 2),
('SALIDA', 20, now() - INTERVAL '5 days', 'Venta registrada', 'VENTA-5', 6, 1, 2),
('AJUSTE', 72, now() - INTERVAL '1 day', 'Ajuste por conteo físico y reposición parcial', 'AJUSTE_MANUAL', 72, 1, 1),
('SALIDA', 2, now() - INTERVAL '4 days', 'Venta registrada', 'VENTA-6', 18, 5, 2),
('SALIDA', 12, now() - INTERVAL '3 days', 'Venta registrada', 'VENTA-7', 48, 7, 2),
('AJUSTE', 11, now() - INTERVAL '1 day', 'Ajuste por conteo físico', 'AJUSTE_MANUAL', 11, 7, 1);

SELECT setval('usuarios_id_seq', (SELECT max(id) FROM usuarios));
SELECT setval('categorias_id_seq', (SELECT max(id) FROM categorias));
SELECT setval('proveedores_id_seq', (SELECT max(id) FROM proveedores));
SELECT setval('clientes_id_seq', (SELECT max(id) FROM clientes));
SELECT setval('productos_id_seq', (SELECT max(id) FROM productos));
SELECT setval('compras_id_seq', (SELECT max(id) FROM compras));
SELECT setval('detalles_compra_id_seq', (SELECT max(id) FROM detalles_compra));
SELECT setval('ventas_id_seq', (SELECT max(id) FROM ventas));
SELECT setval('detalles_venta_id_seq', (SELECT max(id) FROM detalles_venta));
SELECT setval('movimientos_inventario_id_seq', (SELECT max(id) FROM movimientos_inventario));
