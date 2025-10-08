INSERT INTO ADDRESS (id, street, number, neighborhood, city, state, postal_code, created_date, last_modified_date)
VALUES (999, 'Rua do Administrador', '123', 'Centro', 'Cidade Admin', 'SP', '12345-678', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

INSERT INTO TB_USERS (name, email, password, roles, created_date, last_modified_date, address_id)
VALUES ('Admin', 'admin@example.com', '$2a$10$tqs3qCxLd9k3.8jOV4qpBeD4ueV7wLevEEg58zdclLOSKHsCAfT76', 'ADMIN', NOW(), NOW(), 999)
    ON CONFLICT DO NOTHING;
