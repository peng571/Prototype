package com.makeagame.prototype;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Controler;
import com.makeagame.core.action.Action;
import com.makeagame.core.action.EventListener;
import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.MouseEvent;
import com.makeagame.core.view.SignalEvent.Signal;
import com.makeagame.core.view.TextView;
import com.makeagame.core.view.View;
import com.makeagame.tools.Button;
import com.makeagame.tools.SimpleLayout;
import com.makeagame.tools.Sprite;
import com.makeagame.tools.PageView;

public class GameView implements View {

    int touchStartX, touchStartY;
    
    Camera camera;
    
    SimpleLayout layoutMain;
    LayoutTab layoutTab;
    LayoutMap layoutMap;
    
    String action = "no"; // no, build, unit,
    String tempBuilding = "";

    public GameView(){
        camera = new Camera();
        
        layoutMain = new SimpleLayout();
        
        layoutMap = new LayoutMap().XY(240, 0);
        layoutMain.addChild(layoutMap);
        
        layoutTab = new LayoutTab(new Sprite("tab"));
        layoutMain.addChild(layoutTab);
        
    }
    
    @Override
    public void signal(ArrayList<SignalEvent> signalList) {
        
        layoutTab.signal(signalList);
        layoutMap.signal(signalList);
        
        for (SignalEvent s : signalList) {
            if (s.type == SignalEvent.MOUSE_EVENT && s.action == SignalEvent.ACTION_DOWN) {
                switch(s.signal.key){
                case MouseEvent.RIGHT:
                    System.out.println("cencle");
                    action = "no";
                    tempBuilding  = "";
                    Controler.get().call( Sign.Cancel, null);
                    break;
                }
            }
        }
    }

    @Override
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list, String build) {
        
        Hold hold = new Gson().fromJson(build, Hold.class);
        layoutMap.model(hold);
        
        layoutMain.render(list);
        return list;
    }
    
    class LayoutTab extends SimpleLayout {
        
        PageView pages;
        BuildingData bd = BuildingData.get();
        
        public LayoutTab(Sprite s){
            super(s);
            pages = new PageView();
            
            // 建築頁面
            SimpleLayout buildingLayout = new SimpleLayout();
            {
                for(int i=0;i<bd.size;i++){
                    final int fi = i;
                    
                    int cow = 3;
                    int x = 10 + i%cow * 76;
                    int y = 10 + i/cow * 76;
                    
                    // new SimpleLayout(new Sprite(bd.datas.get(fi).name)
                    Button btn = new Button(new Sprite("button")).RectArea(x, y, 64, 64);
                    btn.onClickAction = new Action(){
                        
                        @Override 
                        public void execute(){
                            action = "build";
                            tempBuilding = bd.datas.get(fi).name;
                            System.out.println("building " + bd.datas.get(fi).name);
                        }
                    };
                    btn.addChild(new SimpleLayout(new Sprite(bd.datas.get(fi).name)).XY(x+ 16, y+16));
                    buildingLayout.addChild(btn);
                }
            }
            pages.addTab(new Button(new Sprite("btn_build")).RectArea(10, 10, 64, 64), buildingLayout);
            
            
            // 資源頁面
            SimpleLayout reourceLayout = new SimpleLayout();
            {
                reourceLayout.addChild(new SimpleLayout(new Sprite("tree")));
                reourceLayout.addChild(new TextView("0123"));
            }
            pages.addTab(new Button(new Sprite("btn_res")).RectArea(80, 10, 64, 64), reourceLayout);
            
            addChild(pages.XY(0, 30));
            
            
//            SimpleLayout buildingLayout = new SimpleLayout();
//          {
//              for(int i=0;i<bd.size;i++){
//                  final int fi = i;
//                  
//                  int cow = 3;
//                  int x = 10 + i%cow * 76;
//                  int y = 10 + i/cow * 76;
//                  
//                  // new SimpleLayout(new Sprite(bd.datas.get(fi).name)
//                  Button btn = new Button(new Sprite("button")).RectArea(x, y, 64, 64);
//                  btn.onClickAction = new Action(){
//                      
//                      @Override 
//                      public void execute(){
//                          action = "build";
//                          tempBuilding = bd.datas.get(fi).name;
//                          System.out.println("building " + bd.datas.get(fi).name);
//                      }
//                  };
//                  btn.addChild(new SimpleLayout(new Sprite(bd.datas.get(fi).name)).XY(x+ 16, y+16));
//                  buildingLayout.addChild(btn);
//              }
//          }
//          addChild(buildingLayout);
            
        }
        
//        @Override
//        public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list) {
//           list = super.render(list);
//           return list;
//        }
    }
    
    
    class LayoutMap extends SimpleLayout {
        
        int startX = 240;
        
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
                        System.out.println("click build");
                        build(tempBuilding, R(s), C(s)); // TODO 可選擇建設種類
                    }else if(action.equals("unit")){
                        // TODO 單位的控制
                        System.out.println("?");
                    }else{
                        System.out.println(action + "?");
                    }
                }
                
                @Override
                public void OnMouseMove(Signal s) {
                    if(action.equals("build")) {
                        askBuild(tempBuilding, R(s), C(s)); // TODO 帶入建物大小
                    }
                }
            };
        }
        
        public void model(Hold hold){
            map = hold.map;
            buildRes = hold.buildResultMap;
        }
        
        @Override
        public void signal(ArrayList<SignalEvent> s){
            listener.signal(s);
        }
        
        @Override
        public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list) {
           super.render(list);

           // 地圖畫面  
           // 繪製順序  groundLevel > buildingLevel > animLevel
           for(int i=0;i<3;i++){
              for(int j=0;j<map[i].length; j++){
                for(int k=0;k<map[i][j].length;k++){
                    if(!"no".equals(map[i][j][k])){
                        list.add( new RenderEvent(ResourceManager.get()
                                .fetch(map[i][j][k] ))
                                .XY(plateW * (j-1) + startX,  plateH * (k-1))
                                //.src(0, 0, -1, -1)
                        );
                    }
                }
              }
           }
            
           // 建築指示
            if(buildRes != null){
                for(int i=0;i<buildRes.length;i++){
                    for(int j=0;j<buildRes[i].length; j++){
                        String resName = "";
                        if(buildRes[i][j]){
                            resName = "build_ok";
                        }else{
                            resName = "build_error";
                        }
                        list.add( new RenderEvent(ResourceManager.get()
                                .fetch(resName ))
                                .XY(plateW * (i+r-1) + startX,  plateH * (j+c-1))
                        );
                    }
                }
            }
            
            return list;
        }
        
//        private ArrayList<RenderEvent> drawBuildRes(ArrayList<RenderEvent> list, res)
        
        @Override
        public LayoutMap XY(int x, int y){
            listener.setRectArea(x, y, Bootstrap.screamWidth() - x, Bootstrap.screamHeight());
            return (LayoutMap)super.XY(x, y);
        }
        
        public void build(String name, int r, int c) {
                try {
                    Controler.get().call(
                        Sign.Build, 
                        new JSONObject()
                                .put("name", name)
                                .put("r", r)
                                .put("c", c));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
         }
        
        public void askBuild(String name, int r, int c) {
            try {
                Controler.get().call(
                        Sign.Ask_Build,
                        new JSONObject()
                                .put("name", name)
                                .put("r", r)
                                .put("c", c));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}