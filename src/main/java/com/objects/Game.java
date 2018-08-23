package com.objects;

public class Game {
    private String gameId, gamePassword;
    public Game() {
    }
    public Game(String gameId, String gamePassword) {
        this.gameId = gameId;
        this.gamePassword = gamePassword;
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


}
