CREATE TABLE exercises
(
    id                INTEGER NOT NULL,
    name              VARCHAR(255),
    slug              VARCHAR(255),
    category          VARCHAR(255),
    difficulty        VARCHAR(255),
    force             VARCHAR(255),
    mechanic          VARCHAR(255),
    description       TEXT,
    muscles_primary   JSONB,
    muscles_secondary JSONB,
    correct_steps     JSONB,
    media             JSONB,
    body_map_images   JSONB,
    variation_of      JSONB,
    variations        JSONB,
    CONSTRAINT pk_exercises PRIMARY KEY (id)
);