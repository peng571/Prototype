package com.makeagame.core.model;

import org.json.JSONException;
import org.json.JSONObject;

public interface Model {

    // 改變內部行為
    void process(int command, JSONObject json) throws JSONException;

    // 完整交出內部
    String hold();

}
