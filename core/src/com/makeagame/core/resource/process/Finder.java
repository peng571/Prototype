package com.makeagame.core.resource.process;

import org.json.JSONObject;


/**
 * ID 和 Path 的對應設置和管理，也只有 Finder 會需要知道 Path
 * 實作擴充方向: 檔案系統搜尋器、設定檔搜尋器、註冊式搜尋器、資料夾處理器
 * 
 */
public interface Finder {

    // 若無，請回傳"" 或null
    String findPath(String id);

    String[] getIDList();

    JSONObject find(String id);
    
}