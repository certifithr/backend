# Trainer–Client Onboarding

A `ClientProfileEntity` is what binds a client to a trainer. It is never created directly — it is always the outcome of a completed invitation flow. All three flows share the same `TrainerClientInvitationEntity` and resolve through the same `ClientProfileEntity` creation.

---

## Flows

### 1. Client requests a trainer

The client has an account and initiates contact.

```
CLIENT                              BACKEND
  |                                    |
  |-- POST /api/invitations            |
  |   /request-trainer                 |
  |   { trainerProfileId }  ---------> |  Create invitation
  |                                    |  clientUser = client
  |                                    |  email = client's email
  |                                    |  status = PENDING
  |                                    |  expiresAt = now + 30d
  |
  |                                 TRAINER
  |                                    |
  |                     GET /pending-for-trainer
  |                                    |  (sees the request)
  |                                    |
  |                     POST /{id}/approve
  |                                    |  Creates ClientProfileEntity
  |                                    |  status → ACCEPTED
```

---

### 2. Trainer invites an existing user

The trainer knows the person already has a CertiFit account.

```
TRAINER                             BACKEND
  |                                    |
  |-- POST /api/invitations            |
  |   /invite-by-email                 |
  |   { email }             ---------> |  Looks up UserEntity by email
  |                                    |  clientUser = found user
  |                                    |  status = PENDING
  |                                    |  expiresAt = now + 7d
  |                                    |  (no token generated)
  |
  |                                  CLIENT
  |                                    |
  |                      GET /pending-for-client
  |                                    |  (sees the invitation)
  |                                    |
  |                      POST /{id}/accept  or  /{id}/decline
  |                                    |  Creates ClientProfileEntity (if accepted)
  |                                    |  status → ACCEPTED / DECLINED
```

---

### 3. Trainer invites by email — person has no account yet

The trainer provides an email address not registered in the system.

```
TRAINER                             BACKEND
  |                                    |
  |-- POST /api/invitations            |
  |   /invite-by-email                 |
  |   { email }             ---------> |  No UserEntity found for email
  |                                    |  clientUser = null
  |                                    |  token = random UUID
  |                                    |  status = PENDING
  |                                    |  expiresAt = now + 7d
  |                                    |
  |                                    |  TODO: send email with token / link
  |
  |                              NEW USER
  |                                    |
  |                        Registers account
  |                        (uses the same email address)
  |                                    |
  |                      POST /{id}/accept
  |                                    |  clientUser == null, so identity is
  |                                    |  verified by email match:
  |                                    |  user.email == invitation.email
  |                                    |  Sets clientUser, creates ClientProfileEntity
  |                                    |  status → ACCEPTED
```

---

## Business Rules

| Rule | Where enforced |
|---|---|
| A client can only belong to one trainer | `acceptInvitation`, `approveRequest` — checks for existing `ClientProfileEntity` |
| Only the invited client can accept or decline | Email match (no-account flow) or `clientUser.id` match |
| Only the owning trainer can approve | `invitation.trainer.user.id == trainerUserId` |
| A duplicate pending invitation is rejected | `existsByTrainer_IdAndClientUser_IdAndStatus` / `existsByTrainer_IdAndEmailAndStatus` |
| Trainer cannot approve an email-only invite | `clientUser == null` guard in `approveRequest` — the person must accept themselves |
| Expired invitations cannot be acted on | `ensurePendingAndNotExpired` lazily marks EXPIRED and throws |

---

## Invitation Lifecycle

```
               PENDING
              /   |   \
             /    |    \
        ACCEPTED  |  DECLINED
                  |
               EXPIRED
          (expiresAt passed,
           lazily set on next
           access or via scheduler)
```

`expireOldInvitations()` in `TrainerClientInvitationService` is designed to be called by a scheduler to bulk-expire stale invitations.

---

## API Reference

| Method | Endpoint | Role | Description |
|---|---|---|---|
| `POST` | `/api/invitations/request-trainer` | `CLIENT` | Client requests to join a trainer |
| `POST` | `/api/invitations/invite-by-email` | `TRAINER` | Trainer invites by email |
| `POST` | `/api/invitations/{id}/accept` | `CLIENT` | Client accepts an invitation |
| `POST` | `/api/invitations/{id}/decline` | `CLIENT` | Client declines an invitation |
| `POST` | `/api/invitations/{id}/approve` | `TRAINER` | Trainer approves a client request |
| `GET` | `/api/invitations/pending-for-trainer` | `TRAINER` | Lists trainer's pending invitations |
| `GET` | `/api/invitations/pending-for-client` | `CLIENT` | Lists client's pending invitations |
