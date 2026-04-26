CREATE TYPE user_role         AS ENUM ('TRAINER', 'CLIENT');
CREATE TYPE gender_type       AS ENUM ('MALE', 'FEMALE', 'OTHER');
CREATE TYPE client_status     AS ENUM ('ACTIVE', 'PAUSED', 'ARCHIVED');
CREATE TYPE assignment_status AS ENUM ('ACTIVE', 'COMPLETED', 'CANCELLED');
CREATE TYPE difficulty_level  AS ENUM ('BEGINNER', 'INTERMEDIATE', 'ADVANCED');
CREATE TYPE exercise_category AS ENUM ('STRENGTH', 'CARDIO', 'MOBILITY', 'FLEXIBILITY', 'SPORT');
CREATE TYPE equipment_type    AS ENUM ('BARBELL', 'DUMBBELL', 'CABLE', 'MACHINE', 'BODYWEIGHT', 'KETTLEBELL', 'BAND', 'OTHER');
CREATE TYPE muscle_group      AS ENUM ('CHEST', 'BACK', 'SHOULDERS', 'BICEPS', 'TRICEPS', 'CORE', 'GLUTES', 'QUADS', 'HAMSTRINGS', 'CALVES', 'FULL_BODY');
CREATE TYPE meal_type         AS ENUM ('BREAKFAST', 'LUNCH', 'DINNER', 'SNACK', 'PRE_WORKOUT', 'POST_WORKOUT');
CREATE TYPE photo_angle       AS ENUM ('FRONT', 'BACK', 'SIDE_LEFT', 'SIDE_RIGHT');
CREATE TYPE thread_status     AS ENUM ('OPEN', 'AWAITING_TRAINER', 'AWAITING_CLIENT', 'RESOLVED');
CREATE TYPE attachment_type   AS ENUM ('VIDEO', 'IMAGE', 'AUDIO');

CREATE TABLE body_measurements
(
    id           UUID                                   NOT NULL,
    checkin_id   UUID                                   NOT NULL,
    weight_kg    FLOAT,
    body_fat_pct FLOAT,
    chest_cm     FLOAT,
    waist_cm     FLOAT,
    hips_cm      FLOAT,
    thigh_cm     FLOAT,
    arm_cm       FLOAT,
    measured_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_body_measurements PRIMARY KEY (id)
);

CREATE TABLE client_profiles
(
    id            UUID                                   NOT NULL,
    user_id       UUID                                   NOT NULL,
    trainer_id    UUID                                   NOT NULL,
    date_of_birth date,
    gender        GENDER_TYPE,
    height_cm     FLOAT,
    goal          TEXT,
    status        CLIENT_STATUS                          NOT NULL,
    onboarded_at  TIMESTAMP WITH TIME ZONE,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_client_profiles PRIMARY KEY (id)
);

CREATE TABLE exercise_logs
(
    id                  UUID    NOT NULL,
    workout_log_id      UUID    NOT NULL,
    workout_exercise_id UUID    NOT NULL,
    set_number          INTEGER NOT NULL,
    reps_done           INTEGER,
    weight_used         FLOAT,
    duration_seconds    INTEGER,
    completed           BOOLEAN NOT NULL,
    CONSTRAINT pk_exercise_logs PRIMARY KEY (id)
);

CREATE TABLE exercise_thread_attachments
(
    id               UUID                                   NOT NULL,
    message_id       UUID                                   NOT NULL,
    type             ATTACHMENT_TYPE                        NOT NULL,
    file_url         TEXT                                   NOT NULL,
    thumbnail_url    TEXT,
    duration_seconds INTEGER,
    file_size_bytes  BIGINT,
    mime_type        VARCHAR(100),
    uploaded_at      TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_exercise_thread_attachments PRIMARY KEY (id)
);

CREATE TABLE exercise_thread_messages
(
    id        UUID                                   NOT NULL,
    thread_id UUID                                   NOT NULL,
    sender_id UUID                                   NOT NULL,
    body      TEXT,
    is_read   BOOLEAN                                NOT NULL,
    sent_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_exercise_thread_messages PRIMARY KEY (id)
);

CREATE TABLE exercise_threads
(
    id          UUID                                   NOT NULL,
    exercise_id UUID                                   NOT NULL,
    client_id   UUID                                   NOT NULL,
    trainer_id  UUID                                   NOT NULL,
    status      THREAD_STATUS                          NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    resolved_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT pk_exercise_threads PRIMARY KEY (id)
);

CREATE TABLE exercises
(
    id           UUID                                   NOT NULL,
    created_by   UUID                                   NOT NULL,
    name         VARCHAR(255)                           NOT NULL,
    description  TEXT,
    video_url    TEXT,
    category     EXERCISE_CATEGORY                      NOT NULL,
    equipment    EQUIPMENT_TYPE,
    muscle_group MUSCLE_GROUP,
    is_public    BOOLEAN                                NOT NULL,
    deleted_at   TIMESTAMP WITH TIME ZONE,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);

CREATE TABLE food_items
(
    id        UUID         NOT NULL,
    meal_id   UUID         NOT NULL,
    name      VARCHAR(255) NOT NULL,
    quantity  FLOAT        NOT NULL,
    unit      VARCHAR(30)  NOT NULL,
    calories  INTEGER,
    protein_g FLOAT,
    carbs_g   FLOAT,
    fat_g     FLOAT,
    CONSTRAINT pk_food_items PRIMARY KEY (id)
);

CREATE TABLE meals
(
    id                UUID         NOT NULL,
    nutrition_plan_id UUID         NOT NULL,
    name              VARCHAR(100) NOT NULL,
    meal_type         MEAL_TYPE,
    order_index       INTEGER      NOT NULL,
    CONSTRAINT pk_meals PRIMARY KEY (id)
);

CREATE TABLE messages
(
    id          UUID                                   NOT NULL,
    sender_id   UUID                                   NOT NULL,
    receiver_id UUID                                   NOT NULL,
    body        TEXT                                   NOT NULL,
    is_read     BOOLEAN                                NOT NULL,
    sent_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_messages PRIMARY KEY (id)
);

CREATE TABLE nutrition_assignments
(
    id                UUID                                   NOT NULL,
    nutrition_plan_id UUID                                   NOT NULL,
    client_id         UUID                                   NOT NULL,
    start_date        date                                   NOT NULL,
    end_date          date,
    status            ASSIGNMENT_STATUS                      NOT NULL,
    assigned_at       TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_nutrition_assignments PRIMARY KEY (id)
);

CREATE TABLE nutrition_logs
(
    id                      UUID                                   NOT NULL,
    client_id               UUID                                   NOT NULL,
    nutrition_assignment_id UUID,
    logged_date             date                                   NOT NULL,
    calories_consumed       INTEGER,
    protein_g               FLOAT,
    carbs_g                 FLOAT,
    fat_g                   FLOAT,
    notes                   TEXT,
    created_at              TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_nutrition_logs PRIMARY KEY (id)
);

CREATE TABLE nutrition_plans
(
    id               UUID                                   NOT NULL,
    trainer_id       UUID                                   NOT NULL,
    title            VARCHAR(255)                           NOT NULL,
    description      TEXT,
    target_calories  INTEGER,
    target_protein_g FLOAT,
    target_carbs_g   FLOAT,
    target_fat_g     FLOAT,
    is_template      BOOLEAN                                NOT NULL,
    deleted_at       TIMESTAMP WITH TIME ZONE,
    created_at       TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_nutrition_plans PRIMARY KEY (id)
);

CREATE TABLE progress_checkins
(
    id           UUID                                   NOT NULL,
    client_id    UUID                                   NOT NULL,
    checkin_date date                                   NOT NULL,
    trainer_note TEXT,
    client_note  TEXT,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_progress_checkins PRIMARY KEY (id)
);

CREATE TABLE progress_photos
(
    id         UUID                                   NOT NULL,
    checkin_id UUID                                   NOT NULL,
    angle      PHOTO_ANGLE                            NOT NULL,
    photo_url  TEXT                                   NOT NULL,
    taken_at   TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_progress_photos PRIMARY KEY (id)
);

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

CREATE TABLE workout_assignments
(
    id              UUID                                   NOT NULL,
    workout_plan_id UUID                                   NOT NULL,
    client_id       UUID                                   NOT NULL,
    start_date      date                                   NOT NULL,
    end_date        date,
    status          ASSIGNMENT_STATUS                      NOT NULL,
    assigned_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_workout_assignments PRIMARY KEY (id)
);

CREATE TABLE workout_days
(
    id              UUID    NOT NULL,
    workout_plan_id UUID    NOT NULL,
    day_number      INTEGER NOT NULL,
    label           VARCHAR(100),
    CONSTRAINT pk_workout_days PRIMARY KEY (id)
);

CREATE TABLE workout_exercises
(
    id               UUID    NOT NULL,
    workout_day_id   UUID    NOT NULL,
    exercise_id      UUID    NOT NULL,
    order_index      INTEGER NOT NULL,
    sets             INTEGER,
    reps             INTEGER,
    rest_seconds     INTEGER,
    weight_kg        FLOAT,
    duration_seconds INTEGER,
    notes            TEXT,
    CONSTRAINT pk_workout_exercises PRIMARY KEY (id)
);

CREATE TABLE workout_logs
(
    id                    UUID                                   NOT NULL,
    client_id             UUID                                   NOT NULL,
    workout_assignment_id UUID,
    workout_day_id        UUID                                   NOT NULL,
    logged_date           date                                   NOT NULL,
    notes                 TEXT,
    created_at            TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_workout_logs PRIMARY KEY (id)
);

CREATE TABLE workout_plans
(
    id             UUID                                   NOT NULL,
    trainer_id     UUID                                   NOT NULL,
    title          VARCHAR(255)                           NOT NULL,
    description    TEXT,
    difficulty     DIFFICULTY_LEVEL,
    duration_weeks INTEGER,
    is_template    BOOLEAN                                NOT NULL,
    deleted_at     TIMESTAMP WITH TIME ZONE,
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_workout_plans PRIMARY KEY (id)
);

ALTER TABLE client_profiles
    ADD CONSTRAINT uc_client_profiles_user UNIQUE (user_id);

ALTER TABLE trainer_profiles
    ADD CONSTRAINT uc_trainer_profiles_user UNIQUE (user_id);

ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE body_measurements
    ADD CONSTRAINT FK_BODY_MEASUREMENTS_ON_CHECKIN FOREIGN KEY (checkin_id) REFERENCES progress_checkins (id);

ALTER TABLE client_profiles
    ADD CONSTRAINT FK_CLIENT_PROFILES_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES trainer_profiles (id);

ALTER TABLE client_profiles
    ADD CONSTRAINT FK_CLIENT_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE exercises
    ADD CONSTRAINT FK_EXERCISES_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES users (id);

ALTER TABLE exercise_logs
    ADD CONSTRAINT FK_EXERCISE_LOGS_ON_WORKOUT_EXERCISE FOREIGN KEY (workout_exercise_id) REFERENCES workout_exercises (id);

ALTER TABLE exercise_logs
    ADD CONSTRAINT FK_EXERCISE_LOGS_ON_WORKOUT_LOG FOREIGN KEY (workout_log_id) REFERENCES workout_logs (id);

ALTER TABLE exercise_threads
    ADD CONSTRAINT FK_EXERCISE_THREADS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE exercise_threads
    ADD CONSTRAINT FK_EXERCISE_THREADS_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercises (id);

ALTER TABLE exercise_threads
    ADD CONSTRAINT FK_EXERCISE_THREADS_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES trainer_profiles (id);

ALTER TABLE exercise_thread_attachments
    ADD CONSTRAINT FK_EXERCISE_THREAD_ATTACHMENTS_ON_MESSAGE FOREIGN KEY (message_id) REFERENCES exercise_thread_messages (id);

ALTER TABLE exercise_thread_messages
    ADD CONSTRAINT FK_EXERCISE_THREAD_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE exercise_thread_messages
    ADD CONSTRAINT FK_EXERCISE_THREAD_MESSAGES_ON_THREAD FOREIGN KEY (thread_id) REFERENCES exercise_threads (id);

ALTER TABLE food_items
    ADD CONSTRAINT FK_FOOD_ITEMS_ON_MEAL FOREIGN KEY (meal_id) REFERENCES meals (id);

ALTER TABLE meals
    ADD CONSTRAINT FK_MEALS_ON_NUTRITION_PLAN FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_RECEIVER FOREIGN KEY (receiver_id) REFERENCES users (id);

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_SENDER FOREIGN KEY (sender_id) REFERENCES users (id);

ALTER TABLE nutrition_assignments
    ADD CONSTRAINT FK_NUTRITION_ASSIGNMENTS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE nutrition_assignments
    ADD CONSTRAINT FK_NUTRITION_ASSIGNMENTS_ON_NUTRITION_PLAN FOREIGN KEY (nutrition_plan_id) REFERENCES nutrition_plans (id);

ALTER TABLE nutrition_logs
    ADD CONSTRAINT FK_NUTRITION_LOGS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE nutrition_logs
    ADD CONSTRAINT FK_NUTRITION_LOGS_ON_NUTRITION_ASSIGNMENT FOREIGN KEY (nutrition_assignment_id) REFERENCES nutrition_assignments (id);

ALTER TABLE nutrition_plans
    ADD CONSTRAINT FK_NUTRITION_PLANS_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES users (id);

ALTER TABLE progress_checkins
    ADD CONSTRAINT FK_PROGRESS_CHECKINS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE progress_photos
    ADD CONSTRAINT FK_PROGRESS_PHOTOS_ON_CHECKIN FOREIGN KEY (checkin_id) REFERENCES progress_checkins (id);

ALTER TABLE trainer_profiles
    ADD CONSTRAINT FK_TRAINER_PROFILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE workout_assignments
    ADD CONSTRAINT FK_WORKOUT_ASSIGNMENTS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE workout_assignments
    ADD CONSTRAINT FK_WORKOUT_ASSIGNMENTS_ON_WORKOUT_PLAN FOREIGN KEY (workout_plan_id) REFERENCES workout_plans (id);

ALTER TABLE workout_days
    ADD CONSTRAINT FK_WORKOUT_DAYS_ON_WORKOUT_PLAN FOREIGN KEY (workout_plan_id) REFERENCES workout_plans (id);

ALTER TABLE workout_exercises
    ADD CONSTRAINT FK_WORKOUT_EXERCISES_ON_EXERCISE FOREIGN KEY (exercise_id) REFERENCES exercises (id);

ALTER TABLE workout_exercises
    ADD CONSTRAINT FK_WORKOUT_EXERCISES_ON_WORKOUT_DAY FOREIGN KEY (workout_day_id) REFERENCES workout_days (id);

ALTER TABLE workout_logs
    ADD CONSTRAINT FK_WORKOUT_LOGS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES client_profiles (id);

ALTER TABLE workout_logs
    ADD CONSTRAINT FK_WORKOUT_LOGS_ON_WORKOUT_ASSIGNMENT FOREIGN KEY (workout_assignment_id) REFERENCES workout_assignments (id);

ALTER TABLE workout_logs
    ADD CONSTRAINT FK_WORKOUT_LOGS_ON_WORKOUT_DAY FOREIGN KEY (workout_day_id) REFERENCES workout_days (id);

ALTER TABLE workout_plans
    ADD CONSTRAINT FK_WORKOUT_PLANS_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES users (id);