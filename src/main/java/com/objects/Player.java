package com.objects;

import java.util.ArrayList;

public class Player {
    private String name;
    private ArrayList<String> commanders;
    private int life, poison, experience;

    public Player(String name, ArrayList<String> commanders, int life, int poison, int experience) {
        this.name = name;
        this.commanders = commanders;
        this.life = life;
        this.poison = poison;
        this.experience = experience;
    }
    public Player() {

    }
    public String getName() {
        return this.name;
    }
    public ArrayList<String> getCommanders() {
        return this.commanders;
    }
    public int getLife() {
        return life;
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
    public void setCommanders(ArrayList<String> commanders) {
        this.commanders = commanders;
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
    public void addLife(int life) {
        this.life += life;
    }
    public void addPoison(int poison) {
        this.poison += poison;
    }
    public void addExperience(int experience) {
        this.experience += experience;
    }
    public void addCommander(String commander) {
        this.commanders.add(commander);
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
    public void removeCommander(int index) {
        this.commanders.remove(index);
    }
}
