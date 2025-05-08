CREATE TABLE prestamo (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          isbn VARCHAR(10),
                          identificacion_usuario VARCHAR(10),
                          tipo_usuario INT,
                          fecha_maxima_devolucion DATE
);
