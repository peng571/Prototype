package com.makeagame.prototype;

import org.json.JSONException;
import org.json.JSONObject;

import com.makeagame.core.Bootstrap;
import com.makeagame.core.Driver;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.plugin.libgdx.LibgdxDriver;
import com.makeagame.core.plugin.libgdx.LibgdxProcessor;
import com.makeagame.core.resource.ResourceSystem;
import com.makeagame.core.resource.process.RegisterFinder;
import com.makeagame.core.view.View;

/**
 * Enter Point 在這邊為測試用進入點
 */
public class Prototype {
    private LibgdxDriver driver;

    public Prototype() {

        driver = new LibgdxDriver();
        Engine engine = new Engine(new Bootstrap() {

            @Override
            public View getMainView() {
                return new GameView();
            }

            @Override
            public Model getMainModel() {
                return new GameModel();
            }

            @Override
            public Driver getDriver() {
                return driver;
            }
        });
        driver.setEngine(engine);

        ResourceSystem rs = ResourceSystem.get();

        RegisterFinder finder = new RegisterFinder();
        registerResource(finder);

        LibgdxProcessor processor = new LibgdxProcessor(finder);
        rs.addProcessor(processor);
    }

    /*
     * 可讓使用者自行選擇包裝資訊的方法 這邊用JSONObject來包裝資訊
     */
    private JSONObject packageData(String path) throws JSONException {
        return new JSONObject().put("path", path);
    }

    private JSONObject packageData(String path, int w, int h) throws JSONException {
        return packageData(path).put("type", "img").put("x", 0).put("y", 0).put("w", w).put("h", h);
    }

    private JSONObject packageData(String path, int x, int y, int w, int h) throws JSONException {
        return packageData(path).put("type", "img").put("x", x).put("y", y).put("w", w).put("h", h);
    }

    private JSONObject packageData(String path, String type) throws JSONException {
        return packageData(path).put("type", type);
    }

    private void registerResource(RegisterFinder finder) {
        try {
            finder.register("ground", packageData("ground.png", 32, 32));

            // RES
            finder.register("rock", packageData("rock.png", 32, 32));
            finder.register("tree", packageData("tree.png", 32, 32));
            finder.register("road", packageData("road.png", 32, 32));

            // BUILDING
            finder.register("rockfectory", packageData("rockfectory.png", 64, 64));
            finder.register("woodfectory", packageData("woodfectory.png", 64, 64));

            // UI
            finder.register("tab", packageData("tab.png", 240, 720));
            finder.register("build_error", packageData("build_error.png", 32, 32));
            finder.register("build_ok", packageData("build_ok.png", 32, 32));
            finder.register("btn_build", packageData("btn_build.png", 64, 64));
            finder.register("btn_res", packageData("btn_res.png", 64, 64));
            finder.register("button", packageData("btn.png", 64, 64));
            finder.register("btn1", packageData("btn_build.png", 64, 64));

            // Role
            finder.register("role", packageData("role.png"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 提供跨平台用的程序，目前主要還是透過 Libgdx來實現跨平台
    public LibgdxDriver getApplication() {
        return driver;
    }
}

interface GameObject {
    // TODO
}

class Wolker implements GameObject {
    String name;

    // TODO find road

    // TODO 屬性...

    // TODO xx
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
    // int r;
    // int c;
    boolean[][] buildResultMap;

    // if click unit
    String clickObjectInfo;

    // public void clean(){
    // map = null;
    // // r = 0;
    // // c = 0;
    // action = "";
    // buildResultMap = null;
    // clickObjectInfo = "";
    // }

    public void cleanAction() {
        action = "";
        buildResultMap = null;
        clickObjectInfo = "";
    }
}

class Camera {
    int x, y, row, col;
    float ratio;

    public Camera() {
        x = 0;
        y = 0;
        ratio = 1.0f;
        row = (int) ((Bootstrap.screamWidth() - 240f) / (32f / ratio)) + 1;
        col = (int) (Bootstrap.screamHeight() / (32f / ratio)) + 1;

        System.out.println("reset camera row=" + row + "col=" + col);
    }

    public void move() {
        // TODO 鏡頭的移動
    }
}