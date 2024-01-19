CREATE TABLE events (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    file_id INT,
    status VARCHAR(32),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (file_id) REFERENCES files (id) ON DELETE CASCADE
);