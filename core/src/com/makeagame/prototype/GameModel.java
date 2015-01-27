package com.makeagame.prototype;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;

public  class GameModel implements Model {

    static int mapW =  (int)((Bootstrap.screamWidth() - 240f) / 32f) +1;
    static int mapH = (int)(Bootstrap.screamHeight() / 32f) +1;
    
    BigMap map;
    
    Hold hold;
    String holdRes;
    
    public GameModel() {
        System.out.println("int model mapW " + mapW + ", mapH " + mapH);
        
        map = new BigMap();
        hold = new Hold();
    }

    @Override
    public String hold() {
        hold.map = map.getView(null); // TODO add Camera
        holdRes =   new Gson().toJson(hold);
//        if(hold.buildResultMap != null){
//        System.out.println("send hold " + new Gson().toJson(hold.buildResultMap));
//        }
//        hold.clean();
        return holdRes;
    }

    @Override
    public void process(int command, JSONObject json) throws JSONException {
        if(json == null){
            json = new JSONObject();
        }
        String name = json.optString("name");
        Building b = BuildingData.get().building(name);
        int r = json.optInt("r");
        int c = json.optInt("c");
        int w = 0;
        int h = 0;
        if(b != null){
            w = b.w;
            h = b.h;
        }else{
            Engine.logE("can't find building data at " + name);
        }
        
        
        switch(command){
        case Sign.Cancel:
            hold.cleanAction();
            break;
        case Sign.Ask_Build:
            //System.out.println("get ask build");
            hold.buildResultMap = map.canBuild(r, c, w, h)[1];
            break;
        case Sign.Build:
            //System.out.println("get build");
            if(map.build(name, r, c, w, h)){
                System.out.printf("build %s at %d, %d success\n", name, r,c);
                // TODO build success
            }
            break;
        }
    }
    
    
    
 class BigMap {
 
        /* 這邊定義要畫的內容 */
        String[][] groundLevel; // no, TODO 地面的擴充
        String[][] buildLevel; // tree, rock, road, build_xx
        String[][] animLevel; // job_xx
        
        String [][][] tempView;

        
        /* 這邊定義權限 */
        boolean [][] buildable;
        
        public BigMap(){
            groundLevel = new String[mapW][mapH];
            buildLevel = new String[mapW][mapH];
            animLevel = new String[mapW][mapH];
            
            buildable = new boolean[mapW][mapH];
            
            // 建立初始地圖
            for(int i=0;i<mapW;i++){
                for(int j=0;j<mapH;j++){
                    groundLevel[i][j] = "ground";
                    buildLevel[i][j] = "no";
                    animLevel[i][j] = "no";
                    
                    buildable[i][j] = true;
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
//            if(!"no".equals(groundLevel[r][c])){
//                return false;
//            }
//            if(!"no".equals(buildLevel[r][c])){
//                return false;
//            }
            return buildable[r][c];
        }
        
        // 是否可以建築
        // res[1] 為MAP顯示結果
        // res[0][0][0] = 總結果
        public boolean[][][] canBuild(int r, int c, int w, int h){
            boolean[][][] res = new boolean[2][][] ;
            res[0] = new boolean[][]{{true}};
            res[1] = new boolean[w][h];
            
            for(int i=0,ir=r;ir<mapW && i<w;i++,ir++){
                for(int j=0,jc=c;jc<mapH && j<h;j++,jc++){
                    res[1][i][j] = map.canBuild(ir,jc);
                    res[0][0][0] &= map.canBuild(ir,jc);
                }
            }
            return res;
        }
        
        public boolean build(String name, int r, int c, int w, int h){
            if(canBuild(r, c, w, h)[0][0][0]){
                for(int i=0;i<w;i++){
                    for(int j=0;j<h;j++){
                        if(i==0 && j==0){
                            buildLevel[r][c] = name; 
                        }else{
//                            buildLevel[i+r][j+c] = ""; 
                        }
                        buildable[i+r][j+c] = false;
                    }
                }
                return true;
            }
            return false;
        }
    }

}