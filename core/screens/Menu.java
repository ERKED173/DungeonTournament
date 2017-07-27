package ru.erked.dt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

import ru.erked.dt.StartDT;
import ru.erked.dt.systems.DTButton;
import ru.erked.dt.systems.DTGui;
import ru.erked.dt.utilities.Obfuscation;

public class Menu implements Screen{

    private StartDT game;
    private Stage stage;
    private DTGui aboutGui;
    private float width, height;
    private Obfuscation obf;
    private boolean nextScreen = false;

    public Menu(StartDT game){
        this.game = game;
    }

    private void buttonsInitialization(){

        float width = stage.getWidth();
        float height = stage.getHeight();

        final DTButton[] buttons = new DTButton[3];

        buttons[0] = new DTButton(
                game,
                0.25f * width,
                0.5f * height + 0.15f*width,
                0.5f * width,
                game.LFontS.getFont(),
                game.textSystem.get("start_game"),
                1,
                "start_button"
        );
        buttons[0].get().addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if(!obf.isActive()) {
                    StartDT.clickSound.play();
                    nextScreen = true;
                } else {
                    buttons[0].get().setChecked(false);
                }
            }
        });

        buttons[1] = new DTButton(
                game,
                0.25f * width,
                0.5f * height - 0.125f*width,
                0.5f * width,
                game.XLFontS.getFont(),
                game.textSystem.get("about_game"),
                1,
                "about_button"
        );
        buttons[1].get().addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if(!obf.isActive()) {
                    StartDT.clickSound.play();
                    aboutGui.setDisplayed(true);
                    buttons[1].get().setChecked(false);
                }
            }
        });

        buttons[2] = new DTButton(
                game,
                0.25f * width,
                0.5f * height - 0.4f*width,
                0.5f * width,
                game.XLFontS.getFont(),
                game.textSystem.get("exit_game"),
                1,
                "exit_button"
        );
        buttons[2].get().addListener(new ChangeListener() {
            @Override
            public void changed (ChangeEvent event, Actor actor) {
                if(!obf.isActive()) {
                    StartDT.mainMusic.stop();
                    dispose();
                    Gdx.app.exit();
                } else {
                    buttons[2].get().setChecked(false);
                }
            }
        });

        stage.addActor(buttons[0].get());
        stage.addActor(buttons[1].get());
        stage.addActor(buttons[2].get());

    }

    @Override
    public void show() {
        if(!StartDT.mainMusic.isPlaying()) StartDT.mainMusic.play();

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        width = stage.getWidth();
        height = stage.getHeight();

        buttonsInitialization();

        aboutGui = new DTGui(
                game,
                stage,
                0.1f * stage.getWidth(),
                0.5f * (stage.getHeight() - 0.8f * stage.getWidth()),
                0.8f * stage.getWidth(),
                0.8f * stage.getWidth(),
                new String[]{
                        game.textSystem.get("author"),
                        game.textSystem.get("amazing_name"),
                        "",
                        game.textSystem.get("contacts"),
                        game.textSystem.get("email"),
                        "",
                        "",
                        game.textSystem.get("version")
                },
                0,
                game.MFontS
        );

        obf = new Obfuscation(game.atlas.createSprite("obfuscation"), true);
        stage.addActor(obf);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        textDraw();
        stage.getBatch().end();

        stage.act(delta);
        stage.draw();

        if(obf.isActive() && !nextScreen){
            obf.deactivate(0.5f, delta);
        } else if(nextScreen){
            if(obf.isActive()){
                game.setScreen(new Game(game, false));
                dispose();
            } else {
                obf.activate(0.5f, delta);
            }
        }

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            StartDT.mainMusic.stop();
            dispose();
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
            StartDT.mainMusic.stop();
            dispose();
            Gdx.app.exit();
        }

    }

    private void textDraw(){
        game.XLFontU.draw(
                stage.getBatch(),
                game.textSystem.get("menu_logo_1"),
                0.5f * (width - game.XLFontU.getWidth(game.textSystem.get("menu_logo_1"))),
                0.95f * height
        );
        game.XLFontU.draw(
                stage.getBatch(),
                game.textSystem.get("menu_logo_2"),
                0.5f * (width - game.XLFontU.getWidth(game.textSystem.get("menu_logo_2"))),
                0.95f * height - 1.5f * game.XLFontU.getHeight("A")
        );
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
