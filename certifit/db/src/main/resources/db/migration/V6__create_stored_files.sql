CREATE TABLE stored_files (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    key            TEXT NOT NULL,
    bucket         VARCHAR(255) NOT NULL,
    mime_type      VARCHAR(100),
    file_size_bytes BIGINT,
    uploaded_by    UUID NOT NULL REFERENCES users(id),
    confirmed_at   TIMESTAMPTZ,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
