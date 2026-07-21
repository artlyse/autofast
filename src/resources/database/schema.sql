-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS autofast_db;
USE autofast_db;

-- Crear tablas
CREATE TABLE IF NOT EXISTS cliente (
    id_cliente BIGINT PRIMARY KEY AUTO_INCREMENT,
    dni_ruc VARCHAR(15) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(200),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS tecnico (
    id_tecnico BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    especialidad VARCHAR(100),
    telefono VARCHAR(20),
    email VARCHAR(100),
    fecha_contratacion DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS vehiculo (
    id_vehiculo BIGINT PRIMARY KEY AUTO_INCREMENT,
    placa VARCHAR(10) UNIQUE NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio VARCHAR(4),
    color VARCHAR(30),
    numero_motor VARCHAR(50),
    numero_chasis VARCHAR(50),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE,
    cliente_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente)
);

CREATE TABLE IF NOT EXISTS repuesto (
    id_repuesto BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200),
    marca VARCHAR(50),
    precio_compra DECIMAL(10,2),
    precio_venta DECIMAL(10,2) NOT NULL,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    ubicacion VARCHAR(50),
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS orden_servicio (
    id_orden BIGINT PRIMARY KEY AUTO_INCREMENT,
    numero_orden VARCHAR(20) UNIQUE NOT NULL,
    fecha_apertura DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_cierre DATETIME,
    estado VARCHAR(30) NOT NULL DEFAULT 'Pendiente',
    problema_reportado TEXT,
    diagnostico TEXT,
    trabajo_realizado TEXT,
    mano_obra DECIMAL(10,2) NOT NULL DEFAULT 0,
    total_repuestos DECIMAL(10,2) NOT NULL DEFAULT 0,
    total DECIMAL(10,2) NOT NULL DEFAULT 0,
    observaciones TEXT,
    activo BOOLEAN DEFAULT TRUE,
    cliente_id BIGINT NOT NULL,
    vehiculo_id BIGINT NOT NULL,
    tecnico_id BIGINT,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id_cliente),
    FOREIGN KEY (vehiculo_id) REFERENCES vehiculo(id_vehiculo),
    FOREIGN KEY (tecnico_id) REFERENCES tecnico(id_tecnico)
);

CREATE TABLE IF NOT EXISTS diagnostico (
    id_diagnostico BIGINT PRIMARY KEY AUTO_INCREMENT,
    descripcion TEXT NOT NULL,
    recomendaciones TEXT,
    fecha_diagnostico DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    orden_servicio_id BIGINT NOT NULL,
    FOREIGN KEY (orden_servicio_id) REFERENCES orden_servicio(id_orden)
);

CREATE TABLE IF NOT EXISTS orden_repuesto (
    id_orden_repuesto BIGINT PRIMARY KEY AUTO_INCREMENT,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    orden_servicio_id BIGINT NOT NULL,
    repuesto_id BIGINT NOT NULL,
    FOREIGN KEY (orden_servicio_id) REFERENCES orden_servicio(id_orden),
    FOREIGN KEY (repuesto_id) REFERENCES repuesto(id_repuesto)
);

CREATE TABLE IF NOT EXISTS usuario (
    id_usuario BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    email VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- Insertar datos de ejemplo
INSERT INTO usuario (username, password, nombre, apellido, email, rol) VALUES
('admin', '$2a$10$NkMZ8jY9YtXxZzZzZzZzZu', 'Admin', 'Sistema', 'admin@autofast.com', 'ADMIN'),
('recepcionista', '$2a$10$NkMZ8jY9YtXxZzZzZzZzZu', 'María', 'López', 'maria@autofast.com', 'RECEPCIONISTA'),
('tecnico', '$2a$10$NkMZ8jY9YtXxZzZzZzZzZu', 'Carlos', 'Ramírez', 'carlos@autofast.com', 'TECNICO'),
('almacenero', '$2a$10$NkMZ8jY9YtXxZzZzZzZzZu', 'Ana', 'Martínez', 'ana@autofast.com', 'ALMACENERO');

INSERT INTO cliente (dni_ruc, nombre, apellido, telefono, email, direccion) VALUES
('12345678', 'María', 'Torres', '987654321', 'maria.torres@email.com', 'Av. Los Rosales 123'),
('87654321', 'Luis', 'Ramírez', '987654322', 'luis.ramirez@email.com', 'Calle Las Flores 456'),
('11223344', 'Ana', 'López', '987654323', 'ana.lopez@email.com', 'Jr. Los Olivos 789'),
('44332211', 'Carlos', 'Díaz', '987654324', 'carlos.diaz@email.com', 'Av. Los Pinos 321');

INSERT INTO vehiculo (placa, marca, modelo, anio, color, cliente_id) VALUES
('ABC-123', 'Toyota', 'Yaris', '2020', 'Rojo', 1),
('DEF-456', 'Kia', 'Rio', '2021', 'Blanco', 2),
('GHI-789', 'Hyundai', 'Accent', '2019', 'Negro', 3),
('JKL-012', 'Nissan', 'Versa', '2022', 'Gris', 4);

INSERT INTO tecnico (nombre, apellido, especialidad, telefono, email) VALUES
('Juan', 'Pérez', 'Motor', '987654331', 'juan.perez@autofast.com'),
('Pedro', 'González', 'Electricidad', '987654332', 'pedro.gonzalez@autofast.com');

INSERT INTO repuesto (codigo, nombre, descripcion, marca, precio_compra, precio_venta, stock_actual, stock_minimo) VALUES
('FIL-001', 'Filtro de Aceite', 'Filtro de aceite para motor', 'Bosch', 15.00, 25.00, 20, 5),
('BAT-002', 'Batería', 'Batería 12V 60Ah', 'Yuasa', 120.00, 180.00, 8, 3),
('LUB-003', 'Aceite Motor 5W-30', 'Aceite sintético 5W-30', 'Mobil', 30.00, 45.00, 15, 10);

-- Generar algunas órdenes de ejemplo
INSERT INTO orden_servicio (numero_orden, estado, problema_reportado, cliente_id, vehiculo_id, tecnico_id) VALUES
('OS-2026-001', 'Diagnosticado', 'Ruido extraño en el motor al acelerar', 1, 1, 1),
('OS-2026-002', 'En reparación', 'Problemas con el sistema eléctrico', 2, 2, 2),
('OS-2026-003', 'Finalizado', 'Revisión general y cambio de aceite', 3, 3, 1);