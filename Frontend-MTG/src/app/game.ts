export class Game {
    gameId: string;
    gamePassword: string;
    startingLife: number;
    started: boolean;
    host: string;
    currentSize: number;
    maxSize: number;
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
