-- Table: public.life

-- DROP TABLE public.life;

CREATE TABLE public.life
(
    email character varying COLLATE pg_catalog."default" NOT NULL,
    game character varying COLLATE pg_catalog."default" NOT NULL,
    life integer NOT NULL,
    poison integer DEFAULT 0,
    experience integer DEFAULT 0,
    CONSTRAINT life_pkey PRIMARY KEY (email, game),
    CONSTRAINT life_email_fkey FOREIGN KEY (email)
        REFERENCES public.players (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT life_game_fkey FOREIGN KEY (game)
        REFERENCES public.games (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.life
    OWNER to postgres;