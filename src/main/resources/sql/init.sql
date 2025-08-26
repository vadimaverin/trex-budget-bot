CREATE TABLE IF NOT EXISTS users
(
    id BIGSERIAL PRIMARY KEY,
    chat_id BIGSERIAL,
    user_name VARCHAR(255),
    state VARCHAR(32) NOT NULL,
    update_id BIGINT
    );

-- DROP TABLE users;
-- DROP TABLE expenses;
-- DROP TABLE categories CASCADE;

CREATE TABLE IF NOT EXISTS categories
(
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255),
    alias VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS expenses
(
    id BIGSERIAL PRIMARY KEY,
    date TIMESTAMP,
    amount INT NOT NULL,
    category_id BIGINT NOT NULL REFERENCES categories (id),
    description VARCHAR(255)
);

CREATE DATABASE tgbotdata;