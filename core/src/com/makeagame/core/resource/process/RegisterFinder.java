package com.makeagame.core.resource.process;

import java.util.HashMap;

import org.json.JSONObject;

/**
 * 註冊式搜尋器
 * 以程式碼批次註冊 Resource 的 ID 跟 Path
 * 適合小規模的遊戲使用
 */
public class RegisterFinder implements Finder {

    HashMap<String, JSONObject> map = new HashMap</* ID= */ String,
                                                  /* Attr {path, x, y, w, h, type} = */ JSONObject>();
    
    @Override
    public JSONObject find(String id) {
        return map.get(id);
    }
    
    @Override
    public String findPath(String id) {
        JSONObject res = find(id);
        if(res == null){
            return "";
        }
        return res.optString("path");
    }

    @Override
    public String[] getIDList() {
        return map.keySet().toArray(new String[map.size()]);
    }
    
    public void register(String id, JSONObject attr){
        map.put(id, attr);
    }

}
