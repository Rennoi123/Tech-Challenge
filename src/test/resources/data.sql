INSERT INTO address (id, street, number, neighborhood, city, state, postal_code, created_date, last_modified_date)
VALUES (999, 'Rua do Administrador', '123', 'Centro', 'Cidade Admin', 'SP', '12345-678', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_users (id, name, email, password, roles, created_date, last_modified_date, address_id)
VALUES (1, 'Admin', 'admin@example.com',
        '$2a$10$tqs3qCxLd9k3.8jOV4qpBeD4ueV7wLevEEg58zdclLOSKHsCAfT76',
        'ADMIN', NOW(), NOW(), 999)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO address (id, street, number, neighborhood, city, state, postal_code, created_date, last_modified_date)
VALUES (1000, 'Rua Teste', '123', 'Centro', 'São Paulo', 'SP', '12345000', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

INSERT INTO restaurant (id, name, cuisine_type, opening_time, closing_time, capacity, qtd_table, address_id, owner_id)
VALUES (1, 'Restaurante do Zé', 'Brasileira', '08:00:00', '23:00:00', 40, 20, 1000, 1)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_items (id, name, description, price, restaurant_id, dine_in_only)
VALUES (1, 'Prato Executivo', 'Arroz, feijão, bife e batata frita', 29.90, 1, true)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO tb_users (id, name, email, password, roles, created_date, last_modified_date, address_id)
VALUES (4, 'Cliente Teste', 'cliente@example.com',
        '$2a$10$tqs3qCxLd9k3.8jOV4qpBeD4ueV7wLevEEg58zdclLOSKHsCAfT76',
        'CLIENTE', NOW(), NOW(), 1000)
    ON CONFLICT (id) DO NOTHING;

INSERT INTO reservations  (id, restaurant_id, user_id, reservation_time, number_of_people, status, created_at, updated_at)
VALUES (1, 1, 1, '2025-10-15 20:00:00', 2, 'PENDENTE', NOW(), NOW())
    ON CONFLICT (id) DO NOTHING;

ALTER SEQUENCE IF EXISTS address_id_seq RESTART WITH 2000;
ALTER SEQUENCE IF EXISTS tb_users_id_seq RESTART WITH 10;
ALTER SEQUENCE IF EXISTS restaurant_id_seq RESTART WITH 5;
ALTER SEQUENCE IF EXISTS tb_items_id_seq RESTART WITH 10;
ALTER SEQUENCE IF EXISTS reservations_id_seq RESTART WITH 10;