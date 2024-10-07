CREATE TABLE enderecos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cep VARCHAR(20),
    numero VARCHAR(20),
    logradouro VARCHAR(255),
    bairro VARCHAR(255),
    cidade VARCHAR(255),
    estado VARCHAR(50)
);

CREATE TABLE clientes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    cpf VARCHAR(20) NOT NULL UNIQUE,
    endereco_id BIGINT,
    FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);

