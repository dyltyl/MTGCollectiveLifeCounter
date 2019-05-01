-- Table: public.players

-- DROP TABLE public.players;

CREATE TABLE public.players
(
    email character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    name character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT players_pkey PRIMARY KEY (email)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.players
    OWNER to postgres;