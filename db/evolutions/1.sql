# --- First database schema

# --- !Ups

--
-- Name: revision; Type: TABLE; Schema: public
--

CREATE TABLE revision (
    id SERIAL PRIMARY KEY,
    "timestamp" bigint NOT NULL,
    ipaddress TEXT,
    owner_id integer,
    version integer DEFAULT 0
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
    version integer DEFAULT 0
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
    region_id integer NOT NULL REFERENCES region(id),
    version integer DEFAULT 0
);

--
-- Name: province_history; Type: TABLE; Schema: public
--

CREATE TABLE province_history (
    id integer NOT NULL,
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint,
    code TEXT,
    name TEXT
);

--
-- Name: municipal; Type: TABLE; Schema: public
--

CREATE TABLE municipal (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    code integer NOT NULL,
    name TEXT NOT NULL,
    province_id integer NOT NULL REFERENCES province(id),
    province_previous_id integer REFERENCES province(id),
    version integer DEFAULT 0,
    enabled boolean DEFAULT true NOT NULL
);

CREATE INDEX municipal_province_key ON municipal USING btree (province_id);
CREATE INDEX municipal_province_previous_key ON municipal USING btree (province_previous_id);

--
-- Name: municipal_history; Type: TABLE; Schema: public
--

CREATE TABLE municipal_history (
    id integer NOT NULL,
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint,
    code integer,
    name TEXT,
    province_id integer,
    province_previous_id integer,
    enabled boolean,
    PRIMARY KEY (id, revision)
);



--
-- Name: notification; Type: TABLE; Schema: public
--

CREATE TABLE notification (
    id SERIAL PRIMARY KEY,
    version integer,
    message text,
    destination_id integer NOT NULL,
    read boolean DEFAULT false NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    subject_id integer,
    subject text NOT NULL
);

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
    version integer DEFAULT 0,
    photo text,
    stock_id integer,
    employee boolean DEFAULT true NOT NULL,
    cost_per_km numeric,
    refund_per_km numeric,
    manager_id integer,
    page_layout text DEFAULT 'DEFAULT'::text,
    iban text,
    profile_id integer NOT NULL
);

--
-- Name: operator_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_history (
    id integer NOT NULL,
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint,
    email TEXT,
    enabled boolean,
    firstname TEXT,
    lastname TEXT,
    password TEXT,
    photo text,
    stock_id integer,
    employee boolean DEFAULT true,
    cost_per_km numeric,
    refund_per_km numeric,
    manager_id integer,
    page_layout text DEFAULT 'DEFAULT'::text,
    iban text,
    profile_id integer,
    PRIMARY KEY (id, revision)
);

--
-- Name: operator_profile; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    version integer DEFAULT 0,
    name text NOT NULL,
    description text,
    active boolean DEFAULT true NOT NULL,
    employee boolean NOT NULL,
    agent boolean NOT NULL,
    login_url text,
    disclaimer text,
    page_layout text NOT NULL
);

--
-- Name: operator_profile_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_history (
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint NOT NULL,
    id integer NOT NULL,
    name text,
    description text,
    active boolean DEFAULT true,
    employee boolean,
    agent boolean,
    login_url text,
    disclaimer text,
    page_layout text
);

--
-- Name: operator_profile_roles; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_roles (
    operator_profile_id integer NOT NULL,
    roles text NOT NULL
);

--
-- Name: operator_profile_roles_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_profile_roles_history (
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint NOT NULL,
    operator_profile_id integer NOT NULL,
    roles text NOT NULL
);

--
-- Name: operator_roles; Type: TABLE; Schema: public
--

CREATE TABLE operator_roles (
    operator_id integer NOT NULL,
    roles text NOT NULL
);

--
-- Name: operator_roles_history; Type: TABLE; Schema: public
--

CREATE TABLE operator_roles_history (
    revision integer NOT NULL REFERENCES revision(id),
    revision_type smallint,
    operator_id integer NOT NULL,
    roles text NOT NULL
);

--
-- Name: task; Type: TABLE; Schema: public
--

CREATE TABLE task (
    id SERIAL PRIMARY KEY,
    start_date date,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    version integer DEFAULT 0,
    description text NOT NULL,
    taskable integer
);

--
-- Name: recovery_request; Type: TABLE; Schema: public; Owner: inphase
--

CREATE TABLE recovery_request (
    id SERIAL PRIMARY KEY,
    created_at timestamp without time zone,
    updated_at timestamp without time zone,
    code text NOT NULL,
    ipaddress text,
    operator_id integer NOT NULL REFERENCES operator(id),
    version integer DEFAULT 0
);

CREATE INDEX recovery_request_operator_key ON recovery_request USING btree (operator_id);

--
-- Name: task_candidate_assignees; Type: TABLE; Schema: public
--

CREATE TABLE task_candidate_assignees (
    task_for_candidate_assignee_id integer NOT NULL,
    candidate_assignees_id integer NOT NULL
);


--
-- Name: operator_profile_history operator_profile_history_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_profile_history
    ADD CONSTRAINT operator_profile_history_pkey PRIMARY KEY (revision, revision_type, id);


--
-- Name: operator_profile operator_profile_name_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_profile
    ADD CONSTRAINT operator_profile_name_key UNIQUE (name);


--
-- Name: operator_profile_roles_history operator_profile_roles_history_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_profile_roles_history
    ADD CONSTRAINT operator_profile_roles_history_pkey PRIMARY KEY (revision, operator_profile_id, roles);


--
-- Name: operator_profile_roles operator_profile_roles_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_profile_roles
    ADD CONSTRAINT operator_profile_roles_pkey PRIMARY KEY (operator_profile_id, roles);


--
-- Name: operator_roles_history operator_roles_pkey; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_roles_history
    ADD CONSTRAINT operator_roles_pkey PRIMARY KEY (revision, operator_id, roles);


--
-- Name: operator_roles operator_roles_primary_key; Type: CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_roles
    ADD CONSTRAINT operator_roles_primary_key PRIMARY KEY (operator_id, roles);



--
-- Name: municipal_history_revision_key; Type: INDEX; Schema: public
--

CREATE INDEX municipal_history_revision_key ON municipal_history USING btree (revision);


--
-- Name: notification_destination_key; Type: INDEX; Schema: public
--

CREATE INDEX notification_destination_key ON notification USING btree (destination_id);


--
-- Name: operator_history_revision_key; Type: INDEX; Schema: public
--

CREATE INDEX operator_history_revision_key ON operator_history USING btree (revision);


--
-- Name: operator_manager_key; Type: INDEX; Schema: public
--

CREATE INDEX operator_manager_key ON operator USING btree (manager_id);


--
-- Name: operator_profile_key; Type: INDEX; Schema: public
--

CREATE INDEX operator_profile_key ON operator USING btree (profile_id);


--
-- Name: operator_roles_history_revision_key; Type: INDEX; Schema: public
--

CREATE INDEX operator_roles_history_revision_key ON operator_roles_history USING btree (revision);


--
-- Name: province_history_revision_key; Type: INDEX; Schema: public
--

CREATE INDEX province_history_revision_key ON province_history USING btree (revision);


--
-- Name: province_region_key; Type: INDEX; Schema: public
--

CREATE INDEX province_region_key ON province USING btree (region_id);

--
-- Name: task_candidate_assignees_candidate_assignees_key; Type: INDEX; Schema: public
--

CREATE INDEX task_candidate_assignees_candidate_assignees_key ON task_candidate_assignees USING btree (candidate_assignees_id);


--
-- Name: task_candidate_assignees_task_for_candidate_assignee_key; Type: INDEX; Schema: public
--

CREATE INDEX task_candidate_assignees_task_for_candidate_assignee_key ON task_candidate_assignees USING btree (task_for_candidate_assignee_id);

--
-- Name: notification notification_destination_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY notification
    ADD CONSTRAINT notification_destination_fkey FOREIGN KEY (destination_id) REFERENCES operator(id);



--
-- Name: operator operator_manager_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator
    ADD CONSTRAINT operator_manager_fkey FOREIGN KEY (manager_id) REFERENCES operator(id);


--
-- Name: operator operator_profile_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator
    ADD CONSTRAINT operator_profile_fkey FOREIGN KEY (profile_id) REFERENCES operator_profile(id);


--
-- Name: operator_profile_roles operator_profile_roles_operator_profile_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_profile_roles
    ADD CONSTRAINT operator_profile_roles_operator_profile_fkey FOREIGN KEY (operator_profile_id) REFERENCES operator_profile(id);

--
-- Name: operator_roles operator_roles_operator_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY operator_roles
    ADD CONSTRAINT operator_roles_operator_fkey FOREIGN KEY (operator_id) REFERENCES operator(id);


--
-- Name: task_candidate_assignees task_candidate_assignees_candidate_assignees_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY task_candidate_assignees
    ADD CONSTRAINT task_candidate_assignees_candidate_assignees_fkey FOREIGN KEY (candidate_assignees_id) REFERENCES operator(id);


--
-- Name: task_candidate_assignees task_candidate_assignees_task_for_candidate_assignee_fkey; Type: FK CONSTRAINT; Schema: public
--

ALTER TABLE ONLY task_candidate_assignees
    ADD CONSTRAINT task_candidate_assignees_task_for_candidate_assignee_fkey FOREIGN KEY (task_for_candidate_assignee_id) REFERENCES task(id);
    
# --- !Downs

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
