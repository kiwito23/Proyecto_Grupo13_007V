CREATE TABLE IF NOT EXISTS inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    producto_id BIGINT NOT NULL UNIQUE,
    stock_actual INT NOT NULL DEFAULT 0,
    stock_minimo INT NOT NULL DEFAULT 5,
    ultima_actualizacion DATETIME NOT NULL
);

