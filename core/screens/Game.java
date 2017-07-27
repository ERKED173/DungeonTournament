package ru.erked.dt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

import ru.erked.dt.StartDT;
import ru.erked.dt.systems.DTButton;
import ru.erked.dt.utilities.Enemy;
import ru.erked.dt.utilities.Obfuscation;
import ru.erked.dt.utilities.Subject;

public class Game implements Screen, GestureDetector.GestureListener {

    private StartDT game;
    private Stage stage;
    private Sprite background, moneyBag;
    private Subject hero, lattice, spear, bow, stick, arrow, magic;
    private Obfuscation obf;
    private Subject[][] leftSpec, rightSpec;
    private RandomXS128 rand;
    private DTButton toPrison, toMenu;
    private boolean isGameIsOn, addedAction = false, isAttacking = false, nextToPrison = false, nextToMenu = false;
    private float heroTimer = 0f, hurtTimer = 2f, weaponX = 0f, weaponY = 0f;
    private Sprite[] healthBoard;
    private Sprite[] xpBoard;
    private ArrayList<Enemy> enemies;

    public Game(StartDT game, boolean isGameIsOn){
        this.game = game;
        this.isGameIsOn = isGameIsOn;
    }

    private void leftSpecInit(){
        leftSpec = new Subject[8][3];
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                leftSpec[i][j] = null;
            }
        }
        for(int i = 0; i < rand.nextInt(8) + 16; ++i){
            int number = rand.nextInt(4) + 1;
            switch (number){
                case 1:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject goblin = new Subject(
                            game.atlas.createSprite("goblin"),
                            posX * 0.07f * stage.getWidth() + 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX * 0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    leftSpec[posY][posX] = goblin;
                }
                case 2:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject skeleton = new Subject(
                            game.atlas.createSprite("skeleton"),
                            posX * 0.07f * stage.getWidth() + 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX * 0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    leftSpec[posY][posX] = skeleton;
                }
                case 3:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject wizard = new Subject(
                            game.atlas.createSprite("wizard"),
                            posX * 0.07f * stage.getWidth() + 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX * 0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    leftSpec[posY][posX] = wizard;
                }
                case 4:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject demon = new Subject(
                            game.atlas.createSprite("demon"),
                            posX * 0.07f * stage.getWidth() + 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX * 0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    leftSpec[posY][posX] = demon;
                }
            }
        }

        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(leftSpec[i][j] != null && rand.nextInt() % 3 == 0){
                    float time = rand.nextInt(5) + 2;
                    float angle = rand.nextInt(2) > 0 ? 360f : -360f;

                    leftSpec[i][j].setY(leftSpec[i][j].getY() + 0.035f * stage.getWidth());

                    MoveToAction moveUp = new MoveToAction();
                    moveUp.setDuration(0.25f);
                    moveUp.setPosition(leftSpec[i][j].getX(), leftSpec[i][j].getY() + 0.035f * stage.getWidth());

                    MoveToAction moveDown = new MoveToAction();
                    moveDown.setDuration(0.25f);
                    moveDown.setPosition(leftSpec[i][j].getX(), leftSpec[i][j].getY() - 0.035f * stage.getWidth());

                    SequenceAction sequenceMove = new SequenceAction();
                    sequenceMove.addAction(moveUp);
                    sequenceMove.addAction(moveDown);
                    sequenceMove.addAction(Actions.delay(time));

                    SequenceAction sequenceRotate = new SequenceAction();
                    sequenceRotate.addAction(Actions.rotateBy(angle, 0.5f));
                    sequenceRotate.addAction(Actions.rotateTo(0f));
                    sequenceRotate.addAction(Actions.delay(time));

                    ParallelAction parallel = new ParallelAction();
                    parallel.addAction(sequenceMove);
                    parallel.addAction(sequenceRotate);

                    RepeatAction repeat = new RepeatAction();
                    repeat.setCount(RepeatAction.FOREVER);
                    repeat.setAction(parallel);

                    leftSpec[i][j].addAction(repeat);
                }
            }
        }

        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(leftSpec[i][j] != null){
                    stage.addActor(leftSpec[i][j]);
                }
            }
        }
    }
    private void rightSpecInit(){
        rightSpec = new Subject[8][3];
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                rightSpec[i][j] = null;
            }
        }
        for(int i = 0; i < rand.nextInt(8) + 16; ++i){
            int number = rand.nextInt(4) + 1;
            switch (number){
                case 1:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject goblin = new Subject(
                            game.atlas.createSprite("goblin"),
                            stage.getWidth() - (posX + 1) * 0.07f * stage.getWidth() - 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX*0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    goblin.flip();
                    rightSpec[posY][posX] = goblin;
                }
                case 2:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject skeleton = new Subject(
                            game.atlas.createSprite("skeleton"),
                            stage.getWidth() - (posX + 1) * 0.07f * stage.getWidth() - 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX*0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    skeleton.flip();
                    rightSpec[posY][posX] = skeleton;
                }
                case 3:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject wizard = new Subject(
                            game.atlas.createSprite("wizard"),
                            stage.getWidth() - (posX + 1) * 0.07f * stage.getWidth() - 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX*0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    wizard.flip();
                    rightSpec[posY][posX] = wizard;
                }
                case 4:{
                    int posX = rand.nextInt(3), posY = rand.nextInt(8);
                    Subject demon = new Subject(
                            game.atlas.createSprite("demon"),
                            stage.getWidth() - (posX + 1) * 0.07f * stage.getWidth() - 0.01f * stage.getWidth(),
                            posY * 0.1f * stage.getWidth() + 0.375f * stage.getHeight() - posX*0.035f * stage.getWidth(),
                            0.07f * stage.getWidth(),
                            0.07f * stage.getWidth()
                    );
                    demon.flip();
                    rightSpec[posY][posX] = demon;
                }
            }
        }

        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(rightSpec[i][j] != null && rand.nextInt() % 3 == 0){
                    if(rand.nextInt(2) > 0) {
                        float time = rand.nextInt(5) + 2;
                        float angle = rand.nextInt(2) > 0 ? 360f : -360f;

                        rightSpec[i][j].setY(rightSpec[i][j].getY() + 0.035f * stage.getWidth());

                        MoveToAction moveUp = new MoveToAction();
                        moveUp.setDuration(0.25f);
                        moveUp.setPosition(rightSpec[i][j].getX(), rightSpec[i][j].getY() + 0.035f * stage.getWidth());

                        MoveToAction moveDown = new MoveToAction();
                        moveDown.setDuration(0.25f);
                        moveDown.setPosition(rightSpec[i][j].getX(), rightSpec[i][j].getY() - 0.035f * stage.getWidth());

                        SequenceAction sequenceMove = new SequenceAction();
                        sequenceMove.addAction(moveUp);
                        sequenceMove.addAction(moveDown);
                        sequenceMove.addAction(Actions.delay(time));

                        SequenceAction sequenceRotate = new SequenceAction();
                        sequenceRotate.addAction(Actions.rotateBy(angle, 0.5f));
                        sequenceRotate.addAction(Actions.rotateTo(0f));
                        sequenceRotate.addAction(Actions.delay(time));

                        ParallelAction parallel = new ParallelAction();
                        parallel.addAction(sequenceMove);
                        parallel.addAction(sequenceRotate);

                        RepeatAction repeat = new RepeatAction();
                        repeat.setCount(RepeatAction.FOREVER);
                        repeat.setAction(parallel);

                        rightSpec[i][j].addAction(repeat);
                    } else {
                        float time = rand.nextInt(5) + 2;

                        rightSpec[i][j].setY(rightSpec[i][j].getY() + 0.035f * stage.getWidth());

                        MoveToAction moveUp = new MoveToAction();
                        moveUp.setDuration(0.25f);
                        moveUp.setPosition(rightSpec[i][j].getX(), rightSpec[i][j].getY() + 0.035f * stage.getWidth());

                        MoveToAction moveDown = new MoveToAction();
                        moveDown.setDuration(0.25f);
                        moveDown.setPosition(rightSpec[i][j].getX(), rightSpec[i][j].getY() - 0.035f * stage.getWidth());

                        SequenceAction sequenceMove = new SequenceAction();
                        sequenceMove.addAction(moveUp);
                        sequenceMove.addAction(moveDown);
                        sequenceMove.addAction(Actions.delay(time));

                        RepeatAction repeat = new RepeatAction();
                        repeat.setCount(RepeatAction.FOREVER);
                        repeat.setAction(sequenceMove);

                        rightSpec[i][j].addAction(repeat);
                    }
                }
            }
        }

        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(rightSpec[i][j] != null){
                    stage.addActor(rightSpec[i][j]);
                }
            }
        }
    }
    private void buttonInit(){
        toPrison = new DTButton(
                game,
                0.025f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_prison"),
                1,
                "to_prison"
        );
        toPrison.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive()) {
                    StartDT.clickSound.play();
                    nextToPrison = true;
                } else {
                    toPrison.get().setChecked(false);
                }
            }
        });
        toMenu = new DTButton(
                game,
                stage.getWidth() - 0.325f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_menu"),
                1,
                "to_menu"
        );
        toMenu.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive()) {
                    StartDT.clickSound.play();
                    nextToMenu = true;
                } else {
                    toMenu.get().setChecked(false);
                }
            }
        });
    }
    private void obfuscate(float delta){
        if(obf.isActive() && !(nextToPrison || nextToMenu)){
            obf.deactivate(0.5f, delta);
        } else if(nextToPrison){
            if(obf.isActive()){
                game.setScreen(new Prison(game));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        } else if(nextToMenu){
            if(obf.isActive()){
                game.setScreen(new Menu(game));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        }
    }
    private void gameIsOn(float delta){
        hero.setVisible(true);
        spear.setVisible(false);
        bow.setVisible(false);
        stick.setVisible(false);

        healthBoard[1].setSize((0.25f * stage.getWidth()) / StartDT.player.getHpAll() * StartDT.player.getHp(), healthBoard[1].getHeight());
        healthBoard[0].draw(stage.getBatch());
        healthBoard[1].draw(stage.getBatch());
        xpBoard[2].setSize((0.25f * stage.getWidth()) / StartDT.player.getXpAll() * StartDT.player.getXp(), xpBoard[2].getHeight());
        xpBoard[0].draw(stage.getBatch());
        xpBoard[1].draw(stage.getBatch());
        xpBoard[2].draw(stage.getBatch());
        moneyBag.draw(stage.getBatch());

        game.SFontU.draw(
                stage.getBatch(),
                StartDT.player.getHp() + "/" + StartDT.player.getHpAll(),
                healthBoard[0].getX() + 0.5f * (healthBoard[0].getWidth() - game.SFontU.getWidth(StartDT.player.getHp() + "/" + StartDT.player.getHpAll())),
                healthBoard[0].getY() + 0.5f * (healthBoard[0].getHeight() + game.SFontU.getHeight("A"))
        );
        game.SFontU.draw(
                stage.getBatch(),
                StartDT.player.getXpLevel() + "",
                xpBoard[1].getX() + 0.5f * (xpBoard[1].getWidth() - game.SFontU.getWidth(StartDT.player.getXpLevel() + "")),
                xpBoard[1].getY() + 0.5f * (xpBoard[1].getHeight() + game.SFontU.getHeight("A"))
        );
        game.SFontU.draw(
                stage.getBatch(),
                StartDT.player.getXp() + "/" + StartDT.player.getXpAll(),
                xpBoard[0].getX() + 0.5f * (xpBoard[0].getWidth() - game.SFontU.getWidth(StartDT.player.getXp() + "/" + StartDT.player.getXpAll())),
                xpBoard[0].getY() + 0.5f * (xpBoard[0].getHeight() + game.SFontU.getHeight("A"))
        );
        game.SFontU.draw(
                stage.getBatch(),
                ": " + StartDT.player.getMoney(),
                moneyBag.getX() + 1.05f * moneyBag.getWidth(),
                moneyBag.getY() + 0.5f * (moneyBag.getHeight() + game.SFontU.getHeight("A"))
        );

        if(heroTimer > 3f){
            if(isAttacking && StartDT.player.getHp() > 0 && enemies.size() != 0) attack();
            hero.setSprite(game.atlas.createSprite("hero_back", 1));
            spear.setVisible(true);
            bow.setVisible(true);
            stick.setVisible(true);
            for(int i = 0; i < enemies.size(); ++i){
                enemies.get(i).setVisible(true);
                enemies.get(i).setTimer( enemies.get(i).getTimer() + delta );
                if(!enemies.get(i).hasActions()) enemies.get(i).move();
                if(enemies.get(i).getTimer() > 0.25f){
                    enemies.get(i).changeSprite();
                    enemies.get(i).setTimer(0f);
                }
                if(enemies.get(i).getY() <= hero.getY() + hero.getHeight()){
                    if(hurtTimer <= 0f) {
                        hurtTimer = 2f;
                        StartDT.player.hurt(enemies.get(i).getDamage());
                        hero.addAction(Actions.sequence(
                                Actions.color(Color.RED),
                                Actions.color(Color.WHITE, 1f)
                        ));
                    }
                    enemies.get(i).hurt(enemies.get(i).getHp());
                }
            }
        } else {
            heroTimer += delta;
        }

        if(!addedAction && !obf.isActive()){
            addedAction = true;
            float angle = rand.nextInt(90);
            float rangeX = lattice.getX() - rand.nextFloat() * 0.25f * stage.getWidth();
            float rangeY = lattice.getY() - rand.nextFloat() * 0.25f * stage.getWidth();
            if(rand.nextInt(2) > 0){ rangeX = lattice.getX() + rand.nextFloat() * 0.25f * stage.getWidth(); }

            ParallelAction action = new ParallelAction();
            action.addAction(Actions.rotateBy(angle, 0.5f));
            action.addAction(Actions.moveTo(rangeX, rangeY, 0.5f));
            action.addAction(Actions.fadeOut(1.5f));

            lattice.addAction(action);
            StartDT.hit.play(0.5f, rand.nextFloat() / 1.25f + 0.2f, 0);
        }

        toPrison.get().setVisible(false);
        toMenu.get().setVisible(false);
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(leftSpec[i][j] != null){
                    leftSpec[i][j].setVisible(true);
                }
                if(rightSpec[i][j] != null){
                    rightSpec[i][j].setVisible(true);
                }
            }
        }

        if(StartDT.player.getHp() == 0){
            for(Enemy enemy : enemies) enemy.setSpeed(0f);
            hero.setRotation(270f);
            hero.setPosition(0.5f * (stage.getWidth() - hero.getHeight()), hero.getY());
            spear.setVisible(false);
            bow.setVisible(false);
            stick.setVisible(false);
            if(!obf.isActive()){
                obf.activate(5f, delta);
            } else {
                StartDT.player.setHp(StartDT.player.getHpAll());
                StartDT.inGameMusic.stop();
                game.setScreen(new Prison(game));
                dispose();
            }
        }

        if(enemies.size() == 0){
            hero.setSprite(game.atlas.createSprite("hero", 3));
            spear.setVisible(false);
            bow.setVisible(false);
            stick.setVisible(false);
            if(!obf.isActive()){
                obf.activate(5f, delta);
            } else {
                StartDT.player.setHp(StartDT.player.getHpAll());
                StartDT.inGameMusic.stop();
                game.setScreen(new Prison(game));
                dispose();
            }
        }
    }
    private void gameIsStop(){
        for(int i = 0; i < 8; ++i){
            for(int j = 0; j < 3; ++j){
                if(leftSpec[i][j] != null){
                    leftSpec[i][j].setVisible(false);
                }
                if(rightSpec[i][j] != null){
                    rightSpec[i][j].setVisible(false);
                }
            }
        }
        spear.setVisible(false);
        bow.setVisible(false);
        stick.setVisible(false);
        hero.setVisible(false);
        toPrison.get().setVisible(true);
        toMenu.get().setVisible(true);
    }
    private void weaponInit(){
        arrow = new Subject(
                game.atlas.createSprite("arrow"),
                -10000f,
                -10000f,
                0.025f * stage.getHeight(),
                0.05f * stage.getHeight()
        );
        arrow.setOrigin(arrow.getX() + 0.5f * arrow.getWidth(), arrow.getY() + 0.5f * arrow.getHeight());
        magic = new Subject(
                game.atlas.createSprite("magic"),
                -10000f,
                -10000f,
                0.025f * stage.getHeight(),
                0.05f * stage.getHeight()
        );
        magic.setOrigin(magic.getX() + 0.5f * magic.getWidth(), magic.getY() + 0.5f * magic.getHeight());
        spear = new Subject(
                game.atlas.createSprite("spear", StartDT.player.getWeapon().getId()),
                hero.getX() + 0.0025f * stage.getWidth(),
                hero.getY() + 0.25f * hero.getHeight(),
                0.3575f * hero.getHeight(),
                0.715f * hero.getHeight()
        );
        spear.setOrigin(spear.getX() + 0.5f * spear.getWidth(), spear.getY() + 0.5f * spear.getHeight());
        bow = new Subject(
                game.atlas.createSprite("bow", StartDT.player.getWeapon().getId()),
                hero.getX() - 0.03f * stage.getWidth(),
                hero.getY() + 0.35f * hero.getHeight(),
                0.1f * stage.getWidth(),
                0.2f * stage.getWidth()
        );
        stick = new Subject(
                game.atlas.createSprite("stick", StartDT.player.getWeapon().getId()),
                hero.getX() - 0.03f * stage.getWidth(),
                hero.getY() + 0.35f * hero.getHeight(),
                0.1f * stage.getWidth(),
                0.2f * stage.getWidth()
        );
        stage.addActor(arrow);
        stage.addActor(magic);
    }
    private void mapObjInit(){
        hero = new Subject(
                game.atlas.createSprite("hero", 3),
                0.425f * stage.getWidth(),
                0.1875f * stage.getHeight(),
                0.15f * stage.getWidth(),
                0.3f * stage.getWidth()
        );
        lattice = new Subject(
                game.atlas.createSprite("lattice"),
                0.5f * stage.getWidth() - stage.getWidth() / 8.8f,
                stage.getHeight() - 0.2875f * stage.getHeight(),
                stage.getWidth() / 4.4f,
                stage.getHeight() / 16f
        );
        stage.addActor(lattice);
        for(int i = 0; i < 16; ++i){
            if(rand.nextInt() % 3 == 0){
                int index = rand.nextInt(4) + 1;
                Subject skull = new Subject(
                        game.atlas.createSprite("skull", index),
                        i >= 8 ? 0.685f * stage.getWidth() : 0.235f * stage.getWidth(),
                        i >= 8 ? 0.275f * stage.getHeight() + (15 - i) * 0.08f * stage.getWidth() : 0.275f * stage.getHeight() + i * 0.08f * stage.getWidth(),
                        0.075f * stage.getWidth(),
                        0.075f * stage.getWidth()
                );
                int angle = rand.nextInt(2) > 0 ? rand.nextInt(30) : -rand.nextInt(30);
                skull.setRotation(angle);
                stage.addActor(skull);
            }
        }
    }
    private void attack(){
        switch (StartDT.player.getWeapon().getType() - 1){
            case 0:{
                if(!spear.hasActions()) {
                    float x = hero.getX() + 0.5f * hero.getWidth() - weaponX;
                    float y = weaponY - hero.getY() + 0.5f * hero.getHeight();
                    double rotation = Math.toDegrees(Math.atan(y / x));
                    if(rotation > 0) rotation -= 90f;
                    else rotation += 90f;
                    spear.addAction(Actions.rotateBy((float)(-rotation)));
                    spear.setPosition(
                            hero.getX() + 0.5f * (hero.getWidth() - 0.025f * stage.getHeight()),
                            hero.getY() + 0.5f * (hero.getHeight() - 0.05f * stage.getHeight())
                    );
                    spear.addAction(Actions.sequence(
                            Actions.moveTo(weaponX - 0.5f * spear.getWidth(), weaponY - 0.5f * spear.getHeight(), 0.125f),
                            Actions.moveTo(-100000f, -100000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                }
                isAttacking = false;
                break;
            }
            case 1:{
                if(!arrow.hasActions()) {
                    float x = hero.getX() + 0.5f * hero.getWidth() - weaponX;
                    float y = weaponY - hero.getY() + 0.5f * hero.getHeight();
                    double rotation = Math.toDegrees(Math.atan(y / x));
                    if(rotation > 0) rotation -= 90f;
                    else rotation += 90f;
                    arrow.addAction(Actions.rotateBy((float)(-rotation)));
                    arrow.setPosition(
                            hero.getX() + 0.5f * (hero.getWidth() - 0.025f * stage.getHeight()),
                            hero.getY() + 0.5f * (hero.getHeight() - 0.05f * stage.getHeight())
                    );
                    arrow.addAction(Actions.sequence(
                            Actions.moveTo(weaponX - 0.5f * arrow.getWidth(), weaponY - 0.5f * arrow.getHeight(), 0.125f),
                            Actions.moveTo(-10000f, -10000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                    StartDT.bowHit.play(0.5f, rand.nextFloat() / 3.33f + 0.7f, 0f);
                }
                isAttacking = false;
                break;
            }
            case 2:{
                if(!magic.hasActions()) {
                    float x = hero.getX() + 0.5f * hero.getWidth() - weaponX;
                    float y = weaponY - hero.getY() + 0.5f * hero.getHeight();
                    double rotation = Math.toDegrees(Math.atan(y / x));
                    if(rotation > 0) rotation -= 90f;
                    else rotation += 90f;
                    magic.addAction(Actions.rotateBy((float)(-rotation)));
                    magic.setPosition(
                            hero.getX() + 0.5f * (hero.getWidth() - 0.025f * stage.getHeight()),
                            hero.getY() + 0.5f * (hero.getHeight() - 0.05f * stage.getHeight())
                    );
                    magic.addAction(Actions.sequence(
                            Actions.moveTo(weaponX - 0.5f * magic.getWidth(), weaponY - 0.5f * magic.getHeight(), 0.125f),
                            Actions.moveTo(-10000f, -10000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                    StartDT.magicHit.play(0.5f, rand.nextFloat() / 3.33f + 0.7f, 0f);
                }
                isAttacking = false;
                break;
            }
            default:{
                break;
            }
        }
    }
    private void ui(){
        healthBoard = new Sprite[2];
        healthBoard[0] = game.atlas.createSprite("health", 1);
        healthBoard[0].setBounds(
                0.725f * stage.getWidth(),
                0.025f * stage.getWidth(),
                0.25f * stage.getWidth(),
                0.0625f * stage.getWidth()
        );
        healthBoard[1] = game.atlas.createSprite("health", 2);
        healthBoard[1].setBounds(
                0.725f * stage.getWidth(),
                0.025f * stage.getWidth(),
                (0.25f * stage.getWidth()) / StartDT.player.getHpAll() * StartDT.player.getHp(),
                0.0625f * stage.getWidth()
        );
        xpBoard = new Sprite[3];
        xpBoard[0] = game.atlas.createSprite("experience", 1);
        xpBoard[0].setBounds(
                0.1f * stage.getWidth(),
                0.025f * stage.getWidth(),
                0.25f * stage.getWidth(),
                0.0625f * stage.getWidth()
        );
        xpBoard[1] = game.atlas.createSprite("experience", 2);
        xpBoard[1].setBounds(
                0.025f * stage.getWidth(),
                0.025f * stage.getWidth(),
                0.0625f * stage.getWidth(),
                0.0625f * stage.getWidth()
        );
        xpBoard[2] = game.atlas.createSprite("experience", 3);
        xpBoard[2].setBounds(
                0.1f * stage.getWidth(),
                0.025f * stage.getWidth(),
                (0.25f * stage.getWidth()) / StartDT.player.getXpAll() * StartDT.player.getXp(),
                0.0625f * stage.getWidth()
        );
        moneyBag = game.atlas.createSprite("money_bag");
        moneyBag.setBounds(
                0.025f * stage.getWidth(),
                0.1f * stage.getWidth(),
                0.0625f * stage.getWidth(),
                0.0625f * stage.getWidth()
        );
    }
    private void spawnGoblin(int i){
        enemies.add(new Enemy(
                game.atlas.createSprite("gobelen"),
                10.5f * stage.getWidth(),
                10.75f * stage.getHeight(),
                0.00125f * stage.getWidth(),
                0.0025f * stage.getWidth(),
                1f,
                0.0005f * stage.getWidth(),
                1,
                1,
                rand.nextFloat()
        ));
        enemies.get(i).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                weaponX = event.getTarget().getX() + 0.5f * 0.125f * stage.getWidth();
                weaponY = event.getTarget().getY() + 0.25f * 0.125f * stage.getWidth();
                isAttacking = true;
            }
        });
        enemies.get(i).addAction(
                Actions.sequence(
                        Actions.delay(3 + i * (2 - (StartDT.player.getXpLevel() / 8f))),
                        Actions.moveTo(0.5f * stage.getWidth(), 0.75f * stage.getHeight()),
                        Actions.parallel(
                                Actions.moveTo(0.285f*stage.getWidth() + rand.nextInt(3)*0.15f*stage.getWidth(), 0.675f * stage.getHeight(), 0.5f),
                                Actions.sizeBy(0.125f * stage.getWidth(), 0.25f * stage.getWidth(), 0.5f)
                        )
                )

        );
        if(!isGameIsOn || heroTimer < 3f) enemies.get(i).setVisible(false);
    }
    private void spawnSkeleton(int i){
        enemies.add(new Enemy(
                game.atlas.createSprite("skelet"),
                10.5f * stage.getWidth(),
                10.75f * stage.getHeight(),
                0.00125f * stage.getWidth(),
                0.0025f * stage.getWidth(),
                2f,
                0.0004f * stage.getWidth(),
                1,
                1,
                rand.nextFloat()
        ));
        enemies.get(i).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                weaponX = event.getTarget().getX() + 0.5f * 0.125f * stage.getWidth();
                weaponY = event.getTarget().getY() + 0.25f * 0.125f * stage.getWidth();
                isAttacking = true;
            }
        });
        enemies.get(i).addAction(
                Actions.sequence(
                        Actions.delay(3 + i * (2 - (StartDT.player.getXpLevel() / 8f))),
                        Actions.moveTo(0.5f * stage.getWidth(), 0.75f * stage.getHeight()),
                        Actions.parallel(
                                Actions.moveTo(0.285f*stage.getWidth() + rand.nextInt(3)*0.15f*stage.getWidth(), 0.675f * stage.getHeight(), 0.5f),
                                Actions.sizeBy(0.125f * stage.getWidth(), 0.25f * stage.getWidth(), 0.5f)
                        )
                )

        );
        if(!isGameIsOn || heroTimer < 3f) enemies.get(i).setVisible(false);
    }
    private void spawnWizard(int i){
        enemies.add(new Enemy(
                game.atlas.createSprite("magician"),
                10.5f * stage.getWidth(),
                10.75f * stage.getHeight(),
                0.00125f * stage.getWidth(),
                0.0025f * stage.getWidth(),
                3f,
                0.0004f * stage.getWidth(),
                2,
                2,
                rand.nextFloat()
        ));
        enemies.get(i).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                weaponX = event.getTarget().getX() + 0.5f * 0.125f * stage.getWidth();
                weaponY = event.getTarget().getY() + 0.25f * 0.125f * stage.getWidth();
                isAttacking = true;
            }
        });
        enemies.get(i).addAction(
                Actions.sequence(
                        Actions.delay(3 + i * (2 - (StartDT.player.getXpLevel() / 8f))),
                        Actions.moveTo(0.5f * stage.getWidth(), 0.75f * stage.getHeight()),
                        Actions.parallel(
                                Actions.moveTo(0.285f*stage.getWidth() + rand.nextInt(3)*0.15f*stage.getWidth(), 0.675f * stage.getHeight(), 0.5f),
                                Actions.sizeBy(0.125f * stage.getWidth(), 0.25f * stage.getWidth(), 0.5f)
                        )
                )

        );
        if(!isGameIsOn || heroTimer < 3f) enemies.get(i).setVisible(false);
    }
    private void spawnDemon(int i){
        enemies.add(new Enemy(
                game.atlas.createSprite("devil"),
                10.5f * stage.getWidth(),
                10.75f * stage.getHeight(),
                0.0015f * stage.getWidth(),
                0.003f * stage.getWidth(),
                4f,
                0.0003f * stage.getWidth(),
                2,
                3,
                rand.nextFloat()
        ));
        enemies.get(i).addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                weaponX = event.getTarget().getX() + 0.5f * 0.15f * stage.getWidth();
                weaponY = event.getTarget().getY() + 0.25f * 0.15f * stage.getWidth();
                isAttacking = true;
            }
        });
        enemies.get(i).addAction(
                Actions.sequence(
                        Actions.delay(3 + i * (2 - (StartDT.player.getXpLevel() / 8f))),
                        Actions.moveTo(0.5f * stage.getWidth(), 0.75f * stage.getHeight()),
                        Actions.parallel(
                                Actions.moveTo(0.285f*stage.getWidth() + rand.nextInt(3)*0.15f*stage.getWidth(), 0.675f * stage.getHeight(), 0.5f),
                                Actions.sizeBy(0.15f * stage.getWidth(), 0.3f * stage.getWidth(), 0.5f)
                        )
                )

        );
        if(!isGameIsOn || heroTimer < 3f) enemies.get(i).setVisible(false);
    }
    private void collision(){
        for(int i = 0; i < enemies.size(); ++i){
            float eX = enemies.get(i).getX(), wX, pX = hero.getX();
            float eY = enemies.get(i).getY(), wY, pY = hero.getY();
            float eW = enemies.get(i).getWidth(), wW, pW = hero.getWidth();
            float eH = enemies.get(i).getHeight(), wH, pH = hero.getHeight();
            boolean condHero1 = pX < eX + eW;
            boolean condHero2 = pX + pW > eX;
            boolean condHero3 = pY < eY + eH;
            boolean condHero4 = pY + pH > eY;
            if(condHero1 && condHero2 && condHero3 && condHero4 && enemies.get(i).isAlive() && hurtTimer <= 0f && !hero.hasActions()){
                StartDT.player.hurt(enemies.get(i).getDamage());
                hero.addAction(Actions.sequence(
                        Actions.color(Color.RED),
                        Actions.color(Color.WHITE, 1f)
                ));
            } else {
                switch (StartDT.player.getWeapon().getType()){
                    case 1:{
                        wX = spear.getX();
                        wY = spear.getY();
                        wW = spear.getWidth();
                        wH = spear.getHeight();
                        break;
                    }
                    case 2:{
                        wX = arrow.getX();
                        wY = arrow.getY();
                        wW = arrow.getWidth();
                        wH = arrow.getHeight();
                        break;
                    }
                    case 3:{
                        wX = magic.getX();
                        wY = magic.getY();
                        wW = magic.getWidth();
                        wH = magic.getHeight();
                        break;
                    }
                    default:{
                        wX = 0;
                        wY = 0;
                        wW = 0;
                        wH = 0;
                    }
                }
                boolean cond1 = wX < eX + eW;
                boolean cond2 = wX + wW > eX;
                boolean cond3 = wY < eY + eH;
                boolean cond4 = wY + wH > eY;
                if (cond1 && cond2 && cond3 && cond4 && enemies.get(i).isAlive()) {
                    spear.clearActions();
                    spear.addAction(Actions.sequence(
                            Actions.moveTo(-10000f, -10000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                    arrow.clearActions();
                    arrow.addAction(Actions.sequence(
                            Actions.moveTo(-10000f, -10000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                    magic.clearActions();
                    magic.addAction(Actions.sequence(
                            Actions.moveTo(-10000f, -10000f),
                            Actions.rotateTo(0f),
                            Actions.delay(StartDT.player.getWeapon().getDelay())
                    ));
                    enemies.get(i).hurt(StartDT.player.getWeapon().getDamage());
                    if (!enemies.get(i).isAlive()) {
                        addReward(enemies.get(i).getX(), enemies.get(i).getY(), enemies.get(i).getWidth(), enemies.get(i).getMinReward());
                    }
                }
            }
        }
        int size = enemies.size(), i = 0;
        while (i < size){
            if(!enemies.get(i).isAlive()){
                enemies.remove(i);
                i = 0;
                size--;
            } else {
                i++;
            }
        }
    }
    private void addReward(float x, float y, float width, int minReward){
        int xpAmount = rand.nextInt(2) + minReward;
        int moneyAmount = rand.nextInt(2) + minReward;
        for(int i = 0; i < xpAmount; ++i){
            float side = 0.25f * rand.nextFloat() * width + 0.1f * width;
            Subject xp = new Subject(
                    game.atlas.createSprite("xp"),
                    x + 0.5f * rand.nextFloat() * width + 0.1f * width,
                    y + 0.5f * rand.nextFloat() * width + 0.1f * width,
                    side,
                    side
            );
            xp.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveTo(xpBoard[2].getX() + xpBoard[2].getWidth(), xpBoard[2].getY() + 0.5f * xpBoard[2].getHeight(), 0.5f),
                            Actions.rotateTo(360f, 0.5f)
                    ),
                    Actions.removeActor()
                    )
            );
            stage.addActor(xp);
        }
        for(int i = 0; i < moneyAmount; ++i){
            float side = 0.25f * rand.nextFloat() * width + 0.1f * width;
            Subject money = new Subject(
                    game.atlas.createSprite("money"),
                    x + 0.5f * rand.nextFloat() * width + 0.1f * width,
                    y + 0.5f * rand.nextFloat() * width + 0.1f * width,
                    side,
                    side
            );
            money.addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.moveTo(moneyBag.getX() + 1.05f * moneyBag.getWidth(), moneyBag.getY() + 0.5f * (moneyBag.getHeight() + game.SFontU.getHeight("A")), 0.5f),
                            Actions.rotateTo(360f, 0.5f)
                    ),
                    Actions.removeActor()
                    )
            );
            stage.addActor(money);
        }
        StartDT.player.addMoney(moneyAmount);
        StartDT.player.addXp(xpAmount);
    }

    @Override
    public void show() {
        stage = new Stage();
        rand = new RandomXS128();
        enemies = new ArrayList<>();

        if(!isGameIsOn) StartDT.mainMusic.play();
        else StartDT.inGameMusic.play();

        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(stage);
        inputMultiplexer.addProcessor(new GestureDetector(this));
        Gdx.input.setInputProcessor(inputMultiplexer);

        obf = new Obfuscation(game.atlas.createSprite("obfuscation"), true);
        background = game.atlas.createSprite("arena");
        background.setBounds(0f, 0f, stage.getWidth(), stage.getHeight());

        mapObjInit();
        weaponInit();

        switch (StartDT.player.getWeapon().getType() - 1){
            case 0:{
                stage.addActor(spear);
                break;
            }
            case 1:{
                stage.addActor(bow);
                break;
            }
            case 2:{
                stage.addActor(stick);
                break;
            }
        }
        stage.addActor(hero);

        ui();
        leftSpecInit();
        rightSpecInit();
        buttonInit();

        int kind, plLevel = StartDT.player.getXpLevel();
        for(int i = 0; i < rand.nextInt(10) + 5 * (StartDT.player.getXpLevel() + 1); ++i){
            if(plLevel >= 8){
                kind = rand.nextInt(4) + 1;
            } else if(plLevel >= 5){
                kind = rand.nextInt(3) + 1;
            } else if(plLevel >= 2){
                kind = rand.nextInt(2) + 1;
            } else {
                kind = 1;
            }

            switch (kind){
                case 1:{
                    spawnGoblin(i);
                    break;
                }
                case 2:{
                    spawnSkeleton(i);
                    break;
                }
                case 3:{
                    spawnWizard(i);
                    break;
                }
                case 4:{
                    spawnDemon(i);
                    break;
                }
                default:{
                    break;
                }
            }
        }
        for(int i = enemies.size() - 1; i >= 0; --i) { stage.addActor(enemies.get(i)); }

        stage.addActor(toMenu.get());
        stage.addActor(toPrison.get());
        stage.addActor(obf);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        obfuscate(delta);
        if(hurtTimer - delta <= 0f){
            hurtTimer = 0f;
        } else {
            hurtTimer -= delta;
        }
        collision();

        stage.getBatch().begin();
        background.draw(stage.getBatch());
        if(isGameIsOn) {
            gameIsOn(delta);
        } else {
            gameIsStop();
        }
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        stage.getBatch().begin();
        if((StartDT.player.getHp() == 0 && !obf.isActive()) || enemies.size() == 0) {
            game.XLFontS.draw(
                    stage.getBatch(),
                    game.textSystem.get("game_over"),
                    0.5f * (stage.getWidth() - game.XLFontS.getWidth(game.textSystem.get("game_over"))),
                    0.5f * (stage.getHeight() + game.XLFontS.getHeight("A"))
            );
        }
        stage.getBatch().end();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            StartDT.mainMusic.stop();
            StartDT.inGameMusic.stop();
            game.setScreen(new Menu(game));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
            StartDT.mainMusic.stop();
            StartDT.inGameMusic.stop();
            dispose();
            Gdx.app.exit();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        StartDT.inGameMusic.stop();
        StartDT.mainMusic.stop();
        StartDT.player.saveStats();
    }

    @Override
    public void resume() {
        game.setScreen(new Splash(game));
        dispose();
    }

    @Override
    public void hide() {
        StartDT.player.saveStats();
    }

    @Override
    public void dispose() {
        StartDT.player.saveStats();
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
