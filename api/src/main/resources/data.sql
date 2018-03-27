INSERT INTO roles (id, name) VALUES (1, 'USER');
-- Important (when move to SpringBoot 2.0):
-- INSERT INTO users (id, username, password, first_name, last_name, enabled) VALUES (1, 'user', '{noop}secret', 'Default', 'User', true);
INSERT INTO users (id, username, password, first_name, last_name, image, status, enabled) VALUES (1, 'user@example.org', 'secret', 'Default', 'User', '/assets/img/profile-image.jpg', 'ONLINE', true);
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);
