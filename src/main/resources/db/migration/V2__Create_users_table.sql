CREATE TABLE TB_USERS (
                          id BIGSERIAL PRIMARY KEY,
                          name VARCHAR(255),
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255),
                          last_modified_date TIMESTAMP,
                          address_id BIGINT,
                          created_date TIMESTAMP,
                          roles VARCHAR(255),
                          FOREIGN KEY (address_id) REFERENCES ADDRESS(id)
);