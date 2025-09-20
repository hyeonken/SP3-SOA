CREATE TABLE cliente (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(120) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    consentimento_lgpd TINYINT(1) NOT NULL,
    objetivos VARCHAR(500),
    perfil_risco VARCHAR(20) NOT NULL,
    criado_em DATETIME NOT NULL
);

CREATE TABLE diagnostico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    perfil_risco VARCHAR(20) NOT NULL,
    score_risco INT NOT NULL,
    objetivos VARCHAR(500),
    recomendacao_geral VARCHAR(500),
    gerado_em DATETIME NOT NULL,
    CONSTRAINT fk_diagnostico_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT uk_diagnostico_cliente UNIQUE (cliente_id)
);

CREATE TABLE ativo (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    codigo VARCHAR(20) NOT NULL UNIQUE,
    nome VARCHAR(120) NOT NULL,
    classe VARCHAR(50) NOT NULL,
    perfil_risco VARCHAR(20) NOT NULL,
    retorno_esperado DECIMAL(10, 2)
);

CREATE TABLE carteira (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    descricao VARCHAR(300),
    perfil_risco VARCHAR(20) NOT NULL,
    retorno_esperado DECIMAL(10, 2),
    risco_estimado DECIMAL(10, 2)
);

CREATE TABLE item_carteira (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    carteira_id BIGINT NOT NULL,
    ativo_id BIGINT NOT NULL,
    percentual DECIMAL(5, 2) NOT NULL,
    CONSTRAINT fk_item_carteira FOREIGN KEY (carteira_id) REFERENCES carteira(id),
    CONSTRAINT fk_item_carteira_ativo FOREIGN KEY (ativo_id) REFERENCES ativo(id),
    CONSTRAINT uk_item_carteira UNIQUE (carteira_id, ativo_id)
);

CREATE TABLE recomendacao (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    cliente_id BIGINT NOT NULL,
    carteira_id BIGINT NOT NULL,
    justificativa VARCHAR(500),
    gerado_em DATETIME NOT NULL,
    CONSTRAINT fk_recomendacao_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id),
    CONSTRAINT fk_recomendacao_carteira FOREIGN KEY (carteira_id) REFERENCES carteira(id)
);

CREATE INDEX idx_recomendacao_cliente ON recomendacao(cliente_id);
CREATE INDEX idx_recomendacao_carteira ON recomendacao(carteira_id);
