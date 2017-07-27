package ru.erked.dt.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ru.erked.dt.StartDT;
import ru.erked.dt.systems.DTFont;
import ru.erked.dt.systems.DTTextSystem;
import ru.erked.dt.utilities.Obfuscation;
import ru.erked.dt.utilities.Player;
import ru.erked.dt.utilities.Weapon;

public class Splash implements Screen {

    private StartDT game;
    private Obfuscation obf;
    private boolean isObfOver = false;

    public Splash(StartDT game){
        this.game = game;
    }

    @Override
    public void show() {
        game.stage = new Stage();
        game.atlas = new TextureAtlas("textures/texture.atlas");
        game.textSystem = new DTTextSystem(game.lang);
        game.XLFontU = new DTFont("fonts/normal.ttf", 10, Color.WHITE, game.textSystem.get("symbols"));
        game.XLFontS = new DTFont("fonts/normal.ttf", 10, Color.WHITE, 3, 3, Color.BLACK, game.textSystem.get("symbols"));
        game.LFontU = new DTFont("fonts/normal.ttf", 13, Color.WHITE, game.textSystem.get("symbols"));
        game.LFontS = new DTFont("fonts/normal.ttf", 13, Color.WHITE, 3, 3, Color.BLACK, game.textSystem.get("symbols"));
        game.MFontS = new DTFont("fonts/normal.ttf", 16, Color.WHITE, 3, 3, Color.BLACK, game.textSystem.get("symbols"));
        game.MFontU = new DTFont("fonts/normal.ttf", 16, Color.WHITE, game.textSystem.get("symbols"));
        game.SFontU = new DTFont("fonts/normal.ttf", 20, Color.WHITE, game.textSystem.get("symbols"));
        StartDT.prefs = Gdx.app.getPreferences("game_prefs");
        StartDT.clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/click.wav"));
        StartDT.hit = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/hit.wav"));
        StartDT.bowHit = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/bow.wav"));
        StartDT.magicHit = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/magic.wav"));
        StartDT.hurt = Gdx.audio.newSound(Gdx.files.internal("sounds/misc/hurt.wav"));
        StartDT.mainMusic =  Gdx.audio.newMusic(Gdx.files.internal("sounds/music/main.mp3"));
        StartDT.mainMusic.setLooping(true);
        StartDT.inGameMusic =  Gdx.audio.newMusic(Gdx.files.internal("sounds/music/in_game.mp3"));
        StartDT.inGameMusic.setLooping(true);
        StartDT.weapons = new Weapon[] {
                new Weapon(1, 1f, 0.5f, 1, 0, 0, true, true),
                new Weapon(1, 0.5f, 0.25f, 2, 0, 0, true, false),
                new Weapon(1, 1.5f, 0.75f, 3, 0, 0, true, false),
                new Weapon(2, 2f, 0.5f, 1, 160, 4, false, false),
                new Weapon(2, 1f, 0.25f, 2, 180, 4, false, false),
                new Weapon(2, 3f, 0.75f, 3, 200, 4, false, false),
                new Weapon(3, 3f, 0.4f, 1, 225, 7, false, false),
                new Weapon(3, 1.5f, 0.2f, 2, 250, 7, false, false),
                new Weapon(3, 4.5f, 0.6f, 3, 275, 7, false, false),
                new Weapon(4, 4f, 0.4f, 1, 500, 10, false, false),
                new Weapon(4, 2f, 0.2f, 2, 550, 10, false, false),
                new Weapon(4, 6f, 0.6f, 3, 600, 10, false, false),
                new Weapon(5, 6f, 0.3f, 1, 1000, 13, false, false),
                new Weapon(5, 3f, 0.15f, 2, 1100, 13, false, false),
                new Weapon(5, 9f, 0.45f, 3, 1200, 13, false, false)
        };
        StartDT.weaponsOwned = new boolean[StartDT.weapons.length];
        StartDT.player = new Player();
        StartDT.player.setWeapon(StartDT.weapons[0]);
        StartDT.player.loadStats();
        StartDT.weaponsOwned[0] = true;
        StartDT.weaponsOwned[1] = true;
        StartDT.weaponsOwned[2] = true;

        Gdx.input.setInputProcessor(game.stage);

        obf = new Obfuscation(game.atlas.createSprite("obfuscation"), true);
        game.stage.addActor(obf);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(obf.isActive() && !isObfOver){
            obf.deactivate(1f, delta);
        }else if(obf.isActive() && isObfOver){
            game.setScreen(new Menu(game));
            dispose();
        }
        if(!obf.isActive()){
            isObfOver = true;
            obf.activate(1f, delta);
        }

        game.stage.getBatch().begin();

        float width = game.stage.getWidth();
        float height = game.stage.getHeight();

        game.XLFontU.draw(
                game.stage.getBatch(),
                game.textSystem.get("game_logo"),
                0.5f * (width - game.XLFontU.getWidth(game.textSystem.get("game_logo"))),
                0.5f * (height + game.XLFontU.getHeight("A"))
        );

        game.stage.getBatch().end();

        game.stage.act(delta);
        game.stage.draw();

        if(Gdx.input.isKeyJustPressed(Input.Keys.BACK)){
            dispose();
            Gdx.app.exit();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.HOME)){
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
    }

    @Override
    public void resume() {
        this.show();
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        StartDT.player.saveStats();
    }
}
