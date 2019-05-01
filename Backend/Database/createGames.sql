-- Table: public.games

-- DROP TABLE public.games;

CREATE TABLE public.games
(
    id character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    starting_life integer DEFAULT 40,
    started boolean DEFAULT false,
    host character varying COLLATE pg_catalog."default",
    current_size integer DEFAULT 0,
    max_size integer DEFAULT 0,
    CONSTRAINT games_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.games
    OWNER to postgres;