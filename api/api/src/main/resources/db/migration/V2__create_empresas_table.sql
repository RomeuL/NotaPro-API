CREATE TABLE empresas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    cnpj VARCHAR(14) NOT NULL UNIQUE
);

-- Adicione um índice para melhorar a busca por CNPJ
CREATE INDEX idx_empresas_cnpj ON empresas (cnpj);