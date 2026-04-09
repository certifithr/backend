CREATE TYPE collaboration_type AS ENUM ('IN_PERSON', 'ONLINE');
CREATE TYPE trainer_client_request_status AS ENUM ('PENDING', 'ACCEPTED', 'REJECTED');
CREATE TYPE trainer_client_status AS ENUM ('ACTIVE', 'PAUSED', 'ENDED');

CREATE TABLE trainer_client_requests
(
    id                 UUID                                   NOT NULL,
    client_id          UUID                                   NOT NULL,
    trainer_id         UUID                                   NOT NULL,
    collaboration_type COLLABORATION_TYPE                     NOT NULL,
    status             TRAINER_CLIENT_REQUEST_STATUS          NOT NULL,
    message            VARCHAR(1000),
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_trainer_client_requests PRIMARY KEY (id)
);

CREATE TABLE trainer_clients
(
    id                 UUID                                   NOT NULL,
    trainer_id         UUID                                   NOT NULL,
    client_id          UUID                                   NOT NULL,
    request_id         UUID,
    collaboration_type COLLABORATION_TYPE                     NOT NULL,
    status             TRAINER_CLIENT_STATUS                  NOT NULL,
    note               VARCHAR(1000),
    created_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at         TIMESTAMP WITH TIME ZONE DEFAULT NOW() NOT NULL,
    CONSTRAINT pk_trainer_clients PRIMARY KEY (id)
);

ALTER TABLE trainer_clients
    ADD CONSTRAINT uc_bd7bb60e63262ba273cd8970d UNIQUE (trainer_id, client_id);

ALTER TABLE trainer_clients
    ADD CONSTRAINT FK_TRAINER_CLIENTS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE trainer_clients
    ADD CONSTRAINT FK_TRAINER_CLIENTS_ON_REQUEST FOREIGN KEY (request_id) REFERENCES trainer_client_requests (id);

ALTER TABLE trainer_clients
    ADD CONSTRAINT FK_TRAINER_CLIENTS_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES users (id);

ALTER TABLE trainer_client_requests
    ADD CONSTRAINT FK_TRAINER_CLIENT_REQUESTS_ON_CLIENT FOREIGN KEY (client_id) REFERENCES users (id);

ALTER TABLE trainer_client_requests
    ADD CONSTRAINT FK_TRAINER_CLIENT_REQUESTS_ON_TRAINER FOREIGN KEY (trainer_id) REFERENCES users (id);