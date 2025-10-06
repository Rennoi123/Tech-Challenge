CREATE TABLE ADDRESS (
    id SERIAL PRIMARY KEY,
    street VARCHAR(100) NOT NULL,
    number VARCHAR(10) NOT NULL,
    complement VARCHAR(50),
    neighborhood VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(2) NOT NULL,
    postal_code VARCHAR(9) NOT NULL,
    last_modified_date TIMESTAMP,
    created_date TIMESTAMP
);