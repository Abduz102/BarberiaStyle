-- ============================================================
--  Barberia Style — Script de base de datos MySQL
-- ============================================================

CREATE DATABASE IF NOT EXISTS barberia_style
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE barberia_style;

-- ── Tabla cliente ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cliente(
    id_cliente   INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    apellido     VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    estado       BOOLEAN      NOT NULL DEFAULT TRUE,
    saldo        DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    puntos       DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    tipo_cliente VARCHAR(50)  NOT NULL DEFAULT 'Cliente Nuevo',
    es_recurrente BOOLEAN     NOT NULL DEFAULT FALSE
);

-- ── Tabla barbero ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS barbero(
    id_barbero   INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    apellido     VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    estado       BOOLEAN      NOT NULL DEFAULT TRUE,
    especialidad VARCHAR(100) NOT NULL DEFAULT 'General',
    saldo        DECIMAL(12,2) NOT NULL DEFAULT 0.00
);

-- ── Tabla administrador ──────────────────────────────────────
CREATE TABLE IF NOT EXISTS administrador (
    id_admin     INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    apellido     VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    password     VARCHAR(255) NOT NULL,
    estado       BOOLEAN      NOT NULL DEFAULT TRUE,
    saldo        DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    rol          VARCHAR(50)  NOT NULL DEFAULT 'Admin'
);

-- ── Tabla cita ───────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS cita (
    id_cita       INT AUTO_INCREMENT PRIMARY KEY,
    fecha_hora    DATETIME     NOT NULL,
    id_cliente    INT          NOT NULL,
    id_barbero    INT          NOT NULL,
    tipo_servicio VARCHAR(100) NOT NULL,
    monto_pagado  DECIMAL(12,2) NOT NULL,
    metodo_pago   VARCHAR(50)  NOT NULL,
    estado        VARCHAR(30)  NOT NULL DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente) REFERENCES cliente(id_cliente) ON DELETE CASCADE,
    FOREIGN KEY (id_barbero) REFERENCES barbero(id_barbero) ON DELETE CASCADE
);

-- ── Datos de prueba ──────────────────────────────────────────
INSERT INTO administrador (nombre, apellido, email, password, estado, saldo, rol)
VALUES ('Admin', 'Sistema', 'admin@barberia.com', 'admin123', TRUE, 0.00, 'Admin');

INSERT INTO barbero (nombre, apellido, email, password, estado, especialidad, saldo) VALUES
('Mike',  'Lopez',  'mike@barberia.com',  '123456', TRUE, 'Estilos Clasicos',  0.00),
('Jhon',  'Perez',  'jhon@barberia.com',  '123456', TRUE, 'Degradados VIP',    0.00),
('Alex',  'Torres', 'alex@barberia.com',  '123456', TRUE, 'Spa de Barba',      0.00);

INSERT INTO cliente (nombre, apellido, email, password, estado, saldo, puntos, tipo_cliente, es_recurrente) VALUES
('Cliente', 'Estrella', 'cliente@mail.com', '123456', TRUE, 150000.00, 10.00, 'Cliente Recurrente', TRUE),
('Luis',    'Garcia',   'luis@mail.com',    '123456', TRUE,      0.00,  0.00, 'Cliente Nuevo',      FALSE);
