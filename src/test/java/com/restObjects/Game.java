package com.restObjects;

import com.Application;

public class Game {
    private RestObject rest;
    private String gameId, gamePassword;
    private int startingLife;
    public Game() {
        rest = new RestObject();
    }
    public static Response createGame(String gameId, String gamePassword, int life) {
        Game game = new Game();
        game.setGameId(gameId);
        game.setGamePassword(gamePassword);
        game.setStartingLife(life);
        return game.rest().sendPostRequest(RestObject.BASE_URL + "/game", Application.getJson(game, true));
    }
    public static Response createGame(Game game) {
        return createGame(game.getGameId(), game.getGamePassword(), game.getStartingLife());
    }
    public static Response updateGame(String originalId, String gameId, String gamePassword, int life) {
        Game game = new Game();
        game.rest().setHeader("gameId", originalId);
        game.setGameId(gameId);
        game.setGamePassword(gamePassword);
        game.setStartingLife(life);
        return game.rest().sendPutRequest(RestObject.BASE_URL + "/game", Application.getJson(game, true));
    }
    public static Response hasGameStarted(String gameId) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        return game.rest().sendGetRequest(RestObject.BASE_URL + "/hasGameStarted");
    }
    public static Response startGame(String gameId) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        return game.rest().sendGetRequest(RestObject.BASE_URL + "/startGame");
    }
    public static Response allCommanders(String gameId) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        return game.rest().sendGetRequest(RestObject.BASE_URL + "/commanders");
    }
    public static Response search(String gameId) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        return game.rest().sendGetRequest(RestObject.BASE_URL + "/game");
    }
    public static Response verify(String gameId, String gamePassword) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        game.rest().setHeader("gamePassword", gamePassword);
        return game.rest().sendGetRequest(RestObject.BASE_URL + "/verifyGame");
    }
    public static Response deleteGame(String gameId) {
        Game game = new Game();
        game.rest().setHeader("gameId", gameId);
        return game.rest().sendDeleteRequest(RestObject.BASE_URL + "/game");
    }
    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getGamePassword() {
        return gamePassword;
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
    public RestObject rest() {
        return rest;
    }
}
