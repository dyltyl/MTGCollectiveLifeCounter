package com.objects;

public class Game {
    private String gameId, gamePassword;
    private int startingLife;
    public Game() {
    }
    public Game(String gameId, String gamePassword, int startingLife) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
        this.startingLife = startingLife;
    }
    public String getGameId() {
        return gameId;
    }
    public String getGamePassword() {
        return gamePassword;
    }
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    public void setGamePassword(String gamePassword) {
        this.gamePassword = gamePassword;
    }
    public int getStartingLife() {
        return startingLife;
    }
    public void setStartingLife(int startingLife) {
        this.startingLife = startingLife;
    }


}
