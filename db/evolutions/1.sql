# --- First database schema

# --- !Ups

--
-- Name: revision; Type: TABLE; Schema: public
--

CREATE TABLE revision (
    id SERIAL PRIMARY KEY,
    "timestamp" bigint NOT NULL,
    ipaddress TEXT,
    owner_id INTEGER,
    version INTEGER DEFAULT 0
);

--
-- Name: region; Type: TABLE; Schema: public
--

CREATE TABLE region (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    geographical_distribution TEXT NOT NULL,
    name TEXT UNIQUE NOT NULL,
    version INTEGER DEFAULT 0
);

--
-- Name: province; Type: TABLE; Schema: public
--

CREATE TABLE province (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    code TEXT NOT NULL,
    name TEXT NOT NULL,
    region_id INTEGER NOT NULL REFERENCES region(id),
    version INTEGER DEFAULT 0
);

CREATE INDEX province_region_key ON province USING btree (region_id);

--
-- Name: province_history; Type: TABLE; Schema: public
--

CREATE TABLE province_history (
    id INTEGER NOT NULL,
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint,
    code TEXT,
    name TEXT
);

CREATE INDEX province_history_revision_key ON province_history USING btree (revision);

--
-- Name: municipal; Type: TABLE; Schema: public
--

CREATE TABLE municipal (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    code INTEGER NOT NULL,
    name TEXT NOT NULL,
    province_id INTEGER NOT NULL REFERENCES province(id),
    province_previous_id INTEGER REFERENCES province(id),
    version INTEGER DEFAULT 0,
    enabled boolean DEFAULT true NOT NULL
);

CREATE INDEX municipal_province_key ON municipal USING btree (province_id);
CREATE INDEX municipal_province_previous_key ON municipal USING btree (province_previous_id);

--
-- Name: municipal_history; Type: TABLE; Schema: public
--

CREATE TABLE municipal_history (
    id INTEGER NOT NULL,
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint,
    code INTEGER,
    name TEXT,
    province_id INTEGER,
    province_previous_id INTEGER,
    enabled boolean,
    PRIMARY KEY (id, revision)
);

CREATE INDEX municipal_history_revision_key ON municipal_history USING btree (revision);


--
-- Name: operator_profile; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    version INTEGER DEFAULT 0,
    name text UNIQUE NOT NULL,
    description text,
    active boolean DEFAULT true NOT NULL,
    login_url text,
    disclaimer text
);

--
-- Name: operator_profile_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_history (
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint NOT NULL,
    id INTEGER NOT NULL,
    name text,
    description text,
    active boolean DEFAULT true,
    login_url text,
    disclaimer text,
    page_layout text,
    PRIMARY KEY (revision, revision_type, id)
);

CREATE INDEX operator_profile_history_revision_key ON operator_profile_history USING btree (revision);

--
-- Name: operator; Type: TABLE; Schema: public
--

CREATE TABLE operator (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    email TEXT NOT NULL,
    enabled boolean NOT NULL,
    last_login_address TEXT,
    last_login_date timestamp without time zone,
    password TEXT NOT NULL,
    firstname text NOT NULL,
    lastname text NOT NULL,
    version INTEGER DEFAULT 0,
    photo text,
    page_layout text DEFAULT 'DEFAULT'::text,
    iban text,
    profile_id INTEGER NOT NULL REFERENCES operator_profile(id)
);

CREATE INDEX operator_profile_key ON operator USING btree (profile_id);

--
-- Name: operator_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_history (
    id INTEGER NOT NULL,
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint,
    email TEXT,
    enabled boolean,
    firstname TEXT,
    lastname TEXT,
    password TEXT,
    photo text,
    iban text,
    profile_id INTEGER,
    PRIMARY KEY (id, revision)
);

CREATE INDEX operator_history_revision_key ON operator_history USING btree (revision);

--
-- Name: operator_profile_roles; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_roles (
    operator_profile_id INTEGER NOT NULL REFERENCES operator_profile(id),
    roles text NOT NULL,
    PRIMARY KEY (operator_profile_id, roles)
);

--
-- Name: operator_profile_roles_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_roles_history (
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint NOT NULL,
    operator_profile_id INTEGER NOT NULL,
    roles text NOT NULL,
    PRIMARY KEY (revision, operator_profile_id, roles)
);

CREATE INDEX operator_profile_roles_history_revision_key ON operator_profile_roles_history USING btree (revision);

--
-- Name: operator_roles; Type: TABLE; Schema: public
--

CREATE TABLE operator_roles (
    operator_id INTEGER NOT NULL REFERENCES operator(id),
    roles text NOT NULL,
    PRIMARY KEY (operator_id, roles)
);

--
-- Name: operator_roles_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_roles_history (
    revision INTEGER NOT NULL REFERENCES revision(id),
    revision_type smallint,
    operator_id INTEGER NOT NULL,
    roles text NOT NULL,
    PRIMARY KEY (revision, operator_id, roles)
);

CREATE INDEX operator_roles_history_revision_key ON operator_roles_history USING btree (revision);

--
-- Name: notification; Type: TABLE; Schema: public
--

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    version INTEGER,
    message text,
    destination_id INTEGER NOT NULL REFERENCES operator(id),
    read boolean DEFAULT false NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    subject_id INTEGER,
    subject text NOT NULL
);

CREATE INDEX notification_destination_key ON notification USING btree (destination_id);

--
-- Name: task; Type: TABLE; Schema: public
--

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    start_date date,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    version INTEGER DEFAULT 0,
    description text NOT NULL,
    target_type TEXT NOT NULL,
    target_id INTEGER
);

--
-- Name: recovery_request; Type: TABLE; Schema: public
--

CREATE TABLE recovery_request (
    id SERIAL PRIMARY KEY,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    code text NOT NULL,
    ipaddress text,
    operator_id INTEGER NOT NULL REFERENCES operator(id),
    version INTEGER DEFAULT 0
);

CREATE INDEX recovery_request_operator_key ON recovery_request USING btree (operator_id);


CREATE TABLE comment (
    id SERIAL PRIMARY KEY,
    version INTEGER DEFAULT 0 NOT NULL,
    comment text NOT NULL,
    owner_id INTEGER REFERENCES operator(id),
    created_at timestamp without time zone NOT NULL,
    updated_at timestamp without time zone NOT NULL,
    related_to_comment_id INTEGER,
    active boolean DEFAULT true,
    target_type TEXT,
    target_id INT
);

CREATE INDEX comment_owner_key ON comment USING btree (owner_id);

--
-- Name: task_candidate_assignees; Type: TABLE; Schema: public
--

CREATE TABLE task_candidate_assignees (
    task_for_candidate_assignee_id INTEGER NOT NULL REFERENCES task(id),
    candidate_assignees_id INTEGER NOT NULL REFERENCES operator(id)
);


CREATE INDEX task_candidate_assignees_candidate_assignees_key ON task_candidate_assignees USING btree (candidate_assignees_id);
CREATE INDEX task_candidate_assignees_task_for_candidate_assignee_key ON task_candidate_assignees USING btree (task_for_candidate_assignee_id);


# --- !Downs

DROP TABLE comment;
DROP TABLE notification;
DROP TABLE task_candidate_assignees;
DROP TABLE task;
DROP TABLE recovery_request;
DROP TABLE operator_profile_roles_history;
DROP TABLE operator_profile_roles;
DROP TABLE operator_roles;
DROP TABLE operator_roles_history;
DROP TABLE operator_history;
DROP TABLE operator;
DROP TABLE operator_profile_history;
DROP TABLE operator_profile;
DROP TABLE municipal_history;
DROP TABLE municipal;
DROP TABLE province_history;
DROP TABLE province;
DROP TABLE region;
DROP TABLE revision;
