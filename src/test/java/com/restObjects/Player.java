package com.restObjects;

import com.Application;

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
        player.rest.setHeader("email", currentEmail);
        return player.rest().sendPutRequest(RestObject.BASE_URL + "/player", Application.getJson(player, true));
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
