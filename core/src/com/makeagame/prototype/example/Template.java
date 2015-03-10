package com.makeagame.prototype.example;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.makeagame.core.Bootstrap;
import com.makeagame.core.Driver;
import com.makeagame.core.Engine;
import com.makeagame.core.model.Model;
import com.makeagame.core.plugin.libgdx.LibgdxDriver;
import com.makeagame.core.plugin.libgdx.LibgdxProcessor;
import com.makeagame.core.resource.ResourceSystem;
import com.makeagame.core.resource.process.RegisterFinder;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.SignalEvent.KeyEvent;
import com.makeagame.core.view.View;

/**
 * Empty Game Template
 * Using Libgdx Pluggin
 * 
 * @author Peng
 * 
 */
public class Template {

    private LibgdxDriver driver;
    
    public Template() {

        driver = new LibgdxDriver();
        Engine engine = new Engine(new Bootstrap() {
            
            @Override
            public View getMainView() {
                return  new GameView();
            }

            @Override
            public Model getMainModel() {
                return new GameModel();
            }
            
            @Override
            public Driver getDriver() {
                return driver;
            }
        });
        driver.setEngine(engine);
        
        ResourceSystem rs = ResourceSystem.get();

        RegisterFinder finder = new RegisterFinder();
        try {
            finder.register("xx", new JSONObject().put("path", "image/xx.png"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        LibgdxProcessor processor = new LibgdxProcessor(finder);
        rs.addProcessor(processor);
    }

    class GameView implements View {

        int touchStartX, touchStartY;

        @Override
        public void signal(ArrayList<SignalEvent> signalList) {
            for (SignalEvent s : signalList) {
                if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                    if (s.signal.press(KeyEvent.ANY_KEY) && s.action == SignalEvent.ACTION_DOWN) {
                        touchStartX = s.signal.x;
                        touchStartY = s.signal.y;
                    }
                    if (s.action == SignalEvent.ACTION_UP) {

                    }
                }
            }
        }

        @Override
        public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list, String build) {
            Hold hold = new Gson().fromJson(build, Hold.class);
            return list;
        }

    }

    class GameModel implements Model {

        public GameModel() {
        }

        @Override
        public String hold() {
            Hold hold = new Hold();
            return new Gson().toJson(hold);
        }

        @Override
        public void process(int command, JSONObject json) throws JSONException {
            switch (command) {
            // TODO
            }

        }

    }

    class Sign {
    }

    class Hold {
    }
  
  
  // 提供跨平台用的程序，目前主要還是透過 Libgdx來實現跨平台
  public LibgdxDriver getApplication() {
      return driver;
  }
}

