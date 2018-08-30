package com.objects;

public class CommanderDamage {
    private String player, enemyPlayer, commander;
    private int damage;
    public CommanderDamage() {

    }
    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getEnemyPlayer() {
        return enemyPlayer;
    }

    public void setEnemyPlayer(String enemyPlayer) {
        this.enemyPlayer = enemyPlayer;
    }

    public String getCommander() {
        return commander;
    }

    public void setCommander(String commander) {
        this.commander = commander;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }


}
