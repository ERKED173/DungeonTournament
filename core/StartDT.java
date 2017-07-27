package ru.erked.dt;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;

import ru.erked.dt.screens.Splash;
import ru.erked.dt.systems.DTFont;
import ru.erked.dt.systems.DTTextSystem;
import ru.erked.dt.utilities.Player;
import ru.erked.dt.utilities.Weapon;

public class StartDT extends Game {

    public static Preferences prefs;
    public int lang;
    public Stage stage;
    public TextureAtlas atlas;
    public DTTextSystem textSystem;
    public DTFont XLFontU, XLFontS, LFontU, LFontS, MFontS, MFontU, SFontU;
    public static short closeIterator = 0;
    public static Sound clickSound, hit, bowHit, magicHit, hurt;
    public static Music mainMusic, inGameMusic;
    public static Player player;
    public static Weapon[] weapons;
    public static boolean[] weaponsOwned;

    public StartDT(int lang){
        this.lang = lang;
    }

	@Override
	public void create () {
        setScreen(new Splash(this));
	}
	
	@Override
	public void dispose () {
        atlas.dispose();
	}
}
