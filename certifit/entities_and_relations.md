# CertiFit — Entities & Relations

All primary keys are UUID. All timestamps are `TIMESTAMPTZ`. Soft-delete columns (`deleted_at`) exist on `UserEntity`, `WorkoutPlanEntity`, and `NutritionPlanEntity`.

---

## Entity Map (high-level)

```
UserEntity ──────────────────────────────────────────────────────────────────┐
  │  @OneToOne                                                                │
  ├──► TrainerProfileEntity ──@OneToMany──► ClientProfileEntity               │
  │                                    (many clients per trainer)             │
  │                                          │ @OneToOne                     │
  │                                          └──► UserEntity (client user)   │
  │                                                                           │
  ├──► WorkoutPlanEntity (trainer_id FK → UserEntity)                        │
  │         └──► WorkoutDayEntity                                             │
  │                   └──► WorkoutExerciseEntity ──► ExerciseEntity           │
  │                                                                           │
  ├──► NutritionPlanEntity (trainer_id FK → UserEntity)                      │
  │         └──► MealEntity                                                   │
  │                   └──► FoodItemEntity                                     │
  │                                                                           │
  └──► MessageEntity (sender / receiver FK → UserEntity)                     │
                                                                              │
ClientProfileEntity                                                           │
  ├──► WorkoutAssignmentEntity ──► WorkoutPlanEntity                          │
  │         └──► WorkoutLogEntity ──► ExerciseLogEntity                      │
  ├──► NutritionAssignmentEntity ──► NutritionPlanEntity                     │
  │         └──► NutritionLogEntity                                           │
  ├──► ProgressCheckinEntity                                                  │
  │         ├──► BodyMeasurementEntity                                       │
  │         └──► ProgressPhotoEntity                                         │
  └──► ExerciseThreadEntity ──► ExerciseThreadMessageEntity                  │
                                      └──► ExerciseThreadAttachmentEntity    │
```

---

## Domain 1 — Users & Profiles

### UserEntity → `users`

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| firstName | String(100) | not null |
| lastName | String(100) | not null |
| email | String(255) | unique, not null |
| passwordHash | String(255) | BCrypt |
| role | `UserRole` | `TRAINER` or `CLIENT` |
| phone | String(30) | nullable |
| avatarUrl | TEXT | nullable |
| emailVerified | boolean | |
| deletedAt | TIMESTAMPTZ | soft delete |
| createdAt | TIMESTAMPTZ | immutable |
| updatedAt | TIMESTAMPTZ | |

---

### TrainerProfileEntity → `trainer_profiles`

Extended profile for users with role `TRAINER`.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| user | → `UserEntity` | @OneToOne, unique FK |
| bio | TEXT | nullable |
| specialization | String(255) | nullable |
| timezone | String(100) | nullable |
| isVerified | boolean | |
| verifiedAt | TIMESTAMPTZ | nullable |
| createdAt | TIMESTAMPTZ | immutable |

**Relation:** `UserEntity` **1 — 1** `TrainerProfileEntity`

---

### ClientProfileEntity → `client_profiles`

Extended profile for users with role `CLIENT`, always linked to a trainer.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| user | → `UserEntity` | @OneToOne, unique FK |
| trainer | → `TrainerProfileEntity` | @ManyToOne |
| dateOfBirth | LocalDate | nullable |
| gender | `GenderType` | `MALE / FEMALE / OTHER` |
| heightCm | Float | nullable |
| goal | TEXT | nullable |
| status | `ClientStatus` | `ACTIVE / PAUSED / ARCHIVED` |
| onboardedAt | TIMESTAMPTZ | nullable |
| createdAt | TIMESTAMPTZ | immutable |
| updatedAt | TIMESTAMPTZ | |

**Relations:**
- `UserEntity` **1 — 1** `ClientProfileEntity`
- `TrainerProfileEntity` **1 — N** `ClientProfileEntity` (one trainer, many clients)

---

## Domain 2 — Workout

### ExerciseEntity → `exercises`

Shared exercise library, populated via scraper.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| externalId | Integer | scraper source ID |
| name | String | |
| slug | String | |
| category | String | |
| difficulty | String | |
| force | String | push / pull / static |
| mechanic | String | compound / isolation |
| description | TEXT | |
| musclesPrimary | JSONB (`List<String>`) | |
| musclesSecondary | JSONB (`List<String>`) | |
| correctSteps | JSONB (`List<String>`) | |
| media | JSONB | images/video links |
| bodyMapImages | JSONB | |
| variationOf | JSONB | parent exercise ref |
| variations | JSONB | child exercise refs |
| createdAt | TIMESTAMPTZ | immutable |

---

### WorkoutPlanEntity → `workout_plans`

Trainer-created plan (can be a reusable template).

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| trainer | → `UserEntity` | @ManyToOne (trainer FK) |
| title | String(255) | not null |
| description | TEXT | nullable |
| difficulty | `DifficultyLevel` | `BEGINNER / INTERMEDIATE / ADVANCED` |
| durationWeeks | Integer | nullable |
| isTemplate | boolean | |
| deletedAt | TIMESTAMPTZ | soft delete |
| createdAt | TIMESTAMPTZ | immutable |

---

### WorkoutDayEntity → `workout_days`

A single training day within a plan.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| workoutPlan | → `WorkoutPlanEntity` | @ManyToOne |
| dayNumber | Integer | not null |
| label | String(100) | e.g. "Push Day" |

**Relation:** `WorkoutPlanEntity` **1 — N** `WorkoutDayEntity`

---

### WorkoutExerciseEntity → `workout_exercises`

An exercise slot inside a workout day, with prescribed load.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| workoutDay | → `WorkoutDayEntity` | @ManyToOne |
| exercise | → `ExerciseEntity` | @ManyToOne |
| orderIndex | Integer | display order |
| sets | Integer | |
| reps | Integer | |
| restSeconds | Integer | |
| weightKg | Float | |
| durationSeconds | Integer | time-based exercises |
| notes | TEXT | |

**Relations:**
- `WorkoutDayEntity` **1 — N** `WorkoutExerciseEntity`
- `ExerciseEntity` **1 — N** `WorkoutExerciseEntity`

---

### WorkoutAssignmentEntity → `workout_assignments`

Links a `WorkoutPlan` to a specific `ClientProfile`.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| workoutPlan | → `WorkoutPlanEntity` | @ManyToOne |
| client | → `ClientProfileEntity` | @ManyToOne |
| startDate | LocalDate | not null |
| endDate | LocalDate | nullable |
| status | `AssignmentStatus` | `ACTIVE / COMPLETED / CANCELLED` |
| assignedAt | TIMESTAMPTZ | immutable |

**Relations:**
- `WorkoutPlanEntity` **1 — N** `WorkoutAssignmentEntity`
- `ClientProfileEntity` **1 — N** `WorkoutAssignmentEntity`

---

### WorkoutLogEntity → `workout_logs`

Client's record of completing a workout day session.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| client | → `ClientProfileEntity` | @ManyToOne |
| workoutAssignment | → `WorkoutAssignmentEntity` | @ManyToOne (nullable — freeform log possible) |
| workoutDay | → `WorkoutDayEntity` | @ManyToOne |
| loggedDate | LocalDate | not null |
| notes | TEXT | |
| createdAt | TIMESTAMPTZ | immutable |

**Relations:**
- `ClientProfileEntity` **1 — N** `WorkoutLogEntity`
- `WorkoutAssignmentEntity` **1 — N** `WorkoutLogEntity`
- `WorkoutDayEntity` **1 — N** `WorkoutLogEntity`

---

### ExerciseLogEntity → `exercise_logs`

Set-by-set performance record inside a workout session.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| workoutLog | → `WorkoutLogEntity` | @ManyToOne |
| workoutExercise | → `WorkoutExerciseEntity` | @ManyToOne |
| setNumber | Integer | not null |
| repsDone | Integer | |
| weightUsed | Float | |
| durationSeconds | Integer | |
| completed | boolean | not null |

**Relations:**
- `WorkoutLogEntity` **1 — N** `ExerciseLogEntity`
- `WorkoutExerciseEntity` **1 — N** `ExerciseLogEntity`

---

## Domain 3 — Nutrition

### NutritionPlanEntity → `nutrition_plans`

Trainer-created macro-based plan (can be a reusable template).

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| trainer | → `UserEntity` | @ManyToOne (trainer FK) |
| title | String(255) | not null |
| description | TEXT | nullable |
| targetCalories | Integer | |
| targetProteinG | Float | |
| targetCarbsG | Float | |
| targetFatG | Float | |
| isTemplate | boolean | |
| deletedAt | TIMESTAMPTZ | soft delete |
| createdAt | TIMESTAMPTZ | immutable |

---

### MealEntity → `meals`

A named meal slot within a nutrition plan.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| nutritionPlan | → `NutritionPlanEntity` | @ManyToOne |
| name | String(100) | not null |
| mealType | `MealType` | `BREAKFAST / LUNCH / DINNER / SNACK / PRE_WORKOUT / POST_WORKOUT` |
| orderIndex | Integer | display order |

**Relation:** `NutritionPlanEntity` **1 — N** `MealEntity`

---

### FoodItemEntity → `food_items`

Individual food entry within a meal.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| meal | → `MealEntity` | @ManyToOne |
| name | String(255) | not null |
| quantity | Float | not null |
| unit | String(30) | e.g. g, ml, piece |
| calories | Integer | |
| proteinG | Float | |
| carbsG | Float | |
| fatG | Float | |

**Relation:** `MealEntity` **1 — N** `FoodItemEntity`

---

### NutritionAssignmentEntity → `nutrition_assignments`

Links a `NutritionPlan` to a specific `ClientProfile`.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| nutritionPlan | → `NutritionPlanEntity` | @ManyToOne |
| client | → `ClientProfileEntity` | @ManyToOne |
| startDate | LocalDate | not null |
| endDate | LocalDate | nullable |
| status | `AssignmentStatus` | `ACTIVE / COMPLETED / CANCELLED` |
| assignedAt | TIMESTAMPTZ | immutable |

**Relations:**
- `NutritionPlanEntity` **1 — N** `NutritionAssignmentEntity`
- `ClientProfileEntity` **1 — N** `NutritionAssignmentEntity`

---

### NutritionLogEntity → `nutrition_logs`

Client's daily nutrition intake record.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| client | → `ClientProfileEntity` | @ManyToOne |
| nutritionAssignment | → `NutritionAssignmentEntity` | @ManyToOne (nullable) |
| loggedDate | LocalDate | not null |
| caloriesConsumed | Integer | |
| proteinG | Float | |
| carbsG | Float | |
| fatG | Float | |
| notes | TEXT | |
| createdAt | TIMESTAMPTZ | immutable |

**Relations:**
- `ClientProfileEntity` **1 — N** `NutritionLogEntity`
- `NutritionAssignmentEntity` **1 — N** `NutritionLogEntity`

---

## Domain 4 — Progress Tracking

### ProgressCheckinEntity → `progress_checkins`

A check-in event grouping measurements and photos on a given date.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| client | → `ClientProfileEntity` | @ManyToOne |
| checkinDate | LocalDate | not null |
| trainerNote | TEXT | nullable |
| clientNote | TEXT | nullable |
| createdAt | TIMESTAMPTZ | immutable |

**Relation:** `ClientProfileEntity` **1 — N** `ProgressCheckinEntity`

---

### BodyMeasurementEntity → `body_measurements`

Body metrics captured at a check-in.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| checkin | → `ProgressCheckinEntity` | @ManyToOne |
| weightKg | Float | |
| bodyFatPct | Float | |
| chestCm | Float | |
| waistCm | Float | |
| hipsCm | Float | |
| thighCm | Float | |
| armCm | Float | |
| measuredAt | TIMESTAMPTZ | immutable |

**Relation:** `ProgressCheckinEntity` **1 — N** `BodyMeasurementEntity`

---

### ProgressPhotoEntity → `progress_photos`

Photo taken at a check-in from a specific angle.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| checkin | → `ProgressCheckinEntity` | @ManyToOne |
| angle | `PhotoAngle` | `FRONT / BACK / SIDE_LEFT / SIDE_RIGHT` |
| photoUrl | TEXT | not null |
| takenAt | TIMESTAMPTZ | immutable |

**Relation:** `ProgressCheckinEntity` **1 — N** `ProgressPhotoEntity`

---

## Domain 5 — Exercise Form Review

### ExerciseThreadEntity → `exercise_threads`

A review thread between trainer and client about an exercise's form.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| exercise | → `ExerciseEntity` | @ManyToOne |
| client | → `ClientProfileEntity` | @ManyToOne |
| trainer | → `TrainerProfileEntity` | @ManyToOne |
| status | `ThreadStatus` | `OPEN / AWAITING_TRAINER / AWAITING_CLIENT / RESOLVED` |
| createdAt | TIMESTAMPTZ | immutable |
| resolvedAt | TIMESTAMPTZ | nullable |

**Relations:**
- `ExerciseEntity` **1 — N** `ExerciseThreadEntity`
- `ClientProfileEntity` **1 — N** `ExerciseThreadEntity`
- `TrainerProfileEntity` **1 — N** `ExerciseThreadEntity`

---

### ExerciseThreadMessageEntity → `exercise_thread_messages`

A single message inside a form review thread.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| thread | → `ExerciseThreadEntity` | @ManyToOne |
| sender | → `UserEntity` | @ManyToOne |
| body | TEXT | |
| isRead | boolean | not null |
| sentAt | TIMESTAMPTZ | immutable |

**Relation:** `ExerciseThreadEntity` **1 — N** `ExerciseThreadMessageEntity`

---

### ExerciseThreadAttachmentEntity → `exercise_thread_attachments`

File attached to a thread message (video, image, or audio for form review).

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| message | → `ExerciseThreadMessageEntity` | @ManyToOne |
| type | `AttachmentType` | `VIDEO / IMAGE / AUDIO` |
| fileUrl | TEXT | not null |
| thumbnailUrl | TEXT | nullable |
| durationSeconds | Integer | for video/audio |
| fileSizeBytes | Long | |
| mimeType | String(100) | |
| uploadedAt | TIMESTAMPTZ | immutable |

**Relation:** `ExerciseThreadMessageEntity` **1 — N** `ExerciseThreadAttachmentEntity`

---

## Domain 6 — Messaging

### MessageEntity → `messages`

Direct message between any two users.

| Field | Type | Notes |
|---|---|---|
| id | UUID | PK |
| sender | → `UserEntity` | @ManyToOne |
| receiver | → `UserEntity` | @ManyToOne |
| body | TEXT | not null |
| isRead | boolean | not null |
| sentAt | TIMESTAMPTZ | immutable |

**Relation:** `UserEntity` **1 — N** `MessageEntity` (twice: as sender and as receiver)

---

## Enum Reference

| Enum | Values |
|---|---|
| `UserRole` | `TRAINER`, `CLIENT` |
| `ClientStatus` | `ACTIVE`, `PAUSED`, `ARCHIVED` |
| `GenderType` | `MALE`, `FEMALE`, `OTHER` |
| `AssignmentStatus` | `ACTIVE`, `COMPLETED`, `CANCELLED` |
| `DifficultyLevel` | `BEGINNER`, `INTERMEDIATE`, `ADVANCED` |
| `MealType` | `BREAKFAST`, `LUNCH`, `DINNER`, `SNACK`, `PRE_WORKOUT`, `POST_WORKOUT` |
| `PhotoAngle` | `FRONT`, `BACK`, `SIDE_LEFT`, `SIDE_RIGHT` |
| `ThreadStatus` | `OPEN`, `AWAITING_TRAINER`, `AWAITING_CLIENT`, `RESOLVED` |
| `AttachmentType` | `VIDEO`, `IMAGE`, `AUDIO` |
| `ExerciseCategory` | `STRENGTH`, `CARDIO`, `MOBILITY`, `FLEXIBILITY`, `SPORT` |
| `MuscleGroup` | `CHEST`, `BACK`, `SHOULDERS`, `BICEPS`, `TRICEPS`, `CORE`, `GLUTES`, `QUADS`, `HAMSTRINGS`, `CALVES`, `FULL_BODY` |
| `EquipmentType` | `BARBELL`, `DUMBBELL`, `CABLE`, `MACHINE`, `BODYWEIGHT`, `KETTLEBELL`, `BAND`, `OTHER` |
| `CollaborationType` | `PERSONAL_TRAINING`, `GROUP_TRAINING`, `ONLINE_COACHING` |
