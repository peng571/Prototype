package com.makeagame.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.makeagame.core.model.Model;
import com.makeagame.core.view.View;

public final class Controler {

    public static Controler instance;
    public Model mainModel;
    public View mainView;

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
        try {
            mainModel.process(command, params);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void register(Model mainModel, View mainView) {
        this.mainModel = mainModel;
        this.mainView = mainView;
    }

    public String build() {
        return mainModel.hold();
    }

}
