package com.makeagame.core;

import java.util.ArrayList;

import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;

public interface Driver {

    public Driver setEngine(Engine engine);
    
    public void init();
    
    public ArrayList<SignalEvent> signal(ArrayList<SignalEvent> list);
    
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list);
    
}
