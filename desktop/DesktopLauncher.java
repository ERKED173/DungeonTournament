package ru.erked.dt.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

import ru.erked.dt.StartDT;

public class DesktopLauncher {
	public static void main (String[] arg) {

/**
 */
		TexturePacker.Settings settings = new TexturePacker.Settings();
		settings.maxWidth = 2048;
		settings.maxHeight = 2048;
		TexturePacker.process(settings, "D:/Projects/DungeonTournament/resource/textures", "D:/Projects/DungeonTournament/source/android/assets/textures", "texture");
/**
 */

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 450;
		config.height = 800;
		new LwjglApplication(new StartDT(1), config);
	}
}
