# CertiFit — Entity & Relationship Model

> PostgreSQL · Spring Data JPA · Last updated April 2026

---

## Table of Contents

1. [Domain Overview](#domain-overview)
2. [Users & Profiles](#1-users--profiles)
3. [Workout Domain](#2-workout-domain)
4. [Nutrition Domain](#3-nutrition-domain)
5. [Messaging](#4-messaging)
6. [Progress Tracking](#5-progress-tracking)
7. [Exercise Form Review](#6-exercise-form-review)
8. [Relationships Summary](#relationships-summary)
9. [Enum Reference](#enum-reference)
10. [Key Constraints & Indexes](#key-constraints--indexes)

---

## Domain Overview

```
Users & Profiles
├── USER
├── TRAINER_PROFILE
└── CLIENT_PROFILE

Workout Domain
├── EXERCISE
├── WORKOUT_PLAN
├── WORKOUT_DAY
├── WORKOUT_EXERCISE
├── WORKOUT_ASSIGNMENT
├── WORKOUT_LOG
└── EXERCISE_LOG

Nutrition Domain
├── NUTRITION_PLAN
├── MEAL
├── FOOD_ITEM
├── NUTRITION_ASSIGNMENT
└── NUTRITION_LOG

Messaging
└── MESSAGE

Progress Tracking
├── PROGRESS_CHECKIN
├── BODY_MEASUREMENT
└── PROGRESS_PHOTO

Exercise Form Review
├── EXERCISE_THREAD
├── EXERCISE_THREAD_MESSAGE
└── EXERCISE_THREAD_ATTACHMENT
```

---

## 1. Users & Profiles

### USER
- **id** UUID PK
- **first_name** VARCHAR(100) NOT NULL
- **last_name** VARCHAR(100) NOT NULL
- **email** VARCHAR(255) NOT NULL UNIQUE
- **password_hash** VARCHAR(255) NOT NULL
- **role** user_role NOT NULL (TRAINER, CLIENT)
- **phone** VARCHAR(30) NULLABLE
- **avatar_url** TEXT NULLABLE
- **email_verified** BOOLEAN NOT NULL default false
- **deleted_at** TIMESTAMPTZ NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()
- **updated_at** TIMESTAMPTZ NOT NULL default now()

### TRAINER_PROFILE
- **id** UUID PK
- **user_id** UUID FK UNIQUE NOT NULL → USER.id
- **bio** TEXT NULLABLE
- **specialization** VARCHAR(255) NULLABLE
- **timezone** VARCHAR(100) NULLABLE
- **is_verified** BOOLEAN NOT NULL default false
- **verified_at** TIMESTAMPTZ NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### CLIENT_PROFILE
- **id** UUID PK
- **user_id** UUID FK UNIQUE NOT NULL → USER.id
- **trainer_id** UUID FK NOT NULL → TRAINER_PROFILE.id
- **date_of_birth** DATE NULLABLE
- **gender** gender_type NULLABLE (MALE, FEMALE, OTHER)
- **height_cm** FLOAT NULLABLE
- **goal** TEXT NULLABLE
- **status** client_status NOT NULL default ACTIVE (ACTIVE, PAUSED, ARCHIVED)
- **onboarded_at** TIMESTAMPTZ NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()
- **updated_at** TIMESTAMPTZ NOT NULL default now()

---

## 2. Workout Domain

### EXERCISE
- **id** UUID PK
- **external_id** INTEGER NULLABLE
- **name** VARCHAR(255) NOT NULL
- **slug** VARCHAR(255) NULLABLE
- **category** VARCHAR(255) NULLABLE
- **difficulty** VARCHAR(255) NULLABLE
- **force** VARCHAR(255) NULLABLE
- **mechanic** VARCHAR(255) NULLABLE
- **description** TEXT NULLABLE
- **muscles_primary** JSONB NULLABLE
- **muscles_secondary** JSONB NULLABLE
- **correct_steps** JSONB NULLABLE
- **media** JSONB NULLABLE
- **body_map_images** JSONB NULLABLE
- **variation_of** JSONB NULLABLE
- **variations** JSONB NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### WORKOUT_PLAN
- **id** UUID PK
- **trainer_id** UUID FK NOT NULL → USER.id
- **title** VARCHAR(255) NOT NULL
- **description** TEXT NULLABLE
- **difficulty** difficulty_level NULLABLE (BEGINNER, INTERMEDIATE, ADVANCED)
- **duration_weeks** INTEGER NULLABLE
- **is_template** BOOLEAN NOT NULL default false
- **deleted_at** TIMESTAMPTZ NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### WORKOUT_DAY
- **id** UUID PK
- **workout_plan_id** UUID FK NOT NULL → WORKOUT_PLAN.id
- **day_number** INTEGER NOT NULL
- **label** VARCHAR(100) NULLABLE

### WORKOUT_EXERCISE
- **id** UUID PK
- **workout_day_id** UUID FK NOT NULL → WORKOUT_DAY.id
- **exercise_id** UUID FK NOT NULL → EXERCISE.id
- **order_index** INTEGER NOT NULL
- **sets** INTEGER NULLABLE
- **reps** INTEGER NULLABLE
- **rest_seconds** INTEGER NULLABLE
- **weight_kg** FLOAT NULLABLE
- **duration_seconds** INTEGER NULLABLE
- **notes** TEXT NULLABLE

### WORKOUT_ASSIGNMENT
- **id** UUID PK
- **workout_plan_id** UUID FK NOT NULL → WORKOUT_PLAN.id
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **start_date** DATE NOT NULL
- **end_date** DATE NULLABLE
- **status** assignment_status NOT NULL default ACTIVE (ACTIVE, COMPLETED, CANCELLED)
- **assigned_at** TIMESTAMPTZ NOT NULL default now()

### WORKOUT_LOG
- **id** UUID PK
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **workout_assignment_id** UUID FK NULLABLE → WORKOUT_ASSIGNMENT.id
- **workout_day_id** UUID FK NOT NULL → WORKOUT_DAY.id
- **logged_date** DATE NOT NULL
- **notes** TEXT NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### EXERCISE_LOG
- **id** UUID PK
- **workout_log_id** UUID FK NOT NULL → WORKOUT_LOG.id
- **workout_exercise_id** UUID FK NOT NULL → WORKOUT_EXERCISE.id
- **set_number** INTEGER NOT NULL
- **reps_done** INTEGER NULLABLE
- **weight_used** FLOAT NULLABLE
- **duration_seconds** INTEGER NULLABLE
- **completed** BOOLEAN NOT NULL default true

---

## 3. Nutrition Domain

### NUTRITION_PLAN
- **id** UUID PK
- **trainer_id** UUID FK NOT NULL → USER.id
- **title** VARCHAR(255) NOT NULL
- **description** TEXT NULLABLE
- **target_calories** INTEGER NULLABLE
- **target_protein_g** FLOAT NULLABLE
- **target_carbs_g** FLOAT NULLABLE
- **target_fat_g** FLOAT NULLABLE
- **is_template** BOOLEAN NOT NULL default false
- **deleted_at** TIMESTAMPTZ NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### MEAL
- **id** UUID PK
- **nutrition_plan_id** UUID FK NOT NULL → NUTRITION_PLAN.id
- **name** VARCHAR(100) NOT NULL
- **meal_type** meal_type NULLABLE (BREAKFAST, LUNCH, DINNER, SNACK, PRE_WORKOUT, POST_WORKOUT)
- **order_index** INTEGER NOT NULL

### FOOD_ITEM
- **id** UUID PK
- **meal_id** UUID FK NOT NULL → MEAL.id
- **name** VARCHAR(255) NOT NULL
- **quantity** FLOAT NOT NULL
- **unit** VARCHAR(30) NOT NULL
- **calories** INTEGER NULLABLE
- **protein_g** FLOAT NULLABLE
- **carbs_g** FLOAT NULLABLE
- **fat_g** FLOAT NULLABLE

### NUTRITION_ASSIGNMENT
- **id** UUID PK
- **nutrition_plan_id** UUID FK NOT NULL → NUTRITION_PLAN.id
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **start_date** DATE NOT NULL
- **end_date** DATE NULLABLE
- **status** assignment_status NOT NULL default ACTIVE (ACTIVE, COMPLETED, CANCELLED)
- **assigned_at** TIMESTAMPTZ NOT NULL default now()

### NUTRITION_LOG
- **id** UUID PK
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **nutrition_assignment_id** UUID FK NULLABLE → NUTRITION_ASSIGNMENT.id
- **logged_date** DATE NOT NULL
- **calories_consumed** INTEGER NULLABLE
- **protein_g** FLOAT NULLABLE
- **carbs_g** FLOAT NULLABLE
- **fat_g** FLOAT NULLABLE
- **notes** TEXT NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

---

## 4. Messaging

### MESSAGE
- **id** UUID PK
- **sender_id** UUID FK NOT NULL → USER.id
- **receiver_id** UUID FK NOT NULL → USER.id
- **body** TEXT NOT NULL
- **is_read** BOOLEAN NOT NULL default false
- **sent_at** TIMESTAMPTZ NOT NULL default now()

---

## 5. Progress Tracking

### PROGRESS_CHECKIN
- **id** UUID PK
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **checkin_date** DATE NOT NULL
- **trainer_note** TEXT NULLABLE
- **client_note** TEXT NULLABLE
- **created_at** TIMESTAMPTZ NOT NULL default now()

### BODY_MEASUREMENT
- **id** UUID PK
- **checkin_id** UUID FK NOT NULL → PROGRESS_CHECKIN.id
- **weight_kg** FLOAT NULLABLE
- **body_fat_pct** FLOAT NULLABLE
- **chest_cm** FLOAT NULLABLE
- **waist_cm** FLOAT NULLABLE
- **hips_cm** FLOAT NULLABLE
- **thigh_cm** FLOAT NULLABLE
- **arm_cm** FLOAT NULLABLE
- **measured_at** TIMESTAMPTZ NOT NULL default now()

### PROGRESS_PHOTO
- **id** UUID PK
- **checkin_id** UUID FK NOT NULL → PROGRESS_CHECKIN.id
- **angle** photo_angle NOT NULL (FRONT, BACK, SIDE_LEFT, SIDE_RIGHT)
- **photo_url** TEXT NOT NULL
- **taken_at** TIMESTAMPTZ NOT NULL default now()

---

## 6. Exercise Form Review

### EXERCISE_THREAD
- **id** UUID PK
- **exercise_id** UUID FK NOT NULL → EXERCISE.id
- **client_id** UUID FK NOT NULL → CLIENT_PROFILE.id
- **trainer_id** UUID FK NOT NULL → TRAINER_PROFILE.id
- **status** thread_status NOT NULL default OPEN (OPEN, AWAITING_TRAINER, AWAITING_CLIENT, RESOLVED)
- **created_at** TIMESTAMPTZ NOT NULL default now()
- **resolved_at** TIMESTAMPTZ NULLABLE

### EXERCISE_THREAD_MESSAGE
- **id** UUID PK
- **thread_id** UUID FK NOT NULL → EXERCISE_THREAD.id
- **sender_id** UUID FK NOT NULL → USER.id
- **body** TEXT NULLABLE
- **is_read** BOOLEAN NOT NULL default false
- **sent_at** TIMESTAMPTZ NOT NULL default now()

### EXERCISE_THREAD_ATTACHMENT
- **id** UUID PK
- **message_id** UUID FK NOT NULL → EXERCISE_THREAD_MESSAGE.id
- **type** attachment_type NOT NULL (VIDEO, IMAGE, AUDIO)
- **file_url** TEXT NOT NULL
- **thumbnail_url** TEXT NULLABLE
- **duration_seconds** INTEGER NULLABLE
- **file_size_bytes** BIGINT NULLABLE
- **mime_type** VARCHAR(100) NULLABLE
- **uploaded_at** TIMESTAMPTZ NOT NULL default now()

---

## Relationships Summary

```
USER (1) ──── (1) TRAINER_PROFILE
USER (1) ──── (1) CLIENT_PROFILE
TRAINER_PROFILE (1) ──── (N) CLIENT_PROFILE
USER (1) ──── (N) MESSAGE (as sender)
USER (1) ──── (N) MESSAGE (as receiver)
USER (1) ──── (N) WORKOUT_PLAN
USER (1) ──── (N) NUTRITION_PLAN
CLIENT_PROFILE (1) ──── (N) WORKOUT_ASSIGNMENT
CLIENT_PROFILE (1) ──── (N) NUTRITION_ASSIGNMENT
CLIENT_PROFILE (1) ──── (N) WORKOUT_LOG
CLIENT_PROFILE (1) ──── (N) NUTRITION_LOG
CLIENT_PROFILE (1) ──── (N) PROGRESS_CHECKIN
CLIENT_PROFILE (1) ──── (N) EXERCISE_THREAD
TRAINER_PROFILE (1) ──── (N) EXERCISE_THREAD
WORKOUT_PLAN (1) ──── (N) WORKOUT_DAY
WORKOUT_PLAN (1) ──── (N) WORKOUT_ASSIGNMENT
WORKOUT_DAY (1) ──── (N) WORKOUT_EXERCISE
WORKOUT_EXERCISE (1) ──── (N) EXERCISE_LOG
WORKOUT_ASSIGNMENT (1) ──── (N) WORKOUT_LOG
WORKOUT_LOG (1) ──── (N) EXERCISE_LOG
NUTRITION_PLAN (1) ──── (N) MEAL
NUTRITION_PLAN (1) ──── (N) NUTRITION_ASSIGNMENT
MEAL (1) ──── (N) FOOD_ITEM
NUTRITION_ASSIGNMENT (1) ──── (N) NUTRITION_LOG
PROGRESS_CHECKIN (1) ──── (1) BODY_MEASUREMENT
PROGRESS_CHECKIN (1) ──── (N) PROGRESS_PHOTO
EXERCISE_THREAD (1) ──── (N) EXERCISE_THREAD_MESSAGE
EXERCISE_THREAD_MESSAGE (1) ──── (N) EXERCISE_THREAD_ATTACHMENT
```

---

## Enum Reference

### user_role
- TRAINER
- CLIENT

### gender_type
- MALE
- FEMALE
- OTHER

### client_status
- ACTIVE
- PAUSED
- ARCHIVED

### assignment_status
- ACTIVE
- COMPLETED
- CANCELLED

### difficulty_level
- BEGINNER
- INTERMEDIATE
- ADVANCED

### meal_type
- BREAKFAST
- LUNCH
- DINNER
- SNACK
- PRE_WORKOUT
- POST_WORKOUT

### photo_angle
- FRONT
- BACK
- SIDE_LEFT
- SIDE_RIGHT

### thread_status
- OPEN
- AWAITING_TRAINER
- AWAITING_CLIENT
- RESOLVED

### attachment_type
- VIDEO
- IMAGE
- AUDIO

---

## Key Constraints & Indexes

- **Primary Keys**: All entities use UUID primary keys
- **Foreign Keys**: All relationships use UUID foreign keys with proper cascading
- **Unique Constraints**: USER.email, TRAINER_PROFILE.user_id, CLIENT_PROFILE.user_id
- **Indexes**: Recommended on frequently queried fields (e.g., user_id, trainer_id, client_id, dates)
- **Soft Deletes**: WORKOUT_PLAN.deleted_at, NUTRITION_PLAN.deleted_at
- **Audit Fields**: created_at, updated_at on relevant entities
- **JSON Fields**: Used for flexible data storage (muscles, media, etc.)
