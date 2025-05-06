CREATE TABLE notas (
    id SERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    empresa_id INT NOT NULL,
    data_emissao DATE NOT NULL,
    data_vencimento DATE NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    tipo_pagamento VARCHAR(30) NOT NULL,
    numero_boleto VARCHAR(50),
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

-- Adicione índices para melhorar o desempenho das consultas
CREATE INDEX idx_notas_empresa ON notas (empresa_id);
CREATE INDEX idx_notas_status ON notas (status);
CREATE INDEX idx_notas_vencimento ON notas (data_vencimento);