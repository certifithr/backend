ALTER TABLE exercises
DROP
CONSTRAINT fk_exercises_on_created_by;

ALTER TABLE exercises
    ADD body_map_images JSONB;

ALTER TABLE exercises
    ADD correct_steps JSONB;

ALTER TABLE exercises
    ADD difficulty VARCHAR(255);

ALTER TABLE exercises
    ADD external_id INTEGER;

ALTER TABLE exercises
    ADD force VARCHAR(255);

ALTER TABLE exercises
    ADD mechanic VARCHAR(255);

ALTER TABLE exercises
    ADD media JSONB;

ALTER TABLE exercises
    ADD muscles_primary JSONB;

ALTER TABLE exercises
    ADD muscles_secondary JSONB;

ALTER TABLE exercises
    ADD slug VARCHAR(255);

ALTER TABLE exercises
    ADD variation_of JSONB;

ALTER TABLE exercises
    ADD variations JSONB;

ALTER TABLE exercises
DROP
COLUMN created_by;

ALTER TABLE exercises
DROP
COLUMN deleted_at;

ALTER TABLE exercises
DROP
COLUMN equipment;

ALTER TABLE exercises
DROP
COLUMN is_public;

ALTER TABLE exercises
DROP
COLUMN muscle_group;

ALTER TABLE exercises
DROP
COLUMN video_url;

ALTER TABLE exercises
DROP
COLUMN category;

ALTER TABLE exercises
    ADD category VARCHAR(255);

ALTER TABLE exercises
    ALTER COLUMN category DROP NOT NULL;

ALTER TABLE exercises
    ALTER COLUMN name DROP NOT NULL;