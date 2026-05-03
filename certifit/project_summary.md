# CertiFit Backend — Project Summary

## Overview

CertiFit is a personal fitness coaching platform backend. It connects trainers with clients and supports workout planning, nutrition tracking, progress monitoring, and exercise form review. Built with **Spring Boot 4 / Java 21**, deployed as a **multi-module Maven** project backed by **PostgreSQL 16**.

---

## Architecture

Three-module layered architecture:

```
presentation  →  application  →  db
  (REST API)     (Services)    (JPA + Repos)
```

- **presentation** — REST controllers, exception handling, security filters, Swagger docs
- **application** — Business logic, commands, service classes, JWT auth
- **db** — JPA entities, Spring Data repositories, Flyway migrations

---

## Tech Stack

| Concern | Technology |
|---|---|
| Framework | Spring Boot 4.0.5 |
| Language | Java 21 |
| Build | Maven (multi-module) |
| Database | PostgreSQL 16 (Docker, port 5433) |
| ORM | Spring Data JPA / Hibernate |
| Migrations | Flyway (V1–V5) |
| Auth | JWT (JJWT 0.12.6) + Spring Security |
| API Docs | Springdoc OpenAPI 3.0.2 |
| Utilities | Lombok, dotenv-java |

---

## Domain Modules

### Auth & Users
- JWT-based login/signup with BCrypt hashing
- Two roles: `TRAINER` and `CLIENT`
- Separate profile entities per role (`TrainerProfileEntity`, `ClientProfileEntity`)

### Trainer–Client Relation
- Request-based client onboarding flow
- Client assignment and status tracking (`ClientStatus` enum)

### Workout Management
- Exercise library (scraped + manually managed, JSONB metadata)
- Workout plans with days and exercises (sets, reps, rest, weight)
- Plan assignment to clients with status tracking
- Client workout logging (per-session and per-set)

### Nutrition
- Macro-based nutrition plans (calories, protein, carbs, fat)
- Six meal types (breakfast, lunch, dinner, snacks, pre/post-workout)
- Food item database
- Client nutrition logging

### Progress Tracking
- Body measurements (weight, body fat %, chest, waist, hips, thigh, arm)
- Progress photos (front, back, side, other angles)
- Check-in events

### Exercise Form Review
- Threaded discussion system between trainer and client
- Thread statuses: `OPEN → AWAITING_TRAINER / AWAITING_CLIENT → RESOLVED`
- Attachment support (video, image, audio)

### Messaging
- Direct messaging between users (`MessageEntity`)

---

## Database Schema

- 35 JPA entities, 13 custom PostgreSQL enum types
- UUID primary keys, `TIMESTAMPTZ` audit columns (`created_at`, `updated_at`, `deleted_at`)
- Soft deletes via `deleted_at`
- JSONB columns on `ExerciseEntity` for flexible media/muscle metadata
- Flyway manages all DDL (Hibernate DDL set to `none`)

---

## Key Files

| Path | Purpose |
|---|---|
| `presentation/src/.../CertifitApplication.java` | App entry point |
| `presentation/src/resources/application.yml` | Server config, DB, Swagger |
| `docker-compose.yml` | PostgreSQL container |
| `db/src/main/java/.../entity/` | All JPA entities |
| `db/src/main/resources/db/migration/` | Flyway SQL migrations (V1–V5) |
| `application/src/.../auth/` | JWT service, sign-in/up commands |
| `application/src/.../config/SecurityConfig.java` | Spring Security setup |
