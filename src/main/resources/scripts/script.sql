BEGIN
EXECUTE IMMEDIATE 'DROP TABLE posts';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE reviews';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -942 THEN
         RAISE;
END IF;
END;

BEGIN
EXECUTE IMMEDIATE 'DROP TABLE user_roles';
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
EXECUTE IMMEDIATE 'DROP TABLE roles';
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
EXECUTE IMMEDIATE 'DROP SEQUENCE seq_review';
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

BEGIN
EXECUTE IMMEDIATE 'DROP SEQUENCE seq_role';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE != -2289 THEN
         RAISE;
END IF;
END;


CREATE SEQUENCE seq_user
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

CREATE TABLE users (
                       id NUMBER(19,0) NOT NULL,
                       username VARCHAR2(255 CHAR) NOT NULL,
                       password VARCHAR2(255 CHAR) NOT NULL,
                       email VARCHAR2(255 CHAR) NOT NULL,
                       points NUMBER(10,0) DEFAULT 0 NOT NULL,
                       CONSTRAINT pk_users PRIMARY KEY (id),
                       CONSTRAINT uk_users_username UNIQUE (username),
                       CONSTRAINT uk_users_email UNIQUE (email)
);


CREATE SEQUENCE seq_gym
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

CREATE TABLE gyms (
                      id NUMBER(10,0) NOT NULL,
                      name VARCHAR2(255 CHAR),
                      address VARCHAR2(255 CHAR),
                      phone VARCHAR2(255 CHAR),
                      image_url VARCHAR2(1024 CHAR),
                      CONSTRAINT pk_gyms PRIMARY KEY (id)
);


CREATE SEQUENCE seq_post
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

CREATE TABLE posts (
                       id_post NUMBER(19,0) NOT NULL,
                       content CLOB NOT NULL,
                       image_url VARCHAR2(255 CHAR),
                       timestamp TIMESTAMP NOT NULL,
                       id_user NUMBER(19,0) NOT NULL,
                       CONSTRAINT pk_posts PRIMARY KEY (id_post),
                       CONSTRAINT fk_posts_users FOREIGN KEY (id_user) REFERENCES users(id)
);

CREATE SEQUENCE seq_review
    START WITH 1
    INCREMENT BY 1
    NOCACHE
NOCYCLE;

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

CREATE SEQUENCE seq_role
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

CREATE TABLE roles (
                       id NUMBER(10,0) NOT NULL,
                       role_name VARCHAR2(50 CHAR) NOT NULL UNIQUE,
                       CONSTRAINT pk_roles PRIMARY KEY (id)
);

INSERT INTO roles (id, role_name) VALUES (seq_role.NEXTVAL, 'ROLE_USER');
INSERT INTO roles (id, role_name) VALUES (seq_role.NEXTVAL, 'ROLE_GYM_OWNER');
INSERT INTO roles (id, role_name) VALUES (seq_role.NEXTVAL, 'ROLE_ADMIN');

CREATE TABLE user_roles (
                            user_id NUMBER(19,0) NOT NULL,
                            role_id NUMBER(10,0) NOT NULL,
                            CONSTRAINT pk_user_roles PRIMARY KEY (user_id, role_id),
                            CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users(id),
                            CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles(id)
);


CREATE SEQUENCE seq_gamification_log
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;


CREATE TABLE gamification_log (
                                  id_gamification_log NUMBER(19,0) NOT NULL,
                                  id_user NUMBER(19,0) NOT NULL,
                                  action_type VARCHAR2(50 CHAR) NOT NULL,
                                  action_date DATE NOT NULL,
                                  CONSTRAINT pk_gamification_log PRIMARY KEY (id_gamification_log),
                                  CONSTRAINT fk_gamification_log_users FOREIGN KEY (id_user) REFERENCES users(id)
);

COMMIT;