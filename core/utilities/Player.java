package ru.erked.dt.utilities;

import com.badlogic.gdx.math.RandomXS128;

import ru.erked.dt.StartDT;

public class Player {

    private int hp;
    private int xp;
    private int xpAll;
    private int xpLevel;
    private int money;
    private int hpAll;
    private Weapon weapon;
    private RandomXS128 rand;

    public Player() {
        rand = new RandomXS128();
    }

    public void saveStats() {
        StartDT.prefs.putInteger("hp", hpAll);
        StartDT.prefs.putInteger("xp", xp);
        StartDT.prefs.putInteger("xp_all", xpAll);
        StartDT.prefs.putInteger("xp_level", xpLevel);
        StartDT.prefs.putInteger("money", money);
        for(int i = 0; i < StartDT.weapons.length; ++i){ StartDT.prefs.putBoolean("w_own_" + i, StartDT.weaponsOwned[i]); }
        StartDT.prefs.flush();
    }

    public void loadStats() {
        hpAll = StartDT.prefs.getInteger("hp", 1);
        hp = hpAll;
        xp = StartDT.prefs.getInteger("xp", 0);
        xpAll = StartDT.prefs.getInteger("xp_all", 10);
        xpLevel = StartDT.prefs.getInteger("xp_level", 0);
        money = StartDT.prefs.getInteger("money", 0);
        for(int i = 0; i < StartDT.weapons.length; ++i){ StartDT.weaponsOwned[i] = StartDT.prefs.getBoolean("w_own_" + i, false); }
        for(int i = 0; i < StartDT.weapons.length; ++i){ StartDT.weapons[i].setOwned(StartDT.weaponsOwned[i]); }
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setHpAll(int hpAll) {
        this.hpAll = hpAll;
    }

    public int getHpAll() {
        return hpAll;
    }

    public void hurt(int damage){
        if(hp - damage > 0){
            hp -= damage;
            StartDT.hurt.play(1f, rand.nextFloat() / 3.33f + 0.7f, 0f);
        } else {
            hp = 0;
        }
    }

    public int getXp() {
        return xp;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void addXp(int xp) {
        if(this.xp + xp >= xpAll){
            this.xp = this.xp + xp - xpAll;
            xpLevel++;
            xpAll += xpLevel * 20;
        } else {
            this.xp += xp;
        }
    }

    public int getXpAll() {
        return xpAll;
    }

    public void setXpAll(int xpAll) {
        this.xpAll = xpAll;
    }

    public int getXpLevel() {
        return xpLevel;
    }

    public void setXpLevel(int xpLevel) {
        this.xpLevel = xpLevel;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void addMoney(int money) {
        this.money += money;
    }

    public boolean spendMoney(int money) {
        if(this.money - money < 0){
            return false;
        } else {
            this.money -= money;
            return true;
        }
    }
}
