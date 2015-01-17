package com.makeagame.prototype;

import java.util.HashMap;

public class BuildingData {
    
    public static HashMap<String, Building> datas ;
    
    public BuildingData() {
        datas = new HashMap<String, Building>();
        
        // TODO 動態讀入資料
        datas.put("road",  new Building(1, 1,  "road", "路"));
        datas.put("tree",  new Building(1, 1,  "tree", "樹"));
        datas.put("rock",  new Building(1, 1,  "rock", "石"));
        datas.put("woodfectory",  new Building(2, 2,  "woodfectory", "伐木營地"));
        datas.put("rockfectory",  new Building(2, 2,  "woodfectory", "礦石營地"));
    }
    
    public static Building get(String name){
        return datas.get(name);
    }
}


class Building{
    
    int w, h;
    String name;
    String resName;

    public Building(int w, int h,  String resName, String name){
        this.w = w;
        this.h = h;
        this.resName = resName;
        this.name = name;
        
    }

}

