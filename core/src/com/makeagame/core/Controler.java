package com.makeagame.core;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.makeagame.core.model.Model;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.View;

public final class Controler {

    public static Controler instance;
    private Model mainModel;
    private View mainView;

    private Controler() {
        this.mainModel = null;
        this.mainView = null;
    }

    public static Controler get() {
        if (instance == null) {
            instance = new Controler();
        }
        return instance;

    }

    public void call(int command, JSONObject params) {
        if(mainModel == null){
            return;
        }
        
        try {
            mainModel.process(command, params);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register(Model mainModel, View mainView) {
        this.mainModel = mainModel;
        this.mainView = mainView;
    }
    
    public void signal(ArrayList<SignalEvent> signalList){
        if(mainView == null){
            return;
        }

        try {
            Controler.get().mainView.signal(signalList);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
    }
    
    
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> renderList){
        if(mainView == null){
            return renderList;
        }
        
        // Engine.logI("renderList size: " + new Integer(renderList.size()).toString());
        return mainView.render(renderList, Controler.get().build());
    }
    
    

    public String build() {
        if(mainModel == null){
            return "";
        }
        
        return mainModel.hold();
    }

}
