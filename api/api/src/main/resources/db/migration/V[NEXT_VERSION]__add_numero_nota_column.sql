ALTER TABLE notas ADD COLUMN numero_nota VARCHAR(50) NOT NULL DEFAULT '';
ALTER TABLE notas ADD CONSTRAINT uk_notas_numero_nota UNIQUE (numero_nota);

UPDATE notas SET numero_nota = CONCAT('NOTA-', LPAD(id, 6, '0')) WHERE numero_nota = '';

ALTER TABLE notas ALTER COLUMN numero_nota DROP DEFAULT;