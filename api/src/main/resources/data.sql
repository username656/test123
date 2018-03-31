INSERT INTO roles (id, name) VALUES (1, 'USER');
-- Important (when move to SpringBoot 2.0):
-- INSERT INTO users (id, username, password, first_name, last_name, enabled) VALUES (1, 'user', '{noop}secret', 'Default', 'User', true);
INSERT INTO users (id, username, password, first_name, last_name, image, status, enabled, reset_key) 
VALUES (1, 'user@example.org', 'secret', 'Default', 'User', '/assets/img/profile-image.jpg', 'ONLINE', true, 'valid-token');
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO tokens (token, expiration_date, type, user_id) VALUES ('validtoken', '2022-03-27 15:38:00', 'FORGOT_PASSWORD', 1);
INSERT INTO tokens (token, expiration_date, type, user_id) VALUES ('invalidtoken', '2016-03-27 15:38:00', 'FORGOT_PASSWORD', 1);

