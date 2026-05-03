CREATE TYPE invitation_status AS ENUM ('PENDING', 'ACCEPTED', 'DECLINED', 'EXPIRED');

CREATE TABLE trainer_client_invitations (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    trainer_id     UUID NOT NULL REFERENCES trainer_profiles(id),
    email          VARCHAR(255) NOT NULL,
    client_user_id UUID REFERENCES users(id),
    token          VARCHAR(255),
    status         invitation_status NOT NULL DEFAULT 'PENDING',
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at     TIMESTAMPTZ
);
