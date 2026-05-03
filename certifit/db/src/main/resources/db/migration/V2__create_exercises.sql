CREATE TYPE exercise_category AS ENUM ('STRENGTH', 'CARDIO', 'MOBILITY', 'FLEXIBILITY', 'SPORT');
CREATE TYPE equipment_type    AS ENUM ('BARBELL', 'DUMBBELL', 'CABLE', 'MACHINE', 'BODYWEIGHT', 'KETTLEBELL', 'BAND', 'OTHER');
CREATE TYPE muscle_group      AS ENUM ('CHEST', 'BACK', 'SHOULDERS', 'BICEPS', 'TRICEPS', 'CORE', 'GLUTES', 'QUADS', 'HAMSTRINGS', 'CALVES', 'FULL_BODY');

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