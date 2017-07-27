package ru.erked.dt.utilities;

import ru.erked.dt.StartDT;

public class Weapon {

    private float damage;
    private float delay;
    private int type;
    private int price;
    private int playerXp;
    private boolean owned;
    private boolean selected;
    private int id;

    public Weapon(int id, float damage, float delay, int type, int price, int playerXp, boolean owned, boolean selected){
        this.damage = damage;
        this.delay = delay;
        this.type = type;
        this.price = price;
        this.playerXp = playerXp;
        this.owned = owned;
        this.selected = selected;
        this.id = id;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDelay() {
        return delay;
    }

    public void setDelay(float delay) {
        this.delay = delay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPlayerXp() {
        return playerXp;
    }

    public void setPlayerXp(int playerXp) {
        this.playerXp = playerXp;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        StartDT.weaponsOwned[(id - 1) * 3 + (type - 1)] = owned;
        this.owned = owned;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
