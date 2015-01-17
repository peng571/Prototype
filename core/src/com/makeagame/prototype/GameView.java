package com.makeagame.prototype;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Controler;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.EventListener;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.MouseEvent;
import com.makeagame.core.view.SignalEvent.Signal;
import com.makeagame.core.view.View;
import com.makeagame.tools.SimpleLayout;
import com.makeagame.tools.Sprite;

public class GameView implements View {

    int touchStartX, touchStartY;
    
    Camera camera;
    
    SimpleLayout layoutMain;
    SimpleLayout layoutTab;
    LayoutMap layoutMap;
    
    EventListener btnBuilding;
    
    String action = "no"; // no, build, unit,

    public GameView(){
        camera = new Camera();
        
        layoutMain = new SimpleLayout();
        
        layoutMap = new LayoutMap().xy(128, 0);
        layoutMain.addChild(layoutMap);
        
        layoutTab = new SimpleLayout(new Sprite("tab"));
        {
            SimpleLayout btnBuilding1 = new SimpleLayout(new Sprite("button")).xy(10,10);
            btnBuilding = new EventListener(){
                @Override
                public void OnMouseDown(Signal s) {
                    action = "build";
                }
            };
            btnBuilding.setRectArea(10,10,64,64);
            
            layoutTab.addChild(btnBuilding1);
        }
        layoutMain.addChild(layoutTab);
        
    }
    
    @Override
    public void signal(ArrayList<SignalEvent> signalList) {
        
        layoutMap.listener.signal(signalList);
        btnBuilding.signal(signalList);
        
        for (SignalEvent s : signalList) {
            if (s.type == SignalEvent.MOUSE_EVENT && s.action == SignalEvent.ACTION_DOWN) {
                switch(s.signal.key){
                case MouseEvent.RIGHT:
                    System.out.println("cencle");
                    action = "no";
                    break;
                }
            }
        }
        
        
    }

    @Override
    public ArrayList<RenderEvent> render(String build) {
        ArrayList<RenderEvent> list = new ArrayList<RenderEvent>(); // TODO 循環利用
        Hold hold = new Gson().fromJson(build, Hold.class);
        layoutMap.model(hold);
        
        list.addAll( layoutMain.render());
        return list;
    }
    
    class LayoutMap extends SimpleLayout {
        
        int plateW = 32;
        int plateH = 32;
        
        int r;
        int c;
         
        String[][][] map;
        boolean[][] buildRes;
        EventListener listener;
        
        public LayoutMap(){
            super();
            
            listener = new EventListener(){
                
                int R(Signal s){
                    r = s.x / plateW +1;
                    return r;
                }
                
                int C(Signal s){
                    c = s.y / plateH +1;
                    return c;
                }
                
                @Override
                public void OnMouseDown(Signal s) {
                    if(action.equals("build")){
                        // TODO 建物
                        build("rock", R(s), C(s)); // TODO 可選擇建設種類
                    }else if(action.equals("unit")){
                        // TODO 單位的控制
                    }
                }
                
                @Override
                public void OnMouseMove(Signal s) {
                    if(action.equals("build")) {
                        askBuild(R(s), C(s), 1, 1); // TODO 帶入ˋ建物大小
                    }
                }
            };
        }
        
        public void model(Hold hold){
            map = hold.map;
            buildRes = hold.buildResultMap;
        }
        
        @Override
        public ArrayList<RenderEvent> render() {
           ArrayList<RenderEvent> list = super.render();

           // 地圖畫面
            int i = 0; // TODO for i in 0..3
            for(int j=0;j<map[i].length; j++){
                for(int k=0;k<map[i][j].length;k++){
                    list.add( new RenderEvent(ResourceManager.get()
                            .fetch(map[i][j][k] ))
                            .XY(plateW * j,  plateH* k)
                    );
                }
            }
            
           // 建築指示
            if(buildRes != null){
                for( i=r;i<buildRes.length;i++){
                    for(int j=c;j<buildRes[i].length; j++){
                        String resName = "";
                        if(buildRes[i][j]){
                            resName = "build_ok";
                        }else{
                            resName = "build_error";
                        }
                        list.add( new RenderEvent(ResourceManager.get()
                                .fetch(resName ))
                                .XY(plateW * i,  plateH* j)
                        );
                    }
                }
            }
            
            return list;
        }
        
        @Override
        public LayoutMap xy(int x, int y){
            listener.setRectArea(x, y, 500, 500);
            return (LayoutMap)super.xy(x, y);
        }
        
        public void build(String name, int r, int c) {
                try {
                    Controler.get().call(
                        Sign.Build, new JSONObject()
                                .put("r", r)
                                .put("c", c));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
         }
        
        public void askBuild(int r, int c, int w, int h) {
            try {
                Controler.get().call(
                        Sign.Build, new JSONObject()
                                .put("r", r)
                                .put("c", c)
                                .put("w", 2)
                                .put("h", 2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}