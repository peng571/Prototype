package com.makeagame.core.view;

import java.util.ArrayList;
import java.util.HashMap;

import com.makeagame.core.model.EventListener;
import com.makeagame.tools.KeyTable.ApplyList;
import com.makeagame.tools.Sprite;


/** 
 * 可繪製的View 元件
 * 所有的View元件都應該繼承自此類別
 * (介面類似原本的SimpleLayout，但更為單純)
 */
public class BaseViewComponent {
    
    // 可繪製圖片，預設質皆為null
    public Sprite sprite;
    public Sprite backgroundSprite;
    
    // 畫面中實際值
    public int realX;
    public int realY;
        
    // 定值
    public int fixedX;
    public int fixedY;
    
    // 相對位置
    public int relativeX;
    public int relativeY;
    
    // 位移值(給動畫用)
    public int animX;
    public int animY;
    
    public boolean visible = true;
    
    public EventListener listener;

    
    public BaseViewComponent() {
    }
    
    public BaseViewComponent(Sprite sprite) {
        this();
        this.sprite = sprite;
    }
    
    public BaseViewComponent(Sprite backgroundSprite, Sprite sprite){
        this(sprite);
        this.backgroundSprite = backgroundSprite;
    }
    
    
    public BaseViewComponent Sprite(Sprite sp)    {
        this.sprite = sp;
        return this;
    }
    
    public BaseViewComponent background(Sprite bgSprite){
        this.backgroundSprite = bgSprite;
        return this;
    }
    
    
    public BaseViewComponent XY(int x, int y) {
        this.fixedX = x;
        this.fixedY = y;
        return this;
    }
    
    
    public void signal(ArrayList<SignalEvent> s){
        if(listener != null){
            listener.signal(s);
        }
    }
    
    
    
    
    public void apply(ApplyList applylist) {
        HashMap<String, Object> map = applylist.map;
        if (map.containsKey("x")) {
            animX = ((Double) map.get("x")).intValue();
        }
        if (map.containsKey("y")) {
            animY = ((Double) map.get("y")).intValue();
        }
        
        for(String name : applylist.map.keySet()){
            Object value = applylist.map.get(name);
            if (name.contains(".")) {
                String[] bArray = name.split("[.]", 2);
                //Engine.logI(name);
                //Engine.logI(bArray[0] + "  " + bArray[1]);
                if (bArray[0].equals("")) {
                    //Engine.logI(bArray[0] + "  " + bArray[1]);
                    this.sprite.set(bArray[1], value);
                } else {
                    int idx = Integer.parseInt(bArray[0]);
                }
            }
        }    
    }
    
    
    public void beforeReslove() {
     // Override to use it, if you need
    }
    
    public void beforeRender() {
     // Override to use it, if you need
    }
    
    
    public void reslove(int offx, int offy) {
        beforeReslove();
        realX = fixedX + offx;
        realY = fixedY + offy;
    }
    
    
    
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list) {
        renderSelf(list);
        return list;
    }
    
    
    public ArrayList<RenderEvent> renderSelf(ArrayList<RenderEvent> list) {
        beforeRender();
        if(backgroundSprite != null){
            backgroundSprite.render(list, realX + animX, realY + animY);
        }
        if(sprite != null){
            sprite.render(list, realX + animX, realY + animY);
        }
        return list;
    }

}
