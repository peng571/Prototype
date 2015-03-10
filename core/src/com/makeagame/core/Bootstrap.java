package com.makeagame.core;

import com.badlogic.gdx.graphics.Color;
import com.makeagame.core.model.Model;
import com.makeagame.core.view.View;

/**
 * XXX: 不確定是要幹嘛(名稱不明確)
 * 
 * 暫時定位為:
 * 遊戲的基本設定檔(程式部分)
 * 
 *
 */
public abstract class Bootstrap {

    public static final int FPS = 60;
    private static final int WIDTH = 960;
    private static final int HEIGHT = 540;
    public static float ratio = 1f;
    public static Color BACKGROUND_COLOR = new Color(1, 1, 1, 1);

    public abstract View getMainView();

    public abstract Model getMainModel();

    public abstract Driver getDriver();
    
//    public abstract Engine getEngine();
    
//    public abstract void resourceFactory(Resource2Manager resource);

    public static int screamWidth() {
        return (int) (WIDTH * ratio);
    }

    public static int screamHeight() {
        return (int) (HEIGHT * ratio);
    }

}
