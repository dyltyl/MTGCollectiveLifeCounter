-- Table: public.commanders

-- DROP TABLE public.commanders;

CREATE TABLE public.commanders
(
    game character varying COLLATE pg_catalog."default" NOT NULL,
    player character varying COLLATE pg_catalog."default" NOT NULL,
    commander character varying COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT commanders_pkey PRIMARY KEY (game, player, commander),
    CONSTRAINT commanders_game_fkey FOREIGN KEY (game)
        REFERENCES public.games (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT commanders_player_fkey FOREIGN KEY (player)
        REFERENCES public.players (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.commanders
    OWNER to postgres;