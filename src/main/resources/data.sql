-- =======================
-- Роли
-- =======================
--INSERT INTO roles (id, name) VALUES (1, 'ROLE_USER') ON CONFLICT DO NOTHING;
--INSERT INTO roles (id, name) VALUES (2, 'ROLE_ADMIN') ON CONFLICT DO NOTHING;
--INSERT INTO roles (id, name) VALUES (3, 'ROLE_READER') ON CONFLICT DO NOTHING;
--
---- =======================
---- Тестовый пользователь
---- =======================
--INSERT INTO users (id, username, email, password, role_id)
--VALUES (2, 'admin', 'admin@example.com', '{bcrypt}$2a$10$abcdefghijklmnopqrstuv', 2);
---- password закодирован с BCrypt (например "password")
--
---- =======================
---- Связь пользователь ↔ роли
---- =======================
--INSERT INTO user_roles (user_id, role_id) VALUES (1, 1) ON CONFLICT DO NOTHING; -- ROLE_USER
--INSERT INTO user_roles (user_id, role_id) VALUES (1, 2) ON CONFLICT DO NOTHING; -- ROLE_ADMIN

-- Внешний ключ для роли пользователя

-- Внешний ключ для группы пользователя


-- Внешний ключ для роли пользователя
-- FK для группы пользователя
--ALTER TABLE users
--ADD CONSTRAINT fk_group
--FOREIGN KEY (group_id) REFERENCES groups(id)
--ON DELETE SET NULL DEFERRABLE INITIALLY DEFERRED;
--
---- FK для администратора группы
--ALTER TABLE groups
--ADD CONSTRAINT fk_admin
--FOREIGN KEY (admin_id) REFERENCES users(id)
--ON DELETE SET NULL DEFERRABLE INITIALLY DEFERRED;