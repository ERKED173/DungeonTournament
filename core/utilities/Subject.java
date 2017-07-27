package ru.erked.dt.utilities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Subject extends Actor {

    protected Sprite sprite;

    public Subject(Sprite sprite, float x, float y, float width, float height){
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        setOrigin(width / 2f, height / 2f);
        this.sprite = sprite;
        this.sprite.setBounds(x, y, width, height);
        this.sprite.setOrigin(width / 2f, height / 2f);
    }

    public void flip(){
        sprite.setFlip(true, false);
    }

    public void setSprite(Sprite sprite){
        this.sprite = sprite;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        sprite.setBounds(getX(), getY(), getWidth(), getHeight());
        sprite.setRotation(getRotation());
        sprite.setColor(getColor());
        sprite.draw(batch, parentAlpha);
    }

}
