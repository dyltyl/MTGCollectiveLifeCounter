/**
 * A commander damage object used to set the commander damage in the server
 */
export class CommanderDamage {
    /**
     * The player being hit with commander damage
     */
    player: string;
    /**
     * The enemy player
     */
    enemyPlayer: string;
    /**
     * The enemy player's commander
     */
    commander: string;
    /**
     * The amount of commander damage to set
     */
    damage: number;
    /**
     * Creates a commander damage object
     * @param player The player being hit with commander damage
     * @param enemyPlayer The enemy player
     * @param enemyCommander The enemy player's commander
     * @param damage The amount of commander damage to set
     */
    constructor(player: string, enemyPlayer: string, enemyCommander: string, damage) {
        this.player = player;
        this.enemyPlayer = enemyPlayer;
        this.commander = enemyCommander;
        this.damage = damage;
    }
}
