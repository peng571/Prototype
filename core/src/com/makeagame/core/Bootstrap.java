package com.makeagame.core;

import com.badlogic.gdx.graphics.Color;
import com.makeagame.core.model.Model;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.View;

public abstract class Bootstrap {

    public static final int FPS = 60;
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    public static float ratio = 1f;
    public static Color BACKGROUND_COLOR = new Color(1, 1, 1, 1);

    public abstract View setMainView();

    public abstract Model setMainModel();

    public abstract void resourceFactory(ResourceManager resource);

    public static int screamWidth() {
        return (int) (WIDTH * ratio);
    }

    public static int screamHeight() {
        return (int) (HEIGHT * ratio);
    }

}
