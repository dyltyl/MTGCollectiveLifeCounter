package com.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.HashMap;

public class Player {
    private String name, email, password;
    private int life, poison, experience;
    private HashMap<String, Integer> commanderDamage;

    public Player(String email, String password, String name, int life, int poison, int experience, HashMap<String, Integer> commanderDamage) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.life = life;
        this.poison = poison;
        this.experience = experience;
        this.commanderDamage = commanderDamage;
    }
    public Player() {

    }
    public String getName() {
        return this.name;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public int getLife() {
        return life;
    }
    public HashMap<String, Integer> getCommanderDamage() {
        return commanderDamage;
    }
    public int getPoison() {
        return poison;
    }
    public int getExperience() {
        return experience;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setLife(int life) {
        this.life = life;
    }
    public void setPoison(int poison) {
        this.poison = poison;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setCommanderDamage(HashMap<String, Integer> commanderDamage) {
        this.commanderDamage = commanderDamage;
    }
    public void addLife(int life) {
        this.life += life;
    }
    public void addPoison(int poison) {
        this.poison += poison;
    }
    public void addExperience(int experience) {
        this.experience += experience;
    }
    public void subtractLife(int life) {
        this.life -= life;
    }
    public void subtractPoison(int poison) {
        this.poison -= poison;
    }
    public void subtractExperience(int experience) {
        this.experience -= experience;
    }
    public static String convertToJson(Object object) {
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return mapper.writeValueAsString(object);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
