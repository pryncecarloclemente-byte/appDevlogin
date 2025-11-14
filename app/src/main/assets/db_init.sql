-- Initial DB schema and seed data for appDevlogin
CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL DEFAULT 'guest'
);

-- Default admin account (email/password configurable in DatabaseHelper)
INSERT OR IGNORE INTO users (username, email, password, role) VALUES ('admin', 'admin@local', 'admin123', 'admin');

-- Sample guest accounts
INSERT OR IGNORE INTO users (username, email, password, role) VALUES ('guest1', 'guest1@example.com', 'guestpass', 'guest');
INSERT OR IGNORE INTO users (username, email, password, role) VALUES ('guest2', 'guest2@example.com', 'guestpass2', 'guest');
