# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

### Start the database
```bash
docker-compose up -d
```
Spins up PostgreSQL 16 on port 5433 (volume: `postgres_data`).

### Build
```bash
mvn clean package
```

### Run
```bash
mvn -pl presentation spring-boot:run
```
App runs on port 8080. Swagger UI: `http://localhost:8080/swagger-ui.html`.

### Run a single test
```bash
mvn -pl <module> test -Dtest=ClassName#methodName
```

## Environment Variables

The app uses `dotenv-java` to load a `.env` file from the project root at startup.

| Variable | Description |
|---|---|
| `DB_URL` | PostgreSQL URL, e.g. `postgres://localhost:5433/certifit` |
| `DB_USERNAME` | Database user |
| `DB_PASSWORD` | Database password |
| `JWT_SECRET` | Any random string used to sign JWTs |
| `R2_ACCOUNT_ID` | Cloudflare R2 account ID |
| `R2_ACCESS_KEY` | Cloudflare R2 access key |
| `R2_SECRET_KEY` | Cloudflare R2 secret key |
| `R2_BUCKET` | Cloudflare R2 bucket name |
| `R2_PUBLIC_URL` | Public CDN base URL for R2 files |

## Module Architecture

This is a 3-module Maven project with a strict dependency direction:

```
presentation  →  application  →  db
(REST/HTTP)     (business logic)  (JPA entities + repositories)
```

- **`db`** — JPA entities, Spring Data repositories, Flyway migrations. 20 entities, 13 PostgreSQL enum types, UUID PKs, soft-delete via `deleted_at`.
- **`application`** — Domain service classes, security config (JWT filter, CORS), file storage (Cloudflare R2 via AWS S3 SDK).
- **`presentation`** — REST controllers, request/response DTOs, OpenAPI config, global exception handler. Entry point: `CertifitApplication`.

## Domain Organization

Both `application` and `presentation` are organized into the same domain packages:

`auth` · `exercise` · `exerciseform` · `files` · `messaging` · `nutrition` · `progress` · `trainer` · `trainerclient` · `user` · `workout` · `scraper`

## Security

- Stateless JWT authentication. Token extracted from `Authorization: Bearer <token>` header in `JwtAuthenticationFilter`.
- Public endpoints (no token required): `/api/auth/**`, `/v3/api-docs/**`, `/swagger-ui/**`.
- Role-based access controlled at controller level via `@PreAuthorize`.
- CORS allows all localhost ports plus a configured production domain.

## Database Migrations

Flyway migration files live in `db/src/main/resources/db/migration/`. Current versions: V1–V6. V4 contains the main schema. JSONB columns are used on `ExerciseEntity` for muscle groups, steps, and media.

## Conventions

- Controllers delegate to service classes with no business logic in the controller.
- Mapper components (e.g., `NutritionMapper`, `ProgressMapper`, `ExerciseFormMapper`) convert entities to response DTOs.
- All controllers carry `@SecurityRequirement(name = "bearerAuth")` and OpenAPI `@Tag`/`@Operation` annotations.
- File uploads go to Cloudflare R2 via presigned URLs; `StoredFileEntity` records the metadata.
