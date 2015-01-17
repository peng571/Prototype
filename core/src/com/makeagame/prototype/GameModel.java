package com.makeagame.prototype;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.model.Model;

public  class GameModel implements Model {

    static int mapW = 30, mapH = 16;
    
    BigMap map;
    
    Hold hold;
    String holdRes;
    
    public GameModel() {
        map = new BigMap();
        hold = new Hold();
    }

    @Override
    public String hold() {
        hold.map = map.getView(null); // TODO add Camera
        holdRes =   new Gson().toJson(hold);
        hold.clean();
        return holdRes;
    }

    @Override
    public void process(int command, JSONObject json) throws JSONException {
        int r = json.optInt("r");
        int c = json.optInt("c");
        int w = json.optInt("w");
        int h = json.optInt("h");
        String name = json.optString("name");
        
        switch(command){
        case Sign.Ask_Build:
                hold.buildResultMap = map.canBuild(r, c, w, h)[1];
            break;
        case Sign.Build:
            if(map.build(name, r, c, w, h)){
                // TODO build success
            }
            break;
        }
    }
    
    
    
 class BigMap {
        
        String[][] groundLevel; // no, TODO 地面的擴充
        
        String[][] buildLevel; // tree, rock, road, build_xx
        
        String[][] animLevel; // job_xx
        
        String [][][] tempView;
        
        public BigMap(){
            groundLevel = new String[mapW][mapH];
            buildLevel = new String[mapW][mapH];
            animLevel = new String[mapW][mapH];
            
            // 建立初始地圖
            for(int i=0;i<mapW;i++){
                for(int j=0;j<mapH;j++){
                    groundLevel[i][j] = "ground";
                    buildLevel[i][j] = "no";
                    animLevel[i][j] = "no";
                }
            }
            
            tempView = new String[3][mapW][mapH]; // TODO 相機範圍
        }
        public String[][][] getView (Camera c){
            // TODO 套入相機的範圍
            tempView[0] = groundLevel;
            tempView[1] = buildLevel;
            tempView[2] = animLevel;
            return tempView;
        }
        
        // 該板塊是否可以建築
        boolean canBuild(int r, int c){
            if(!"no".equals(groundLevel[r][c])){
                return false;
            }
            if(!"no".equals(buildLevel[r][c])){
                return false;
            }
            return true;
        }
        
        // 是否可以建築
        // res[1] 為MAP顯示結果
        // res[0][0][0] = 總結果
        public boolean[][][] canBuild(int r, int c, int w, int h){
            boolean[][][] res = new boolean[2][][] ;
            res[0] = new boolean[1][1];
            res[1] = new boolean[w][h];
            
            for(int i=r;i<mapW && i<w;i++){
                for(int j=c;j<mapH && j<h;j++){
                    res[1][i][j] = map.canBuild(i,j);
                    res[0][0][0] &= map.canBuild(i,j);
                }
            }
            return res;
        }
        
        public boolean build(String name, int r, int c, int w, int h){
            if(canBuild(r, c, w, h)[0][0][0]){
                for(int i=r;i<w;i++){
                    for(int j=c;j<h;j++){
                        buildLevel[r][c] = name;
                    }
                }
                return true;
            }
            return false;
        }
    }

}