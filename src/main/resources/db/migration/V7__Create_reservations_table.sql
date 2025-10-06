CREATE TABLE reservations (
    id SERIAL PRIMARY KEY,
    restaurant_id INT NOT NULL,
    user_id INT NOT NULL,
    reservation_time TIMESTAMP NOT NULL,
    number_of_people INT NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES RESTAURANT(id),
    FOREIGN KEY (user_id) REFERENCES TB_USERS(id)
);