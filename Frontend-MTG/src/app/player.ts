/**
 * The player object
 */
export class Player {
    /**
     * The name of the player
     */
    name: string;
    /**
     * The email of the player, must be unique in the database
     */
    email: string;
    /**
     * The password of the player, can be left blank
     */
    password: string;
    /**
     * The current life of the player
     */
    life: number;
    /**
     * The number of poison counters the player has
     */
    poison: number;
    /**
     * The number of experience counters the player has
     */
    experience: number;
    /**
     * The commander damage table for the player. The table is set up as:
     * commanderDamage<enemyPlayer, <enemyCommander, damage>>
     */
    commanderDamage: Map<string, Map<string, number>>;
    /**
     * Creates a new player, with 0 commander damage, poison, and experience counters
     * @param name The name of the player
     * @param email The email of the player, must be unique in the database
     * @param password The password of the player, can be left blank
     * @param life The starting life of the player
     */
    constructor(name: string, email: string, password: string, life: number) { // TODO Might have to initialize the commanderDamage
        this.name = name;
        this.email = email;
        this.password = password;
        this.life = life;
        this.poison = 0;
        this.experience = 0;
    }
}
