INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_USER');

INSERT INTO users(username,email,password) VALUES ('admin','admin@gmail.com','$2a$10$23LrDJJgDSek5TLrTMFKtOM4i9Ufi3Yse7DEyf9x7Y5wdy5UBsE1q');
INSERT INTO user_roles(user_id,role_id) VALUES(1,1);



