package com.makeagame.prototype;

import java.util.ArrayList;

public class BuildingData {

    public static BuildingData instance;
    // public static HashMap<String, Building> datas ;
    public ArrayList<Building> datas;
    public int size;

    public BuildingData() {
        // datas = new HashMap<String, Building>();
        datas = new ArrayList<Building>();

        // TODO 動態讀入資料
        // datas.put("road", new Building(1, 1, "road", "路"));
        // datas.put("tree", new Building(1, 1, "tree", "樹"));
        // datas.put("rock", new Building(1, 1, "rock", "石"));
        // datas.put("woodfectory", new Building(2, 2, "woodfectory", "伐木營地"));
        // datas.put("rockfectory", new Building(2, 2, "rockfectory", "礦石營地"));
        datas.add(new Building(1, 1, "road", "路"));
        datas.add(new Building(1, 1, "tree", "樹"));
        datas.add(new Building(1, 1, "rock", "石"));
        datas.add(new Building(2, 2, "woodfectory", "伐木營地"));
        datas.add(new Building(2, 2, "rockfectory", "礦石營地"));
        
        size = datas.size();
    }

    public static BuildingData get(){
        if(instance == null){
            instance = new BuildingData();
        }
        return instance;
    }
    
    public Building building(String name) {
        for(Building b : datas){
            if(b.name.equals(name)){
                return b;
            }
        }
        return null;
    }
}

class Building {

    int w, h;
    String name;
    String showName; // 給玩家看的資訊

    public Building(int w, int h, String resName, String showName) {
        this.w = w;
        this.h = h;
        this.name = resName;
        this.showName = showName;

    }

}
