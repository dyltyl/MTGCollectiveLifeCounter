package com.objects;

public class Game {
    private String gameId, gamePassword, host;
    private int startingLife, currentSize, maxSize;
    private boolean started;
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
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCurrentSize() {
        return currentSize;
    }

    public void setCurrentSize(int currentSize) {
        this.currentSize = currentSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
    public boolean getStarted() {
        return started;
    }
    public void setStarted(boolean started) {
        this.started = started;
    }

}
