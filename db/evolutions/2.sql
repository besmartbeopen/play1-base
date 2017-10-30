# --- Messaggi agli operatori.

# --- !Ups

CREATE TABLE message (
    id SERIAL PRIMARY KEY,
    source_id integer NOT NULL REFERENCES operator(id),
    subject TEXT NOT NULL,
    body TEXT,
    version integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


CREATE TABLE message_attachments (
    message_id integer NOT NULL REFERENCES message(id),
    attachments TEXT
);


CREATE TABLE message_detail (
    id SERIAL PRIMARY KEY,
    read boolean NOT NULL DEFAULT false,
    starred boolean NOT NULL DEFAULT false,
    message_id integer NOT NULL REFERENCES message(id),
    operator_id integer NOT NULL REFERENCES operator(id),
    version integer,
    created_at timestamp without time zone,
    updated_at timestamp without time zone
);


CREATE TABLE message_detail_tags (
    message_detail_id integer NOT NULL REFERENCES message_detail(id),
    tags character varying(255)
);


CREATE TABLE message_tags (
    message integer NOT NULL REFERENCES message(id),
    tags character varying(255)
);


CREATE TABLE message_history (
    revision INT NOT NULL REFERENCES revision(id),
    revision_type SMALLINT,
    id INT NOT NULL,
    body TEXT,
    subject TEXT,
    source_id integer,
    PRIMARY KEY (revision, id)
);

CREATE TABLE message_detail_history (
    revision INT NOT NULL REFERENCES revision(id),
    revision_type SMALLINT,
    id INT NOT NULL,
    read boolean,
    starred boolean,
    message_id integer,
    operator_id integer,
    PRIMARY KEY (revision, id)
);

# --- !Downs

DROP TABLE IF EXISTS message_attachments;
DROP TABLE IF EXISTS message_detail_tags;
DROP TABLE IF EXISTS message_detail;
DROP TABLE IF EXISTS message_tags;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS message_detail_history;
DROP TABLE IF EXISTS message_history;
