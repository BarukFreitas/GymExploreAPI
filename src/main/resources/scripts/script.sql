-- Remove tabelas e sequências se elas já existirem, para garantir uma criação limpa
BEGIN
EXECUTE IMMEDIATE 'DROP TABLE posts';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE users';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE gyms';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE seq_post';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE seq_user';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE seq_gym';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
END IF;
END;


-- Cria a sequência para a tabela USERS
CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- Cria a tabela USERS
CREATE TABLE users (
                       id NUMBER(19,0) NOT NULL,
                       username VARCHAR2(255 CHAR) NOT NULL,
                       password VARCHAR2(255 CHAR) NOT NULL,
                       email VARCHAR2(255 CHAR) NOT NULL,
                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uk_users_username UNIQUE (username),
                       CONSTRAINT uk_users_email UNIQUE (email)
);


-- Cria a sequência para a tabela GYMS
CREATE SEQUENCE seq_gym
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- Cria a tabela GYMS
CREATE TABLE gyms (
                      id NUMBER(10,0) NOT NULL,
                      name VARCHAR2(255 CHAR),
                      address VARCHAR2(255 CHAR),
                      phone VARCHAR2(255 CHAR),
                      image_url VARCHAR2(1024 CHAR),
                      CONSTRAINT pk_gyms PRIMARY KEY (id)
);


-- Cria a sequência para a tabela POSTS
CREATE SEQUENCE seq_post
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- Cria a tabela POSTS (versão final e correta)
CREATE TABLE posts (
                       id_post NUMBER(19,0) NOT NULL,
                       content CLOB NOT NULL,
                       image_url VARCHAR2(255 CHAR),
                       timestamp TIMESTAMP NOT NULL,
                       id_user NUMBER(19,0) NOT NULL,
                       CONSTRAINT pk_posts PRIMARY KEY (id_post),
                       CONSTRAINT fk_posts_users FOREIGN KEY (id_user) REFERENCES users(id)
);

-- Cria a sequência para a tabela REVIEWS
CREATE SEQUENCE seq_review
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

-- Cria a tabela REVIEWS
CREATE TABLE reviews (
                         id_review NUMBER(10,0) NOT NULL,
                         comment_text CLOB,
                         rating NUMBER(2,0) NOT NULL,
                         creation_date TIMESTAMP NOT NULL,
                         id_gym NUMBER(10,0) NOT NULL,
                         id_user NUMBER(19,0) NOT NULL,
                         CONSTRAINT pk_reviews PRIMARY KEY (id_review),
                         CONSTRAINT fk_reviews_gyms FOREIGN KEY (id_gym) REFERENCES gyms(id),
                         CONSTRAINT fk_reviews_users FOREIGN KEY (id_user) REFERENCES users(id)
);

-- Confirma a transação
COMMIT;