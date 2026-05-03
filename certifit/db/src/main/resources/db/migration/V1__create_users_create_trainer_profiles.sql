CREATE TYPE user_role AS ENUM ('ADMIN', 'TRAINER', 'CLIENT');

CREATE TABLE trainer_profiles
(
    id             UUID                                   NOT NULL,
    user_id        UUID                                   NOT NULL,
    bio            TEXT,
    specialization VARCHAR(255),
    timezone       VARCHAR(100),
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_trainer_profiles PRIMARY KEY (id)
);

CREATE TABLE users
  (
      id             UUID                                   NOT NULL,
      first_name     VARCHAR(100)                           NOT NULL,
      last_name      VARCHAR(100)                           NOT NULL,
      email          VARCHAR(255)                           NOT NULL,
      password_hash  VARCHAR(255)                           NOT NULL,
      role           USER_ROLE,
      phone          VARCHAR(30),
      avatar_url     TEXT,
      email_verified BOOLEAN                                NOT NULL,
      deleted_at     TIMESTAMP WITH TIME ZONE,
      created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
      updated_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
      CONSTRAINT pk_users PRIMARY KEY (id)
  );

ALTER TABLE trainer_profiles
    ADD CONSTRAINT uc_trainer_profiles_user UNIQUE (user_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE trainer_profiles
    ADD CONSTRAINT FK_TRAINER_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);