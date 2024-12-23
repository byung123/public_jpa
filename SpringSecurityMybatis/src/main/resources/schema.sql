-- BIGINT가 Long과 비슷한 자료형
-- DROP TABLE IF EXISTS USER;
-- DROP TABLE IF EXISTS ROLE;
-- DROP TABLE IF EXISTS USER_ROLES;
-- DROP TABLE IF EXISTS OAUTH2_USER;
--
-- CREATE TABLE USER (
--     id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
--     username VARCHAR(255) UNIQUE not null,
--     password VARCHAR(255) not null,
--     name VARCHAR(255) not null,
--     email VARCHAR(255) not null,
--     img TEXT not null DEFAULT 'https://firebasestorage.googleapis.com/v0/b/userprofile-1def1.appspot.com/o/user%2Fdefault.png?alt=media&token=7cd1869a-f581-451d-89cc-bc7342879f56'
-- );
--
-- CREATE TABLE ROLE (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     name VARCHAR(255) UNIQUE not null
-- );
--
-- INSERT INTO ROLE
-- VALUES(DEFAULT, 'ROLE_USER'),
--       (DEFAULT, 'ROLE_MANAGER'),
--       (DEFAULT, 'ROLE_ADMIN');
--
-- CREATE TABLE USER_ROLES (
--     id BIGINT AUTO_INCREMENT PRIMARY KEY,
--     user_id BIGINT not null,
--     role_id BIGINT not null
-- );

CREATE TABLE OAUTH2_USER (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT not null,
    oauth2_name VARCHAR(255) UNIQUE not null,
    provider VARCHAR(255) not null
);

-- ALTER TABLE 'USER' ADD COLUMN 'email' VARCHAR(255) NOT NULL;