CREATE DATABASE IF NOT EXISTS saep_db;
USE saep_db;

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    login VARCHAR(45) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    papel ENUM('ROLE_ADMIN', 'ROLE_USER') NOT NULL
);

CREATE TABLE IF NOT EXISTS produto (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(45) NOT NULL UNIQUE,
    nome VARCHAR(45) NOT NULL,
    caracteristicas VARCHAR(45) NOT NULL,
    estoque_atual INT NOT NULL,
    estoque_minimo INT NOT NULL,
    data_cadastro DATETIME
);

CREATE TABLE IF NOT EXISTS movimentacao_estoque (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_produto BIGINT NOT NULL,
    id_usuario BIGINT NOT NULL,
    tipo_movimentacao ENUM('ENTRADA', 'SAIDA') NOT NULL,
    quantidade INT NOT NULL,
    data_movimentacao DATETIME NOT NULL,
    FOREIGN KEY (id_produto) REFERENCES produto(id),
    FOREIGN KEY (id_usuario) REFERENCES usuario(id)
);
