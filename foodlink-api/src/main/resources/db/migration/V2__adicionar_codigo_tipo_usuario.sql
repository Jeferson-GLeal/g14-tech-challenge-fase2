ALTER TABLE tipos_usuario ADD COLUMN codigo VARCHAR(50);

ALTER TABLE tipos_usuario ALTER COLUMN codigo SET NOT NULL;
