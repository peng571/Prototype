package com.makeagame.core;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.makeagame.core.model.Model;
import com.makeagame.core.plugin.libgdx.LibgdxDriver;
import com.makeagame.core.plugin.libgdx.LibgdxProcessor;
import com.makeagame.core.resource.ResourceSystem;
import com.makeagame.core.resource.process.RegisterFinder;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.core.view.View;
import com.makeagame.core.view.component.BaseViewComponent;
import com.makeagame.tools.Sprite;


/**
 * Enter Point
 * 在這邊為測試用進入點
 */
public class MakeAGame {
  private LibgdxDriver driver;
  
  public MakeAGame() { 

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
      registerResource(finder);
      
      LibgdxProcessor processor = new LibgdxProcessor(finder);
      rs.addProcessor(processor);
  }
  
  
  class GameView implements View{

    BaseViewComponent picture;
    
    public GameView(){
        picture = new BaseViewComponent().withSprite(new Sprite().setImage("image")).withXY(0, 0);
    }
      
    @Override
    public void signal(ArrayList<SignalEvent> s) throws JSONException {
        
    }

    @Override
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list, String s) {
        picture.render(list);
        return list;
    }
      
  }
  
  class GameModel implements Model{

    @Override
    public void process(int command, JSONObject json) throws JSONException {
        // TODO Auto-generated method stub
        
    }

    @Override
    public String hold() {
        // TODO Auto-generated method stub
        return null;
    }
      
  }
  
  

  /*
   * 可讓使用者自行選擇包裝資訊的方法
   * 這邊用JSONObject來包裝資訊
   */
  private JSONObject packageData(String path) throws JSONException{
      return new JSONObject().put("path", path);
  }

  private JSONObject packageData(String path, int x, int y, int w, int h) throws JSONException{
      return packageData(path).put("type", "img").put("x", x).put("y", y).put("w", w).put("h", h);
  }

  private JSONObject packageData(String path, String type) throws JSONException{
      return packageData(path).put("type", type);
  }
  
  
  
  private void registerResource(RegisterFinder finder) {
      try {
          /* IMAGE */
          finder.register("image", packageData("image/example.png", 0, 0, 322, 467));

          /* ATTRIBUTE */
          finder.register("attribute", packageData("data/bird.txt", "atr"));
  
          /* SOUND */
//          finder.register("sound", packageData("sound/button-50.mp3", "snd"));
      } catch (JSONException e) {
          e.printStackTrace();
      }
  }

  
  
  // 提供跨平台用的程序，目前主要還是透過 Libgdx來實現跨平台
  public LibgdxDriver getApplication() {
      return driver;
  }
}
