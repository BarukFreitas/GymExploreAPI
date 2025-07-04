DROP TABLE IF EXISTS user_roles CASCADE;
DROP TABLE IF EXISTS reviews CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS gyms CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

DROP SEQUENCE IF EXISTS seq_post CASCADE;
DROP SEQUENCE IF EXISTS seq_review CASCADE;
DROP SEQUENCE IF EXISTS seq_user CASCADE;
DROP SEQUENCE IF EXISTS seq_gym CASCADE;
DROP SEQUENCE IF EXISTS seq_role CASCADE;


CREATE TABLE users (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE gyms (
    id SERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(255),
    address VARCHAR(255),
    phone VARCHAR(255),
    image_url VARCHAR(1024)
);

CREATE TABLE posts (
    id_post BIGSERIAL NOT NULL PRIMARY KEY,
    content TEXT NOT NULL,
    image_url VARCHAR(255),
    timestamp TIMESTAMP NOT NULL,
    id_user BIGINT NOT NULL,
    CONSTRAINT fk_posts_users FOREIGN KEY (id_user) REFERENCES users(id)
);

CREATE TABLE reviews (
    id_review SERIAL NOT NULL PRIMARY KEY,
    comment_text TEXT,
    rating SMALLINT NOT NULL,
    creation_date TIMESTAMP NOT NULL,
    id_gym INTEGER NOT NULL,
    id_user BIGINT NOT NULL,
    CONSTRAINT fk_reviews_gyms FOREIGN KEY (id_gym) REFERENCES gyms(id),
    CONSTRAINT fk_reviews_users FOREIGN KEY (id_user) REFERENCES users(id)
);

CREATE TABLE roles (
    id SERIAL NOT NULL PRIMARY KEY,
    role_name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO roles (role_name) VALUES ('ROLE_USER');
INSERT INTO roles (role_name) VALUES ('ROLE_GYM_OWNER');
INSERT INTO roles (role_name) VALUES ('ROLE_ADMIN');

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INTEGER NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);

COMMIT;