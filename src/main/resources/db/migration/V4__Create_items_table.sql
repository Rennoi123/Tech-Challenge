CREATE TABLE TB_ITEMS (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    price DECIMAL(19, 2) NOT NULL,
    dine_in_only BOOLEAN NOT NULL,
    photo_path VARCHAR(255),
    restaurant_id INT NOT NULL,
    available BOOLEAN NOT NULL DEFAULT,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT(id)
);