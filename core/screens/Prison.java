package ru.erked.dt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import ru.erked.dt.StartDT;
import ru.erked.dt.systems.DTButton;
import ru.erked.dt.systems.DTGui;
import ru.erked.dt.utilities.Obfuscation;
import ru.erked.dt.utilities.Subject;

public class Prison implements Screen{

    private StartDT game;
    private Sprite background, moneyBag, hearth;
    private Sprite[] hero, xpBoard;
    private int iter = 0;
    private float time = 0f;
    private Stage stage;
    private DTButton toArena, toGame, toShop, weaponInfoButton;
    private boolean nextToArena = false, weaponMoved = false, nextToGame = false, nextToShop = false;
    private Obfuscation obf;
    private Subject weapon;
    private DTGui weaponInfo;

    public Prison(StartDT game){
        this.game = game;
    }

    private void buttonInit(){
        toArena = new DTButton(
                game,
                0.025f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_arena"),
                1,
                "to_arena"
        );
        toArena.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && !weaponInfo.isDisplayed()) {
                    StartDT.clickSound.play();
                    nextToArena = true;
                } else {
                    toArena.get().setChecked(false);
                }
            }
        });

        toShop = new DTButton(
                game,
                0.35f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_shop"),
                1,
                "to_shop"
        );
        toShop.get().setColor(Color.YELLOW);
        toShop.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && !weaponInfo.isDisplayed()) {
                    StartDT.clickSound.play();
                    nextToShop = true;
                } else {
                    toShop.get().setChecked(false);
                }
            }
        });

        toGame = new DTButton(
                game,
                0.675f * stage.getWidth(),
                0.05f * stage.getWidth(),
                0.3f * stage.getWidth(),
                game.MFontS.getFont(),
                game.textSystem.get("to_game"),
                1,
                "to_game"
        );
        toGame.get().setColor(Color.RED);
        toGame.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && !weaponInfo.isDisplayed()) {
                    StartDT.clickSound.play();
                    nextToGame = true;
                } else {
                    toGame.get().setChecked(false);
                }
            }
        });

        weaponInfoButton = new DTButton(
                game,
                0.825f * stage.getWidth(),
                stage.getHeight() - 0.175f * stage.getWidth(),
                0.15f * stage.getWidth(),
                game.LFontS.getFont(),
                "i",
                2,
                "weapon_info"
        );
        weaponInfoButton.get().addListener(new ClickListener(){
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(!obf.isActive() && !weaponInfo.isDisplayed()) {
                    String weapon;
                    switch (StartDT.player.getWeapon().getType()){
                        case 1:{ weapon = "spear_"; break; }
                        case 2:{ weapon = "bow_"; break; }
                        case 3:{ weapon = "stick_"; break; }
                        default: weapon = "null";
                    }
                    weaponInfo.setText(new String[]{
                            game.textSystem.get("n_" + weapon + (StartDT.player.getWeapon().getId())),
                            "",
                            "",
                            game.textSystem.get("damage_") + ": " + StartDT.player.getWeapon().getDamage(),
                            game.textSystem.get("delay") + ": " + StartDT.player.getWeapon().getDelay(),
                            "",
                            "",
                            game.textSystem.get("d_" + weapon + StartDT.player.getWeapon().getId() + "_1"),
                            game.textSystem.get("d_" + weapon + StartDT.player.getWeapon().getId() + "_2"),
                            game.textSystem.get("d_" + weapon + StartDT.player.getWeapon().getId() + "_3")
                    });
                    StartDT.clickSound.play();
                    weaponInfoButton.get().setChecked(false);
                    weaponInfo.setDisplayed(true);
                } else {
                    weaponInfoButton.get().setChecked(false);
                }
            }
        });
    }
    private void textDraw(){
        game.MFontU.draw(
                stage.getBatch(),
                StartDT.player.getXpLevel() + "",
                xpBoard[1].getX() + 0.5f * (xpBoard[1].getWidth() - game.MFontU.getWidth(StartDT.player.getXpLevel() + "")),
                xpBoard[1].getY() + 0.5f * (xpBoard[1].getHeight() + game.MFontU.getHeight("A"))
        );
        game.MFontU.draw(
                stage.getBatch(),
                StartDT.player.getXp() + "/" + StartDT.player.getXpAll(),
                xpBoard[0].getX() + 0.5f * (xpBoard[0].getWidth() - game.MFontU.getWidth(StartDT.player.getXp() + "/" + StartDT.player.getXpAll())),
                xpBoard[0].getY() + 0.5f * (xpBoard[0].getHeight() + game.MFontU.getHeight("A"))
        );
        game.MFontU.draw(
                stage.getBatch(),
                ": " + StartDT.player.getMoney(),
                moneyBag.getX() + 1.05f * moneyBag.getWidth(),
                moneyBag.getY() + 0.5f * (moneyBag.getHeight() + game.MFontU.getHeight("A"))
        );
        game.MFontU.draw(
                stage.getBatch(),
                ": " + StartDT.player.getHpAll(),
                hearth.getX() + 1.05f * hearth.getWidth(),
                hearth.getY() + 0.5f * (hearth.getHeight() + game.MFontU.getHeight("A"))
        );
    }
    private void obfuscate(float delta){
        if(obf.isActive() && !(nextToArena || nextToGame || nextToShop)){
            obf.deactivate(0.5f, delta);
        } else if(nextToArena){
            if(obf.isActive()){
                game.setScreen(new Game(game, false));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        } else if(nextToGame){
            if(obf.isActive()){
                StartDT.mainMusic.stop();
                game.setScreen(new Game(game, true));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        } else if(nextToShop){
            if(obf.isActive()){
                game.setScreen(new Shop(game));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        }
    }
    private void weaponInit(){
        String str;
        switch (StartDT.player.getWeapon().getType()){
            case 1:{ str = "spear"; break; }
            case 2:{ str = "bow"; break; }
            case 3:{ str = "stick"; break; }
            default: str = "null";
        }
        weapon = new Subject(
                game.atlas.createSprite(str, StartDT.player.getWeapon().getId()),
                0.825f * stage.getWidth(),
                stage.getHeight() - 0.5f * stage.getWidth(),
                0.15f * stage.getWidth(),
                0.3f * stage.getWidth()
        );
        stage.addActor(weapon);
    }
    private void guiInit(){
        weaponInfo = new DTGui(
                game,
                stage,
                0.05f * stage.getWidth(),
                0.5f * stage.getHeight() - 0.45f * stage.getWidth(),
                0.9f * stage.getWidth(),
                0.9f * stage.getWidth(),
                new String[] {
                        "",
                },
                1,
                game.MFontS
        );
        xpBoard = new Sprite[3];
        xpBoard[0] = game.atlas.createSprite("experience", 1);
        xpBoard[0].setBounds(
                0.15f * stage.getWidth(),
                stage.getHeight() - 0.125f * stage.getWidth(),
                0.4f * stage.getWidth(),
                0.1f * stage.getWidth()
        );
        xpBoard[1] = game.atlas.createSprite("experience", 2);
        xpBoard[1].setBounds(
                0.025f * stage.getWidth(),
                stage.getHeight() - 0.125f * stage.getWidth(),
                0.1f * stage.getWidth(),
                0.1f * stage.getWidth()
        );
        xpBoard[2] = game.atlas.createSprite("experience", 3);
        xpBoard[2].setBounds(
                0.15f * stage.getWidth(),
                stage.getHeight() - 0.125f * stage.getWidth(),
                (0.4f * stage.getWidth()) / StartDT.player.getXpAll() * StartDT.player.getXp(),
                0.1f * stage.getWidth()
        );
        moneyBag = game.atlas.createSprite("money_bag");
        moneyBag.setBounds(
                0.025f * stage.getWidth(),
                stage.getHeight() - 0.25f * stage.getWidth(),
                0.1f * stage.getWidth(),
                0.1f * stage.getWidth()
        );
        hearth = game.atlas.createSprite("hearth");
        hearth.setBounds(
                0.025f * stage.getWidth(),
                stage.getHeight() - 0.375f * stage.getWidth(),
                0.1f * stage.getWidth(),
                0.1f * stage.getWidth()
        );
    }
    private void isGuiEnabled(){
        if(!weaponMoved){
            weaponMoved = true;
            weapon.addAction(Actions.moveTo(
                    0.5f * stage.getWidth() + weapon.getWidth(),
                    0.5f * (stage.getHeight() - weapon.getHeight()),
                    0.25f
            ));
        }
    }
    private void isGuiDisabled(){
        weaponMoved = false;
        weapon.setPosition(
                0.825f * stage.getWidth(),
                stage.getHeight() - 0.5f * stage.getWidth()
        );
    }

    @Override
    public void show() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = game.atlas.createSprite("prison");
        background.setBounds(0f, 0f, stage.getWidth(), stage.getHeight());
        hero = new Sprite[4];
        for(int i = 0; i < 4; ++i) {
            hero[i] = game.atlas.createSprite("hero", i + 1);
            hero[i].setBounds(
                    0.3f * stage.getWidth(),
                    0.25f * stage.getHeight(),
                    0.4f * stage.getWidth(),
                    0.8f * stage.getWidth()
            );
        }

        guiInit();
        weaponInit();
        buttonInit();

        obf = new Obfuscation(game.atlas.createSprite("obfuscation"), true);

        stage.addActor(weaponInfoButton.get());
        stage.addActor(weaponInfo);
        stage.addActor(toArena.get());
        stage.addActor(toGame.get());
        stage.addActor(toShop.get());
        stage.addActor(obf);

        StartDT.mainMusic.play();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;
        if(time > 0.5f){
            time = 0f;
            if(iter == 3){
                iter = 0;
            } else {
                iter++;
            }
        }

        xpBoard[2].setSize((0.4f * stage.getWidth()) / StartDT.player.getXpAll() * StartDT.player.getXp(), xpBoard[2].getHeight());

        stage.getBatch().begin();
        background.draw(stage.getBatch());
        hero[iter].draw(stage.getBatch());
        xpBoard[0].draw(stage.getBatch());
        xpBoard[1].draw(stage.getBatch());
        xpBoard[2].draw(stage.getBatch());
        moneyBag.draw(stage.getBatch());
        hearth.draw(stage.getBatch());
        if(weaponInfo.isDisplayed()){
            isGuiEnabled();
        } else {
            isGuiDisabled();
        }
        textDraw();
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        obfuscate(delta);

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            StartDT.mainMusic.stop();
            game.setScreen(new Game(game, false));
            dispose();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
            StartDT.mainMusic.stop();
            dispose();
            Gdx.app.exit();
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
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
}
