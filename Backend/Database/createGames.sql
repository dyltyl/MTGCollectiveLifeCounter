-- Table: games

-- DROP TABLE games;

CREATE TABLE games
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    starting_life integer DEFAULT 40,
    started boolean DEFAULT false,
    host character varying COLLATE pg_catalog."default",
    current_size integer DEFAULT 0,
    max_size integer DEFAULT 0,
    CONSTRAINT games_pkey PRIMARY KEY (id),
    CONSTRAINT games_host_fkey FOREIGN KEY (host)
        REFERENCES players (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE SET NULL
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE games
    OWNER to postgres;