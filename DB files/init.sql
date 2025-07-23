-- init.sql: Initial schema for Personal Event Planner API

CREATE DATABASE IF NOT EXISTS personal_events_db;
USE personal_events_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE events (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    created_at DATETIME NOT NULL,
    type VARCHAR(30),
    importance VARCHAR(30),
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Optional sample insert (commented out)
-- INSERT INTO users (username, password, email, role) VALUES
-- ('example_user', '$2a$10$xxxxxxxx', 'user@example.com', 'USER');
