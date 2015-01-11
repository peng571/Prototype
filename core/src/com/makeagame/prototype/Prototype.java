package com.makeagame.prototype;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.KeyEvent;
import com.makeagame.core.view.View;

/**
 * Empty Game Template
 * 
 * @author Peng
 * 
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
                // TODO:
                // resource.bind("xx", new Resource().image("image/xx.png"));
            }
        });
    }

    class GameView implements View {

        int touchStartX, touchStartY;

        @Override
        public void signal(ArrayList<SignalEvent> signalList) {
            for (SignalEvent s : signalList) {
                if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                    if (s.signal.press(KeyEvent.ANY_KEY) && s.action == SignalEvent.ACTION_DOWN) {
                        touchStartX = s.signal.x;
                        touchStartY = s.signal.y;
                    }
                    if (s.action == SignalEvent.ACTION_UP) {

                    }
                }
            }
        }

        @Override
        public ArrayList<RenderEvent> render(String build) {

            ArrayList<RenderEvent> list = new ArrayList<RenderEvent>();
            Hold hold = new Gson().fromJson(build, Hold.class);
            return list;
        }

    }

    class GameModel implements Model {

        BigMap map;
        
        public GameModel() {
            map = new BigMap();
        }

        @Override
        public String hold() {
            Hold hold = new Hold();
            return new Gson().toJson(hold);
        }

        @Override
        public void process(int command, JSONObject json) throws JSONException {
            switch (command) {
            // TODO
            }

        }

    }

    class Sign {
        String action; // build, unit
        String buildName; // if action is build
        int r; // 對應大地圖的格子 ROW
        int c; // 對應大地圖的格子 CAL
    }

    class Hold {
        String[][][] map;
        String clickObjectInfo;
    }
    
    
    class BigMap {
        
        int mapW = 30, mapH = 16;
        
        String[][] groundLevel; // no, TODO 地面的擴充
        
        String[][] buildLevel; // tree, rock, road, build_xx
        
        String[][] animLevel; // job_xx
        
        String [][][] tempView;
        
        public BigMap(){
            groundLevel = new String[mapW][mapH];
            buildLevel = new String[mapW][mapH];
            animLevel = new String[mapW][mapH];
            for(int i=0;i<mapW;i++){
                for(int j=0;j<mapH;j++){
                    groundLevel[i][j] = "ground";
                    buildLevel[i][j] = "no";
                    animLevel[i][j] = "no";
                }
            }
            
            tempView = new String[3][mapW][mapH];
        }
        public String[][][] getView (Camera c){
            // TODO 套入相機的範圍
            tempView[0] = groundLevel;
            tempView[1] = buildLevel;
            tempView[2] = animLevel;
            return tempView;
        }
    }
    
    class Camera{
        int x, y, w, h;
        
        public Camera(){
            x = 0;
            y = 0;
            w = 30;
            h = 16;
        }
        
        public void move(){
            // TODO 鏡頭的移動
        }
    }
    
    interface GameObject{
        // TODO
    }
    
    class Building implements GameObject{
        String name;
        
        // TODO
    }
    
    class Wolker implements GameObject{
        String name;
        
        // TODO
    }
    

}
