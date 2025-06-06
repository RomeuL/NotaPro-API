ALTER TABLE notas 
ADD COLUMN created_at TIMESTAMP,
ADD COLUMN updated_at TIMESTAMP;

UPDATE notas SET 
    created_at = CURRENT_TIMESTAMP, 
    updated_at = CURRENT_TIMESTAMP 
WHERE created_at IS NULL;

ALTER TABLE notas 
ALTER COLUMN created_at SET NOT NULL,
ALTER COLUMN updated_at SET NOT NULL;