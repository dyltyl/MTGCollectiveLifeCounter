package com.restObjects;

import com.Application;
import com.objects.CommanderDamage;

import java.util.HashMap;

public class Player extends RestObject {
    private RestObject rest;
    private String name, email, password;
    private int life, poison, experience;
    private HashMap<String, HashMap<String, Integer>> commanderDamage;
    public Player() {
        rest = new RestObject();
    }
    public static Response createPlayer(String email, String name, String password) {
        Player player = new Player();
        player.setEmail(email);
        player.setName(name);
        player.setPassword(password);
        return player.rest().sendPostRequest(RestObject.BASE_URL + "/player", Application.getJson(player, true));
    }
    public static Response createPlayer(Player player) {
        return createPlayer(player.getEmail(), player.getName(), player.getPassword());
    }
    public static Response updatePlayer(String currentEmail, String email, String name, String password) {
        Player player = new Player();
        player.setEmail(email);
        player.setName(name);
        player.setPassword(password);
        player.rest().setHeader("email", currentEmail);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/player", Application.getJson(player, true));
    }
    public static Response deletePlayer(String email, String password) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        return player.rest().sendDeleteRequest(RestObject.BASE_URL + "/player");
    }
    public static Response byEmail(String email, String gameId) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        player.rest().setHeader("gameId", gameId);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/player");
    }
    public static Response all(String gameId) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/players");
    }
    public static Response allGames(String email) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/gamesPlayerIsIn");
    }
    public static Response joinGame(String email, String password, String gameId, String gamePassword, String[] commanders) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("gamePassword", gamePassword);
        return player.rest.sendPostRequest(RestObject.BASE_URL + "/joinGame", Application.getJson(commanders, true));
    }
    public static Response leaveGame(String email, String gameId) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        player.rest().setHeader("gameId", gameId);
        return player.rest().sendDeleteRequest(RestObject.BASE_URL + "/leaveGame");
    }
    public static Response login(String email, String password) {
        Player player = new Player();
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/login");
    }
    public static Response commanderDamage(String commander, String gameId, String email, String enemyPlayer) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("email", email);
        player.rest().setHeader("enemyPlayer", enemyPlayer);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/commanderDamage/"+commander);
    }
    public static Response putCommanderDamage(String gameId, String gamePassword, String password, CommanderDamage damage) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("gamePassword", gamePassword);
        player.rest().setHeader("email", damage.getPlayer());
        player.rest().setHeader("password", password);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/commanderDamage", Application.getJson(damage, true));
    }
    public static Response putLife(String gameId, String gamePassword, String email, String password, int amount) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("gamePassword", gamePassword);
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/life/"+amount, "");
    }
    public static Response putPoison(String gameId, String gamePassword, String email, String password, int amount) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("gamePassword", gamePassword);
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/poison/"+amount, "");
    }
    public static Response putExperience(String gameId, String gamePassword, String email, String password, int amount) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("gamePassword", gamePassword);
        player.rest().setHeader("email", email);
        player.rest().setHeader("password", password);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/experience/"+amount, "");
    }
    public static Response player(String gameId, String email) {
        Player player = new Player();
        player.rest().setHeader("gameId", gameId);
        player.rest().setHeader("email", email);
        return player.rest().sendGetRequest(RestObject.BASE_URL + "/player");
    }
    public RestObject rest() {
        return rest;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    public int getPoison() {
        return poison;
    }

    public void setPoison(int poison) {
        this.poison = poison;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public HashMap<String, HashMap<String, Integer>> getCommanderDamage() {
        return commanderDamage;
    }

    public void setCommanderDamage(HashMap<String, HashMap<String, Integer>> commanderDamage) {
        this.commanderDamage = commanderDamage;
    }
}
