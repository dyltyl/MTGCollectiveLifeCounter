-- Table: public.commander_damage

-- DROP TABLE public.commander_damage;

CREATE TABLE public.commander_damage
(
    player character varying COLLATE pg_catalog."default" NOT NULL,
    enemy_player character varying COLLATE pg_catalog."default" NOT NULL,
    game character varying COLLATE pg_catalog."default" NOT NULL,
    commander character varying COLLATE pg_catalog."default" NOT NULL,
    damage integer NOT NULL,
    CONSTRAINT commander_damage_pkey PRIMARY KEY (player, enemy_player, game, commander),
    CONSTRAINT commander_damage_enemy_player_fkey FOREIGN KEY (enemy_player)
        REFERENCES players (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION,
    CONSTRAINT commander_damage_game_fkey FOREIGN KEY (game)
        REFERENCES games (id) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT commander_damage_player_fkey FOREIGN KEY (player)
        REFERENCES players (email) MATCH SIMPLE
        ON UPDATE CASCADE
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.commander_damage
    OWNER to postgres;