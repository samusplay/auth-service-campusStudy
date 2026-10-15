CREATE TABLE IF NOT EXISTS users (
    usuario_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    rol VARCHAR(255),
    password_hash VARCHAR(255) NOT NULL,
    estado VARCHAR(255) NOT NULL,
    created_at TIMESTAMP
);
