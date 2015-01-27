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
                resource.bindImage("ground", "ground.png").src(32, 32);
                
                // RES
                resource.bindImage("rock", "rock.png").src(32, 32);
                resource.bindImage("tree", "tree.png").src(32, 32);
                resource.bindImage("road", "road.png").src(32, 32);
                
                // BUILDING
                resource.bindImage("rockfectory", "rockfectory.png").src(64, 64);
                resource.bindImage("woodfectory", "woodfectory.png").src(64, 64);
                
                // UI
                resource.bindImage("tab", "tab.png").src(240, 720);
                resource.bindImage("build_error", "build_error.png").src(32, 32);
                resource.bindImage("build_ok", "build_ok.png").src(32, 32);
                resource.bindImage("btn_build", "btn_build.png").src(64,64);
                resource.bindImage("btn_res", "btn_res.png").src(64,64);
                resource.bindImage("button", "btn.png").src(64, 64);
                resource.bindImage("btn1", "btn_build.png").src(64, 64);
                
            }
        });
    }

    interface GameObject {
        // TODO
    }

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
    public static final int Cancel = 0;
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
    
//    public void clean(){
//        map = null;
////        r = 0;
////        c = 0;
//        action = "";
//        buildResultMap = null;
//        clickObjectInfo  = "";
//    }
    
    public void cleanAction(){
        action = "";
        buildResultMap = null;
        clickObjectInfo  = "";
    }
}

class Camera {
    int x, y, row, col;
    float ratio;

    public Camera() {
        x = 0;
        y = 0;
        ratio = 1.0f;
        row = (int)((Bootstrap.screamWidth() - 240f) / (32f/ratio)) +1;
        col = (int)(Bootstrap.screamHeight() / (32f/ratio)) +1;
        
        System.out.println("reset camera row="+row + "col=" + col );
    }

    public void move() { 
        // TODO 鏡頭的移動
    }
}