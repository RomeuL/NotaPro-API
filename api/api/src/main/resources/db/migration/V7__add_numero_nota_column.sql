DO $$ 
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns 
        WHERE table_name = 'notas' AND column_name = 'numero_nota'
    ) THEN
        ALTER TABLE notas ADD COLUMN numero_nota VARCHAR(50);
        
        UPDATE notas SET numero_nota = CONCAT('NOTA-', LPAD(id::text, 6, '0')) WHERE numero_nota IS NULL;
        
        ALTER TABLE notas ALTER COLUMN numero_nota SET NOT NULL;
        ALTER TABLE notas ADD CONSTRAINT uk_notas_numero_nota UNIQUE (numero_nota);
    END IF;
END $$;