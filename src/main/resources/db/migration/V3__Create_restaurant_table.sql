CREATE TABLE RESTAURANT (
                            id BIGSERIAL PRIMARY KEY,
                            name VARCHAR(255) NOT NULL,
                            address_id BIGINT,
                            cuisine_type VARCHAR(255) NOT NULL,
                            opening_time TIME NOT NULL,
                            closing_time TIME NOT NULL,
                            owner_id BIGINT,
                            FOREIGN KEY (address_id) REFERENCES ADDRESS(id),
                            FOREIGN KEY (owner_id) REFERENCES TB_USERS(id)
);