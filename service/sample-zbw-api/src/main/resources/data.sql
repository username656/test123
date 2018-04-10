--password=secret
INSERT INTO users (id, username, password, first_name, last_name, image, status, enabled, reset_key)
VALUES (1, 'user@example.org', '$2a$10$x5udSzDyTMWOkXG7HRJx5.LpqHayAUkvZ98Gw/aKxiMsdR3gboDJG', 'Default', 'User', '/assets/img/profile-image.jpg', 'ONLINE', true, 'valid-token');
