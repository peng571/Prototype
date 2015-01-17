package com.makeagame.prototype;

import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.View;

/**
 * COPY練習 - 工人物語
 */
public class Prototype {
    private Engine engine;

    public Engine getEngine() {
        return engine;
    }

    public Prototype() {
        engine = new Engine(new Bootstrap() {
            @Override
            public View setMainView() {
                return new GameView();
            }

            @Override
            public Model setMainModel() {
                return new GameModel();
            }

            @Override
            public void resourceFactory(ResourceManager resource) {
                resource.bindImage("ground", "ground.png").src(0, 0, 32, 32);
                
                // RES
                resource.bindImage("rock", "rock.png").src(0, 0, 32, 32);
                resource.bindImage("tree", "tree.png").src(0, 0, 32, 32);
                resource.bindImage("road", "road.png").src(0, 0, 32, 32);
                
                // BUILDING
                resource.bindImage("rockf", "rockfectory.png").src(0, 0, 64, 64);
                resource.bindImage("woodf", "woodfectory.png").src(0, 0, 64, 64);
                
                // UI
                resource.bindImage("tab", "tab.png").src(0, 0, 128, 540);
                
                // BTN
                resource.bindImage("button", "btn.png").src(0, 0, 64, 64);
                resource.bindImage("btn1", "btn_build.png").src(0, 0, 64, 64);
            }
        });
    }

    interface GameObject {
        // TODO
    }

    // class Building implements GameObject{
    // String name;
    //
    // // TODO
    // }
    class Wolker implements GameObject {
        String name;
        // TODO
    }
}

class Sign {
    
    // String action; // build, unit
    // String buildName; // if action is build
    // int r; // 對應大地圖的格子 ROW
    // int c; // 對應大地圖的格子 CAL
    // Camera camera;
    
    public static final int Ask_Build = 2;
    public static final int Build = 1;
}

class Hold {
    String[][][] map;
    
    String action; // click, build, ask_build
    
    // if ask for build
//    int r;
//    int c;
    boolean[][] buildResultMap; 
    
    // if click unit
    String clickObjectInfo;  
    
    public void clean(){
        map = null;
//        r = 0;
//        c = 0;
        action = "";
        buildResultMap = null;
        clickObjectInfo  = "";
    }
}

class Camera {
    int x, y, w, h;
    float ratio;

    public Camera() {
        x = 0;
        y = 0;
        w = 30;
        h = 16;
        ratio = 1.0f;
    }

    public void move() {
        // TODO 鏡頭的移動
    }
}