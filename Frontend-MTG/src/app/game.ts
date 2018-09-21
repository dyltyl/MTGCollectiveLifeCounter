/**
 * The Game object
 */
export class Game {
    /**
     * The id of the game, must be unique in the database
     */
    gameId: string;
    /**
     * The password of the game, can be left blank
     */
    gamePassword: string;
    /**
     * The life that each player starts at
     */
    startingLife: number;
    /**
     * Whether or not the game has started
     */
    started: boolean;
    /**
     * The email of the player who is hosting the game
     */
    host: string;
    /**
     * The amount of players in the game
     */
    currentSize: number;
    /**
     * The maximum number of players that can be in the game
     */
    maxSize: number;
    /**
     * Creates the Game object, with a current size of 0
     * @param gameId The id of the game, must be unique in the database
     * @param gamePassword The password of the game, can be left blank
     * @param startingLife The life that each player starts at
     * @param host The email of the player who is hosting the game
     * @param maxSize The maximum number of players that can be in the game
     * @param started Whether or not the game has started
     */
    constructor(gameId: string, gamePassword: string, startingLife: number, host: string, maxSize: number, started: boolean) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
        this.startingLife = startingLife;
        this.currentSize = 0;
        this.host = host;
        this.maxSize = maxSize;
        this.started = started;
    }
}
