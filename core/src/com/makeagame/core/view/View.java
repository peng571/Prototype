package com.makeagame.core.view;

import java.util.ArrayList;

import org.json.JSONException;

public interface View {

    // 接收外部指令
    public abstract void signal(ArrayList<SignalEvent> s) throws JSONException;

    // 送出繪圖指令
    public abstract ArrayList<RenderEvent> render(String list);
    
    public abstract String info();
}
