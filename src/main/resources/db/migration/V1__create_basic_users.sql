CREATE TABLE owner (
                        id SERIAL PRIMARY KEY,
                        login VARCHAR(255) NOT NULL,
                        username VARCHAR(255) NOT NULL,
                        password VARCHAR(255) NOT NULL
);
--password 'admin123'
INSERT INTO owner (username, login, password)
VALUES ('admin', 'admin', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2');

--password 'user123'
INSERT INTO owner (username, login, password)
VALUES ('user', 'user', '$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K');
