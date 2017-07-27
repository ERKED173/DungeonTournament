package ru.erked.dt.systems;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import ru.erked.dt.StartDT;

public class DTGui extends Actor {


    private boolean isDisplayed, closeClicked;
    private DTButton close;
    private Sprite sprite;
    private String[] text;
    private DTFont font;
    private float x, y, width, height;
    private int textType;

    public DTGui(StartDT game, Stage stage, float x, float y, float width, float height, String[] text, int textType, DTFont font){
        isDisplayed = false;
        closeClicked = false;
        this.setPosition(x, y);
        this.setSize(width, height);
        sprite = game.atlas.createSprite("gui");
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        close = new DTButton(
                game,
                x + 0.85f*width,
                y + height - 0.15f*width,
                0.1f*width,
                game.LFontU.getFont(),
                "X",
                2,
                "close_button_" + StartDT.closeIterator
        );
        StartDT.closeIterator++;
        close.get().addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                close.get().setChecked(false);
                StartDT.clickSound.play();
                setDisplayed(false);
                closeClicked = true;
            }
        });
        close.get().setVisible(false);
        this.setVisible(false);
        stage.addActor(this);
        stage.addActor(close.get());
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.textType = textType;
    }

    public boolean isCloseClicked(){
        boolean bool = closeClicked;
        closeClicked = false;
        return bool;
    }

    public void setDisplayed(boolean bool){
        isDisplayed = bool;
        close.get().setVisible(bool);
        this.setVisible(bool);
    }

    public void setText(String[] text){
        this.text = text;
    }

    public boolean isDisplayed(){
        return isDisplayed;
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        sprite.draw(batch);
        if(textType == 0) {
            for (int i = 0; i < text.length; ++i) {
                font.draw(
                        batch,
                        text[i],
                        x + 0.5f * (width - font.getWidth(text[i])),
                        y + (height - 0.15f * width) - (1.5f * i * font.getHeight("A"))
                );
            }
        } else if(textType == 1){
            for (int i = 0; i < text.length; ++i) {
                font.draw(
                        batch,
                        text[i],
                        x + 0.05f * width,
                        y + (height - 0.15f * width) - (1.5f * i * font.getHeight("A"))
                );
            }
        }
    }

}
