package ru.erked.dt.utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Enemy extends Subject {

    private float hp;
    private float speed;
    private float timer;
    private int damage;
    private int minReward;

    public Enemy(Sprite sprite, float x, float y, float width, float height, float hp, float speed, int damage, int minReward, float timer){
        super(sprite, x, y, width, height);
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.minReward = minReward;
        this.timer = timer;
    }

    public void changeSprite(){
        if(sprite.isFlipX()){
            sprite.setFlip(false, false);
        } else {
            sprite.setFlip(true, false);
        }
    }

    public float getHp() {
        return hp;
    }

    public void hurt(float damage) {
        if(hp - damage <= 0){
            hp = 0;
            setVisible(false);
        } else {
            addAction(Actions.sequence(
                    Actions.color(Color.RED),
                    Actions.color(Color.WHITE, 0.25f)
            ));
            hp -= damage;
        }
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isAlive(){
        return hp > 0;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void move(){
        setY(getY() - speed);
    }

    public int getMinReward() {
        return minReward;
    }

    public void setMinReward(int minXp) {
        this.minReward = minXp;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
}
