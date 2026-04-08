# CertiFit - Kompletna Platforma za Fitness Trenere i Klijente

## 📋 Pregled Sustava

CertiFit je sveobuhvatna platforma koja povezuje certificirane fitness trenere s klijentima, omogućavajući online i offline suradnju, prodaju treningskih planova te gradnju povjerenja kroz sustav certifikata i znački.

---

## 🎯 Glavni Ciljevi Sustava

1. **Verificiranje Stručnosti** - Treneri prilažu certifikate koji im donose značke i povećavaju kredibilitet
2. **Upravljanje Klijentima** - Treneri vode evidenciju online i offline (individualni uživo rad) klijenata
3. **Prodaja Treningskih Planova** - Monetizacija kroz jednokratne planove (dnevni, tjedni, mjesečni)
4. **Online Suradnja** - Interaktivno sučelje za trenere i klijente
5. **Marketplace** - Klijenti pretražuju i kupuju planove od verificiranih trenera

---

## 👥 Korisničke Uloge

### 🏋️ TRENER (Trainer)

**Osnovne Funkcionalnosti:**
- Registracija i autentifikacija s JWT tokenima
- Kreiranje i upravljanje profilom
- Upload i verifikacija certifikata (dodjeljivanje znački)
- Pregled i upravljanje svojom kolekcijom certifikata i znački

**Upravljanje Klijentima:**
- **Online Klijenti**:
  - Dodavanje i uklanjanje klijenata u online suradnju
  - Kreiranje i dodjeljivanje personaliziranih treningskih planova
  - Praćenje napretka i komunikacija
  - Prilagodba planova i prehrane

- **Offline/Uživo Klijenti (Inventura)**:
  - Evidencija klijenata koji treniraju individualno u teretani
  - Vođenje evidencije o prisutnosti
  - Bilješke i praćenje napretka
  - Upravljanje terminima treninga

**Treningski Planovi:**
- Kreiranje jednokratnih treningskih planova za prodaju
- Definiranje trajanja plana (dnevni, tjedni, mjesečni, custom)
- Postavljanje cijena
- Kategoriranje i opisivanje planova
- Korištenje baze vježbi (bazne + vlastite dorade)
- Pregled statistike prodaje svojih planova

**Upravljanje Vježbama:**
- Pristup baznim vježbama (ExerciseEntity) koje je kreirao admin
- Kreiranje vlastitih vježbi (kopija bazne + izmjene ili potpuno nova)
- Uređivanje svojih vježbi (naziv, opis, mišićne skupine, mediji, itd.)
- Organizacija svojih vježbi po kategorijama
- Korištenje svojih i baznih vježbi u planovima
- **Razlikovanje:**
  - **Bazne vježbe** - read-only za trenere, kreirane od admina (npr. "Barbell Squat")
  - **Vlastite vježbe trenera** - trener može kreirati/uređivati samo svoje (npr. "Barbell Squat - My Version")

**Značke i Certifikati:**
- Upload skenova/fotografija certifikata
- Automatsko ili ručno (admin) verificiranje certifikata
- Dodjeljivanje znački (badges) prema tipu i razini certifikata
- Javni prikaz znački na profilu

---

### 💪 KLIJENT (Client)

**Osnovne Funkcionalnosti:**
- Registracija i autentifikacija
- Kreiranje i upravljanje profilom
- Postavljanje fitness ciljeva

**Pregled Suradnje:**
- Prikaz trenutnog trenera (ako radi individualno uživo)
- Status online suradnje (ako postoji)
- Povijest treningskih planova

**Online Suradnja s Trenerom:**
- Zahtjev za online suradnju s trenerom
- Pristup dodijeljenim personaliziranim planovima
- Evidencija završenih treninga
- Praćenje napretka (težina, mjerenja, fotografije)
- Komunikacija s trenerom (chat/poruke)

**Marketplace Funkcionalnosti:**
- Pretraživanje treningskih planova po:
  - Kategoriji (snaga, izdržljivost, mršavljenje, masa...)
  - Trajanju (dnevni, tjedni, mjesečni)
  - Cijeni
  - Treneru (s prikazom znački i certifikata)
  - Ocjeni i reviews
- Pregled detalja plana i preview vježbi
- Kupovina jednokratnih planova
- Pristup kupljenim planovima
- Ocjenjivanje i review kupljenih planova

---

### 👨‍💼 ADMIN (Administrator)

**Upravljanje Certifikatima:**
- Pregled svih podnesenih certifikata (PENDING, VERIFIED, REJECTED)
- Filtriranje certifikata po statusu i treneru
- Pregled detalja certifikata (ime, organizacija, datumi, uploaded image)
- Verifikacija certifikata:
  - Approve (PENDING -> VERIFIED) - automatski se dodjeljuju badge-evi treneru
  - Reject (PENDING -> REJECTED) - razlog odbijanja
- Povijest verifikacija (tko je i kada verificirao)

**Dashboard i Statistika:**
- Pregled ukupnog broja:
  - Korisnika (total, treneri, klijenti)
  - Aktivnih odnosa trenera-klijenta (online/offline)
  - Kreiraih i prodanih treningskih planova
  - Ukupne prodaje (revenue)
  - Registracija po danima/tjednima/mjesecima (chart)
- Top treneri (po prodaji, broju klijenata, ocjenama)
- Top planovi (najprodavaniji, najbolje ocijenjeni)
- Nedavne aktivnosti (registracije, kupovine, review-ovi)

**Upravljanje Korisnicima:**
- Lista svih korisnika (s filterima po roli, statusu, datumu registracije)
- Pregled profila korisnika (detalji, aktivnost, povijest)
- Ručno uređivanje korisničkih podataka
- Suspenzija/Ban korisnika (privremeno ili trajno)
- Brisanje korisnika (GDPR compliance)
- Reset password linkova

**Upravljanje Sadržajem:**
- Moderacija treningskih planova (uklanjanje neprikladno sadržaja)
- Moderacija review-ova (spam, uvredljivi komentari)
- Upravljanje kategorijama i oznakama

**Upravljanje Vježbama:**
- Pregled svih baznih vježbi (ExerciseEntity)
- Dodavanje novih baznih vježbi u sustav
- Uređivanje postojećih baznih vježbi
- Brisanje vježbi (soft delete)
- Import vježbi iz ZIP/JSON (već implementiran ExerciseScraperController)

**Upravljanje Značkama (Badges):**
- Kreiranje novih badge-ova
- Definiranje uvjeta za badge (koje certifikate zahtijeva, tier)
- Uređivanje postojećih badge-ova
- Upload badge ikona
- Ručno dodjeljivanje/uklanjanje badge-a treneru (override logic)

**Financijski Izvještaji:**
- Revenue po danima/tjednima/mjesecima
- Breakdown po trenerima (koliko je koji trener zaradio)
- Fees i komisije (ako platforma uzima postotak)
- Export podataka (CSV, Excel)

**Systemski Logs i Monitoring:**
- Pregled SLF4J logova (errori, warnings)
- Monitoring performansi (slow queries, API response times)
- Alerte (neuspjele transakcije, security incidents)

---

## 🏗️ Arhitektura Sustava

### Backend (Spring Boot) - **Trenutni Codebase**
Modularni Maven projekt s tri glavna modula:

#### 📦 `db` modul
- JPA entity klase i repozitoriji
- Trenutno implementirano:
  - `UserEntity` (email, password, role, ime, prezime)
  - `TrainerProfileEntity` (povezan s UserEntity)
  - `ExerciseEntity` (vježbe s detaljima, mišići, mediji, body map)
  - `UserRepository`, `TrainerProfileRepository`, `ExerciseRepository`

#### 📦 `application` modul
- Business logika i servisi
- Trenutno implementirano:
  - `AuthService` (signup, signin) ✅
  - `JwtService` (generiranje JWT tokena) ✅
  - `ExerciseService` (filtriranje i pretraga vježbi) ✅
  - `ExerciseScraperService` (import vježbi iz ZIP/JSON)
  - `ExerciseMapper` (DTO mapiranje)

#### 📦 `presentation` modul
- REST API kontroleri
- Swagger/OpenAPI dokumentacija
- Trenutno implementirano:
  - `AuthController` (POST /signup, /signin) ✅
  - `ExerciseController` (GET /exercises s filterima, GET /exercises/{id}) ✅
  - `ExerciseScraperController` (POST /scraper/exercises/upload)
  - `HealthCheckController` ✅

### Frontend (Planiran)
- **Web App**: React/Next.js (zasebni codebase)
- **Mobile App**: React Native / Flutter (zasebni codebase)

### Database
- PostgreSQL (docker-compose već konfiguriran)
- JSONB polja za fleksibilne strukture (media, body maps)

---

## 📊 Planirani Entiteti (Proširenje Database)

### ✅ Već Implementirano

```
UserEntity (id, email, passwordHash, firstName, lastName, role)
  └── role: TRAINER, CLIENT (ADMIN će biti dodan)

TrainerProfileEntity (id, userId, ...)

ExerciseEntity (id, name, category, difficulty, muscles, media, ...)
  └── trenutno bazne vježbe, treba proširiti za trainer ownership
```

### 🔨 Za Implementaciju

```
TrainerExerciseEntity (Vlastite Vježbe Trenera)
├── id (UUID)
├── trainerId (FK -> UserEntity)
├── baseExerciseId (FK -> ExerciseEntity, nullable) - ako je derivirana iz bazne
├── name (String)
├── slug (String)
├── category (String)
├── difficulty (String)
├── force (String, nullable)
├── mechanic (String, nullable)
├── description (Text)
├── musclesPrimary (JSONB - String[])
├── musclesSecondary (JSONB - String[], nullable)
├── correctSteps (JSONB - String[])
├── media (JSONB - MediaItemDto[])
├── bodyMapImages (JSONB - BodyMapImageDto[])
├── isPublic (Boolean) - može li drugi treneri vidjeti/koristiti
├── createdAt, updatedAt
└── notes (Text) - trenerove bilješke


CertificateEntity
├── id (UUID)
├── trainerId (FK -> UserEntity)
├── certificateName (String) - npr. "ISSA Personal Trainer"
├── issuingOrganization (String)
├── issueDate (LocalDate)
├── expiryDate (LocalDate, nullable)
├── certificateImageUrl (String) - putanja do uploada
├── verificationStatus (enum: PENDING, VERIFIED, REJECTED)
├── verifiedAt (LocalDateTime)
├── verifiedBy (FK -> UserEntity - admin)
└── createdAt, updatedAt

BadgeEntity
├── id (UUID)
├── name (String) - npr. "Certified Personal Trainer"
├── description (String)
├── iconUrl (String)
├── tier (enum: BRONZE, SILVER, GOLD, PLATINUM)
└── requiredCertificateTypes (JSON/String[])

TrainerBadgeEntity (Many-to-Many)
├── trainerId (FK -> UserEntity)
├── badgeId (FK -> BadgeEntity)
├── earnedAt (LocalDateTime)
└── certificateId (FK -> CertificateEntity - izvor badge-a)

WorkoutPlanEntity (Treningski Plan za Prodaju)
├── id (UUID)
├── trainerId (FK -> UserEntity)
├── title (String)
├── description (Text)
├── category (enum: STRENGTH, CARDIO, WEIGHT_LOSS, MUSCLE_GAIN, ...)
├── duration (Integer) - broj dana
├── durationType (enum: DAILY, WEEKLY, MONTHLY, CUSTOM)
├── price (BigDecimal)
├── currency (String - default "EUR")
├── isPublished (Boolean)
├── previewImageUrl (String)
├── rating (Float - calculated)
├── totalSales (Integer)
└── createdAt, updatedAt

WorkoutPlanDayEntity
├── id (UUID)
├── planId (FK -> WorkoutPlanEntity)
├── dayNumber (Integer)
├── title (String) - npr. "Day 1: Upper Body"
├── description (Text)
└── exercises (JSONB) - [{exerciseId, sets, reps, rest, notes}, ...]

PurchasedPlanEntity
├── id (UUID)
├── clientId (FK -> UserEntity)
├── planId (FK -> WorkoutPlanEntity)
├── purchasedAt (LocalDateTime)
├── price (BigDecimal) - snapshot cijene
├── accessExpiresAt (LocalDateTime, nullable)
└── isReviewed (Boolean)

PlanReviewEntity
├── id (UUID)
├── purchasedPlanId (FK -> PurchasedPlanEntity)
├── clientId (FK -> UserEntity)
├── rating (Integer 1-5)
├── comment (Text)
└── createdAt

ClientTrainerRelationship (Online Suradnja)
├── id (UUID)
├── clientId (FK -> UserEntity)
├── trainerId (FK -> UserEntity)
├── relationshipType (enum: ONLINE, OFFLINE_PERSONAL)
├── startDate (LocalDate)
├── endDate (LocalDate, nullable)
├── status (enum: ACTIVE, PAUSED, ENDED)
├── monthlyFee (BigDecimal, nullable)
└── notes (Text)

PersonalizedPlanEntity (Planovi Trenera za Konkretnog Klijenta)
├── id (UUID)
├── relationshipId (FK -> ClientTrainerRelationship)
├── createdBy (FK -> UserEntity - trainer)
├── title (String)
├── startDate (LocalDate)
├── endDate (LocalDate)
├── plan (JSONB) - slično WorkoutPlanDayEntity strukturi
└── createdAt, updatedAt

ClientProgressEntry
├── id (UUID)
├── clientId (FK -> UserEntity)
├── relationshipId (FK -> ClientTrainerRelationship, nullable)
├── date (LocalDate)
├── weight (Float, nullable)
├── bodyFatPercentage (Float, nullable)
├── measurements (JSONB - {chest, waist, arms, ...})
├── photoUrls (String[])
└── notes (Text)

WorkoutLogEntry (Evidencija Obavljenih Treninga)
├── id (UUID)
├── clientId (FK -> UserEntity)
├── planDayId (FK -> WorkoutPlanDayEntity OR PersonalizedPlanEntity, nullable)
├── completedAt (LocalDateTime)
├── exercises (JSONB - [{exerciseId, setsCompleted, actualReps, actualWeight}, ...])
└── notes (Text)

GymSessionEntity (Offline/Inventura Treninga)
├── id (UUID)
├── trainerId (FK -> UserEntity)
├── clientId (FK -> UserEntity)
├── scheduledAt (LocalDateTime)
├── completedAt (LocalDateTime, nullable)
├── status (enum: SCHEDULED, COMPLETED, CANCELLED, NO_SHOW)
└── notes (Text)
```

---

## 🚀 Faze Razvoja

### ✅ FAZA 0: Inicijalna Postava (ZAVRŠENO)
- [x] Maven multi-modul projekt (db, application, presentation)
- [x] Spring Boot konfiguracija
- [x] PostgreSQL + Docker Compose
- [x] Autentifikacija (signup/signin) s JWT
- [x] User entitet s TRAINER/CLIENT rolama
- [x] TrainerProfile entitet
- [x] Exercise entitet i repository
- [x] Exercise pretraga s filterima
- [x] Exercise scraper za import podataka
- [x] Swagger/OpenAPI dokumentacija
- [x] Health check endpoint
- [x] SLF4J logging

---

### 🔄 FAZA 1: Certifikati, Značke i ADMIN Rola (U PLANIRANJU)

**Backend Tasks:**
- [ ] Dodati ADMIN u `UserRole` enum (TRAINER, CLIENT, ADMIN)
- [ ] Kreirati `CertificateEntity`, `BadgeEntity`, `TrainerBadgeEntity`
- [ ] Implementirati `CertificateRepository`, `BadgeRepository`, `TrainerBadgeRepository`
- [ ] `CertificateService`:
  - Upload certifikata (S3 / lokalni storage)
  - Lista certifikata trenera
  - Admin verifikacija (PENDING -> VERIFIED/REJECTED)
- [ ] `BadgeService`:
  - Automatsko dodjeljivanje znački na osnovu verificiranih certifikata
  - Lista znački trenera
  - Badge tier logika
  - Admin: CRUD operacije za badge-ove
- [ ] `CertificateController`:
  - POST /api/trainers/certificates (upload)
  - GET /api/trainers/me/certificates
  - GET /api/trainers/{id}/certificates (public view)
- [ ] `AdminCertificateController`:
  - GET /api/admin/certificates (svi certifikati + filteri)
  - PATCH /api/admin/certificates/{id}/approve
  - PATCH /api/admin/certificates/{id}/reject
- [ ] `BadgeController`:
  - GET /api/trainers/{id}/badges (public)
  - GET /api/badges (lista svih dostupnih badge-ova)
- [ ] `AdminBadgeController`:
  - POST /api/admin/badges (kreiranje)
  - PUT /api/admin/badges/{id}
  - DELETE /api/admin/badges/{id}
- [ ] File upload handling (MultipartFile -> storage service)
- [ ] Admin role checks i Security konfiguracija (@PreAuthorize("hasRole('ADMIN')"))

**Database Migrations:**
- [ ] Alter user_role enum - dodati ADMIN
- [ ] Create certificates table
- [ ] Create badges table
- [ ] Create trainer_badges table

---

### 🔄 FAZA 1.5: Vlastite Vježbe Trenera (U PLANIRANJU)

**Backend Tasks:**
- [ ] Kreirati `TrainerExerciseEntity`
- [ ] Implementirati `TrainerExerciseRepository`
- [ ] `TrainerExerciseService`:
  - CRUD operacije za trenerove vježbe
  - Kloniranje bazne vježbe (copy + allow edits)
  - Validation (samo owner može uređivati)
- [ ] `TrainerExerciseController`:
  - POST /api/trainers/me/exercises (kreiranje vlastite)
  - GET /api/trainers/me/exercises (lista mojih)
  - PUT /api/trainers/me/exercises/{id}
  - DELETE /api/trainers/me/exercises/{id}
  - POST /api/trainers/me/exercises/{id}/clone (kloniranje bazne)
- [ ] `AdminExerciseController`:
  - POST /api/admin/exercises (kreiranje bazne vježbe)
  - PUT /api/admin/exercises/{id} (update bazne)
  - DELETE /api/admin/exercises/{id} (soft delete bazne)
- [ ] Update `WorkoutPlanDayEntity` da podržava i bazne i trenerove vježbe
- [ ] Security: trainer može uređivati samo svoje vježbe

**Database Migrations:**
- [ ] Create trainer_exercises table

---

### 🔄 FAZA 2: Treningski Planovi (Marketplace)

**Backend Tasks:**
- [ ] Kreirati `WorkoutPlanEntity`, `WorkoutPlanDayEntity`
- [ ] Implementirati `WorkoutPlanRepository`, `WorkoutPlanDayRepository`
- [ ] `WorkoutPlanService`:
  - CRUD operacije za planove
  - Publish/unpublish
  - Filtriranje i pretraga (category, price, duration, trainer)
  - Kalkulacija rating-a iz review-a
- [ ] `WorkoutPlanController`:
  - POST /api/plans (kreiranje - trainer only)
  - GET /api/plans (marketplace - svi publicirani)
  - GET /api/plans/{id}
  - PUT /api/plans/{id} (update - owner only)
  - DELETE /api/plans/{id} (owner only)
  - GET /api/trainers/me/plans (moji planovi)
- [ ] Validacija (trainer može kreirati planove, client ne može)
- [ ] Paginacija i sorting za marketplace

**Database Migrations:**
- [ ] Create workout_plans table
- [ ] Create workout_plan_days table

---

### 🔄 FAZA 3: Kupovina i Review Planova

**Backend Tasks:**
- [ ] Kreirati `PurchasedPlanEntity`, `PlanReviewEntity`
- [ ] Implementirati repositories
- [ ] `PurchaseService`:
  - Kupovina plana (kreiranje PurchasedPlan zapisa)
  - Payment integracija (Stripe/PayPal - opciono za kasnije)
  - Lista kupljenih planova klijenta
  - Access kontrola (je li klijent kupio plan)
- [ ] `ReviewService`:
  - Kreiranje review-a (samo ako je kupio)
  - Lista review-a za plan
  - Update rating-a plana
- [ ] `PurchaseController`:
  - POST /api/plans/{id}/purchase
  - GET /api/clients/me/purchased-plans
- [ ] `ReviewController`:
  - POST /api/plans/{id}/reviews
  - GET /api/plans/{id}/reviews
- [ ] Security: samo kupac može reviewati

**Database Migrations:**
- [ ] Create purchased_plans table
- [ ] Create plan_reviews table

---

### 🔄 FAZA 4: Client-Trainer Odnos (Online/Offline Suradnja)

**Backend Tasks:**
- [ ] Kreirati `ClientTrainerRelationship`, `PersonalizedPlanEntity`
- [ ] `RelationshipService`:
  - Kreiranje odnosa (client request -> trainer accepts)
  - Update statusa (ACTIVE, PAUSED, ENDED)
  - Lista trenera za klijenta / klijenata za trenera
  - Razlikovanje ONLINE vs OFFLINE_PERSONAL
- [ ] `PersonalizedPlanService`:
  - Trener kreira personalizirani plan za klijenta
  - Klijent vidi svoje personalizirane planove
  - CRUD operacije
- [ ] `RelationshipController`:
  - POST /api/relationships (zahtjev za suradnju)
  - GET /api/trainers/me/clients
  - GET /api/clients/me/trainer
  - PATCH /api/relationships/{id}/status
- [ ] `PersonalizedPlanController`:
  - POST /api/relationships/{id}/plans
  - GET /api/relationships/{id}/plans
  - GET /api/clients/me/personalized-plans

**Database Migrations:**
- [ ] Create client_trainer_relationships table
- [ ] Create personalized_plans table

---

### 🔄 FAZA 5: Tracking i Progress

**Backend Tasks:**
- [ ] Kreirati `ClientProgressEntry`, `WorkoutLogEntry`
- [ ] `ProgressService`:
  - Dodavanje progress entry-a (težina, mjerenja, slike)
  - Timeline progress-a klijenta
  - Charts data (weight over time, etc.)
- [ ] `WorkoutLogService`:
  - Logiranje završenih treninga
  - Statistika (koliko puta trenirao, consistency, ...)
- [ ] `ProgressController`:
  - POST /api/clients/me/progress
  - GET /api/clients/me/progress
  - GET /api/trainers/clients/{clientId}/progress (trener vidi progress svog klijenta)
- [ ] `WorkoutLogController`:
  - POST /api/workout-logs
  - GET /api/clients/me/workout-logs
  - GET /api/workout-logs/stats

**Database Migrations:**
- [ ] Create client_progress_entries table
- [ ] Create workout_log_entries table

---

### 🔄 FAZA 6: Gym Sessions (Inventura/Offline Treninga)

**Backend Tasks:**
- [ ] Kreirati `GymSessionEntity`
- [ ] `GymSessionService`:
  - Zakazivanje termina
  - Označavanje prisutnosti (completed, no-show, cancelled)
  - Kalendar sesija za trenera
  - Lista sesija klijenta
- [ ] `GymSessionController`:
  - POST /api/gym-sessions (zakazivanje)
  - GET /api/trainers/me/gym-sessions (calendar view)
  - GET /api/clients/me/gym-sessions
  - PATCH /api/gym-sessions/{id}/status

**Database Migrations:**
- [ ] Create gym_sessions table

---

### 🔄 FAZA 7: Komunikacija i Notifikacije

**Backend Tasks:**
- [ ] `MessageEntity` (chat između klijenta i trenera)
- [ ] `NotificationEntity` (push notifications)
- [ ] WebSocket setup za real-time chat
- [ ] Email notifications (SpringBoot Mail)
- [ ] Push notifications (Firebase Cloud Messaging)

---

### 🔄 FAZA 8: Admin Panel i Dashboard

**Backend Tasks:**
- [ ] `AdminDashboardService`:
  - Aggregacija statistike (total users, trainers, clients, plans, revenue)
  - Chart data (registracije i prodaje over time)
  - Top treneri i top planovi
  - Nedavne aktivnosti (recent signups, purchases, reviews)
- [ ] `AdminDashboardController`:
  - GET /api/admin/dashboard/stats
  - GET /api/admin/dashboard/charts
  - GET /api/admin/dashboard/recent-activity
  - GET /api/admin/dashboard/top-trainers
  - GET /api/admin/dashboard/top-plans
- [ ] `AdminUserService`:
  - Lista korisnika s filterima (role, status, date range)
  - User details (full profile + activity)
  - Suspend/unsuspend user
  - Delete user (GDPR compliance)
  - Edit user details
- [ ] `AdminUserController`:
  - GET /api/admin/users (paginacija + filteri)
  - GET /api/admin/users/{id}
  - PUT /api/admin/users/{id}
  - DELETE /api/admin/users/{id}
  - PATCH /api/admin/users/{id}/suspend
  - PATCH /api/admin/users/{id}/unsuspend
- [ ] `AdminContentModerationService`:
  - Flagging system za planove i review-e
  - Uklanjanje neprikladno sadržaja
- [ ] `AdminContentController`:
  - GET /api/admin/plans/flagged
  - PATCH /api/admin/plans/{id}/remove
  - GET /api/admin/reviews/flagged
  - DELETE /api/admin/reviews/{id}
- [ ] `AdminReportService`:
  - Revenue izvještaji (daily, weekly, monthly)
  - Trainer revenue breakdown
  - Export CSV/Excel
- [ ] `AdminReportController`:
  - GET /api/admin/reports/revenue
  - GET /api/admin/reports/revenue/export
  - GET /api/admin/reports/trainers-revenue
- [ ] Security: sve admin funkcije zaštićene s @PreAuthorize("hasRole('ADMIN')")

**Database Extensions:**
- [ ] Add `status` field to UserEntity (ACTIVE, SUSPENDED, BANNED)
- [ ] Add `isFlagged` field to WorkoutPlanEntity and PlanReviewEntity
- [ ] Add audit fields gdje nedostaju (createdAt, updatedAt)

---

### 🔄 FAZA 9: Napredno i Optimizacije

**Backend Tasks:**
- [ ] Caching (Redis) za često pristupane podatke
- [ ] Search optimization (Elasticsearch za planove i trenere)
- [ ] Analytics i insights (top planovi, top treneri, ...)
- [ ] Recommendation system (suggest planove based on history)
- [ ] Subscription/recurring payments za online coaching
- [ ] Promo codes i discounts
- [ ] Affiliate program

---

## 🛠️ Tech Stack

### Backend (Trenutni)
- Java 17+
- Spring Boot 3.x
- Spring Data JPA / Hibernate
- PostgreSQL 15+
- Maven
- JWT (jjwt)
- Lombok
- Jackson (JSON processing)
- SLF4J (logging)
- SpringDoc OpenAPI (Swagger)
- Docker & Docker Compose

### Planirano Dodati
- AWS S3 / MinIO (file storage)
- Redis (caching)
- Spring Security (OAuth2, roles)
- WebSocket (real-time chat)
- Stripe/PayPal SDK (payments)
- Firebase Admin SDK (push notifications)
- Spring Mail (email)
- Elasticsearch (optional - search)
- JUnit 5 + Mockito (testing)

### Frontend (Odvojeni Codebase-ovi)
- Web: React/Next.js + TypeScript + TailwindCSS
- Mobile: React Native / Flutter

---

## 📁 Struktura Projekta (Trenutno)

```
certifit/
├── pom.xml (root parent POM)
├── docker-compose.yml (PostgreSQL)
├── .env.example
├── PROJECT_OVERVIEW.md (ovaj dokument)
│
├── db/                          (JPA entities, repositories)
│   ├── src/main/java/org/certifit/db/
│   │   ├── entity/
│   │   │   ├── UserEntity.java ✅
│   │   │   ├── TrainerProfileEntity.java ✅
│   │   │   ├── ExerciseEntity.java ✅
│   │   │   └── UserRole.java ✅
│   │   ├── repository/
│   │   │   ├── UserRepository.java ✅
│   │   │   ├── TrainerProfileRepository.java ✅
│   │   │   └── ExerciseRepository.java ✅
│   │   └── config/
│   │       └── JsonbFunctionContributor.java ✅
│   └── pom.xml
│
├── application/                 (Business logic, services)
│   ├── src/main/java/org/certifit/application/
│   │   ├── auth/
│   │   │   ├── AuthService.java ✅
│   │   │   ├── JwtService.java ✅
│   │   │   ├── command/ (SigninCommand, SignupCommand) ✅
│   │   │   └── result/ (AuthResult) ✅
│   │   ├── exercise/
│   │   │   ├── ExerciseService.java ✅
│   │   │   ├── ExerciseSpecification.java ✅
│   │   │   └── dto/ (ExerciseFilterDto) ✅
│   │   ├── scraper/
│   │   │   ├── ExerciseScraperService.java ✅
│   │   │   ├── mapper/ (ExerciseMapper) ✅
│   │   │   └── dto/ (ExerciseDto, MediaItemDto, ...) ✅
│   │   └── config/
│   │       ├── SecurityConfig.java ✅
│   │       └── RestConfig.java ✅
│   └── pom.xml
│
└── presentation/                (REST Controllers)
    ├── src/main/java/org/certifit/presentation/
    │   ├── CertifitApplication.java (main) ✅
    │   ├── auth/
    │   │   ├── AuthController.java ✅
    │   │   └── dto/ (SignupRequest, SigninRequest, AuthResponse) ✅
    │   ├── exercise/
    │   │   └── ExerciseController.java ✅
    │   ├── scraper/
    │   │   └── ExerciseScraperController.java ✅
    │   ├── health/
    │   │   └── HealthCheckController.java ✅
    │   └── config/
    │       └── SpringDocConfig.java ✅
    └── pom.xml
```

---

## 🔐 Autentifikacija i Autorizacija

### Trenutno Implementirano
- JWT tokeni (generate u JwtService)
- Signup/Signin endpoints
- UserRole enum (TRAINER, CLIENT)

### Za Dodati
- Spring Security filters (JWT validation middleware)
- Role-based access control (@PreAuthorize)
- **ADMIN role** - dodati u UserRole enum (TRAINER, CLIENT, ADMIN)
- Password reset / email verification
- OAuth2 (Google, Facebook login - opciono)
- Admin-only endpoints (@PreAuthorize("hasRole('ADMIN')"))
- Razlikovanje između TRAINER/CLIENT/ADMIN autorizacije

---

## 🌐 API Endpoints (Trenutno i Planirano)

### ✅ Implementirano

```
POST   /api/auth/signup          - Registracija (trainer/client)
POST   /api/auth/signin          - Login (JWT token)
GET    /api/health               - Health check

GET    /api/exercises            - Lista vježbi (s filterima)
GET    /api/exercises/{id}       - Detalji vježbe

POST   /api/scraper/exercises/upload - Import vježbi (admin)
```

### 🔜 Planirano (vidi faze razvoja iznad)

```
# Trainer Exercises (Vlastite Vježbe Trenera)
POST   /api/trainers/me/exercises           - kreiranje vlastite vježbe
GET    /api/trainers/me/exercises           - lista mojih vježbi
GET    /api/trainers/me/exercises/{id}      - detalji moje vježbe
PUT    /api/trainers/me/exercises/{id}      - update moje vježbe
DELETE /api/trainers/me/exercises/{id}      - brisanje moje vježbe
POST   /api/trainers/me/exercises/{id}/clone - kloniranje bazne vježbe za uređivanje

# Base Exercises (Admin Only)
POST   /api/admin/exercises                 - kreiranje bazne vježbe
PUT    /api/admin/exercises/{id}            - update bazne vježbe
DELETE /api/admin/exercises/{id}            - brisanje bazne vježbe

# Certificates & Badges
POST   /api/trainers/certificates
GET    /api/trainers/me/certificates
GET    /api/trainers/{id}/certificates
GET    /api/trainers/{id}/badges
PATCH  /api/admin/certificates/{id}/verify

# Workout Plans (Marketplace)
POST   /api/plans
GET    /api/plans                      (marketplace, filteri)
GET    /api/plans/{id}
PUT    /api/plans/{id}
DELETE /api/plans/{id}
GET    /api/trainers/me/plans

# Purchases & Reviews
POST   /api/plans/{id}/purchase
GET    /api/clients/me/purchased-plans
POST   /api/plans/{id}/reviews
GET    /api/plans/{id}/reviews

# Client-Trainer Relationship
POST   /api/relationships
GET    /api/trainers/me/clients
GET    /api/clients/me/trainer
PATCH  /api/relationships/{id}/status

# Personalized Plans
POST   /api/relationships/{id}/plans
GET    /api/relationships/{id}/plans
GET    /api/clients/me/personalized-plans

# Progress & Logs
POST   /api/clients/me/progress
GET    /api/clients/me/progress
GET    /api/trainers/clients/{clientId}/progress
POST   /api/workout-logs
GET    /api/clients/me/workout-logs

# Gym Sessions (Offline/Inventura)
POST   /api/gym-sessions
GET    /api/trainers/me/gym-sessions
GET    /api/clients/me/gym-sessions
PATCH  /api/gym-sessions/{id}/status

# Admin Dashboard & Statistics
GET    /api/admin/dashboard/stats           - ukupne statistike (users, plans, revenue, ...)
GET    /api/admin/dashboard/charts          - chart data (registracije, prodaje po danima)
GET    /api/admin/dashboard/recent-activity - nedavne aktivnosti
GET    /api/admin/dashboard/top-trainers    - top treneri
GET    /api/admin/dashboard/top-plans       - top planovi

# Admin User Management
GET    /api/admin/users                     - lista svih korisnika (filteri, paginacija)
GET    /api/admin/users/{id}                - detalji korisnika
PUT    /api/admin/users/{id}                - update korisnika
DELETE /api/admin/users/{id}                - brisanje korisnika
PATCH  /api/admin/users/{id}/suspend        - suspenzija korisnika
PATCH  /api/admin/users/{id}/unsuspend      - uklanjanje suspenzije

# Admin Certificate Management
GET    /api/admin/certificates              - svi certifikati (filteri po statusu)
GET    /api/admin/certificates/{id}         - detalji certifikata
PATCH  /api/admin/certificates/{id}/approve - odobri certifikat
PATCH  /api/admin/certificates/{id}/reject  - odbij certifikat (reason)

# Admin Badge Management
GET    /api/admin/badges                    - lista svih badge-ova
POST   /api/admin/badges                    - kreiranje badge-a
PUT    /api/admin/badges/{id}               - update badge-a
DELETE /api/admin/badges/{id}               - brisanje badge-a
POST   /api/admin/trainers/{id}/badges      - ručno dodavanje badge-a
DELETE /api/admin/trainers/{id}/badges/{badgeId} - uklanjanje badge-a

# Admin Content Moderation
GET    /api/admin/plans/flagged             - planirani za moderaciju
PATCH  /api/admin/plans/{id}/remove         - uklanjanje plana
GET    /api/admin/reviews/flagged           - review-ovi za moderaciju
DELETE /api/admin/reviews/{id}              - brisanje review-a

# Admin Financial Reports
GET    /api/admin/reports/revenue           - revenue izvještaj (params: from, to, groupBy)
GET    /api/admin/reports/revenue/export    - export CSV/Excel
GET    /api/admin/reports/trainers-revenue  - breakdown po trenerima

# Messages (TBD)
POST   /api/messages
GET    /api/messages/conversations
GET    /api/messages/conversations/{id}
```

---

## 📝 Bilješke i TODOs

- [ ] Razmotriti Liquibase/Flyway za database migrations
- [ ] Dodati integration testove (TestContainers + PostgreSQL)
- [ ] CI/CD pipeline (GitHub Actions / GitLab CI)
- [ ] Swagger autentifikacija (Bearer token input)
- [ ] Rate limiting za API endpoints
- [ ] CORS konfiguracija za frontend domene
- [ ] Environment profiles (dev, staging, prod)
- [ ] Monitoring (Prometheus + Grafana / ELK stack)
- [ ] Backup strategija za PostgreSQL
- [ ] GDPR compliance (data export, deletion)

---

## 🤝 Doprinos i Razvoj

Ovaj dokument je živi dokument koji će se ažurirati kako sustav raste.
Koristi ga kao orjentir za sljedeće faze razvoja.

**Napomena:** Frontend (Web i Mobile) će biti u odvojenim codebase-ovima i komunicirat će s ovim backendom preko REST API-ja.

---

**Verzija:** 1.1
**Zadnje Ažuriranje:** 2026-04-01
**Status:** Backend Faza 0 završena, planiranje Faze 1-9 u tijeku

**Changelog:**
- v1.1: Dodana ADMIN rola s detaljnim funkcionalnostima (dashboard, user management, certificate verification, content moderation, reports)
- v1.1: Dodana funkcionalnost za trenerove vlastite vježbe (TrainerExerciseEntity)
- v1.1: Razlikovanje baznih vježbi (admin-managed) i trenerovih vježbi
- v1.0: Inicijalna verzija s osnovnom arhitekturom i planiranim fazama

**Changelog:**
- v1.1: Dodana ADMIN rola s detaljnim funkcionalnostima (dashboard, user management, certificate verification, content moderation, reports)
- v1.1: Dodana funkcionalnost za trenerove vlastite vježbe (TrainerExerciseEntity)
- v1.1: Razlikovanje baznih vježbi (admin-managed) i trenerovih vježbi
- v1.0: Inicijalna verzija s osnovnom arhitekturom i planiranim fazama
