CREATE TABLE IF NOT EXISTS movimiento_inventario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    inventario_id BIGINT NOT NULL,
    tipo_movimiento ENUM('ENTRADA', 'SALIDA') NOT NULL,
    cantidad INT NOT NULL,
    motivo VARCHAR(255),
    fecha DATETIME NOT NULL,
    CONSTRAINT fk_movimiento_inventario FOREIGN KEY (inventario_id) REFERENCES inventario(id)
);