package com.makeagame.prototype.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.makeagame.core.Bootstrap;
import com.makeagame.prototype.Prototype;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Bootstrap.screamWidth();
        config.height = Bootstrap.screamHeight();
        config.title = "Game";
        new LwjglApplication(new Prototype().getEngine(), config);
    }
}
