package com.makeagame.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.makeagame.core.action.EventListener;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;
import com.makeagame.tools.KeyTable.ApplyList;

public class SimpleLayout {
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
    
    // 階層系統的子物件
    public ArrayList<SimpleLayout> children;
    public Sprite sprite;
    
    public SimpleLayout() {
        sprite = new Sprite();
        children = new ArrayList<SimpleLayout>();
    }
    
    public SimpleLayout(Sprite sprite) {
        this();
        this.sprite = sprite;
    }
    
    public SimpleLayout copy() {
        return new SimpleLayout().copyFrom(this);
    }
    
    public SimpleLayout copyFrom(SimpleLayout other) {
        this.fixedX = other.fixedX;
        this.fixedY = other.fixedY;
        this.animX = other.animX;
        this.animY = other.animY;
        this.visible = other.visible;
        this.sprite.copyFrom(other.sprite);
        
        if (this.children.size() == other.children.size()) {
            for(int i=0; i < other.children.size(); i++) {
                this.children.get(i).copyFrom(other.children.get(i));
            }
        } else {
            this.cleanAllChildren();
            for(int i=0; i < other.children.size(); i++) {
                SimpleLayout n = new SimpleLayout();
                n.copyFrom(other.children.get(i));
                this.addChild(n);
            }
        }
        return this;
    }
    
    
    public SimpleLayout Sprite(Sprite sp)    {
        this.sprite = sp;
        return this;
    }
    
    
    public SimpleLayout XY(int x, int y) {
        this.fixedX = x;
        this.fixedY = y;
        return this;
    }
    
    
    public SimpleLayout addChild(SimpleLayout layout) {
        if (children == null) {
            children = new ArrayList<SimpleLayout>();
        }
        children.add(layout);
        
        
        // 只在新增Child時重新計算
        reslove(0, 0);
        
        return this;

    }

    public void cleanAllChildren() {
        if (children != null) {
            children.clear();
        }
        children = null;
    }
    
    
    public void signal(ArrayList<SignalEvent> s){
        if (children != null) {
            for(SimpleLayout c : children){
                
                if(c instanceof Button){
                // Button有三種狀態(Gone, Invisible, Visible)，需要特別處裡
                   if(((Button)c).visible_state.currentStat() != Button.Gone){
                       c.signal(s);
                   }
                } else if (c.visible) {
                   c.signal(s);
               } 
            }
        }
        
        // TODO (擴充)階層式觸發事件   e.x. public boolean execute(){}
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
        //Iterator<String> it = applylist.map.keySet().iterator();
        //while (it.hasNext()) {
            //String name = it.next();
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
                    this.children.get(idx).sprite.set(bArray[1], value); 
                }
            }
        }    
    }
    
    
    /**
     *  NOTE:
     *  建議把整體架構改成，
     *  在Create View的時候就同時決定位置，
     *  所有的定位都使用XY() 來決定相對中的絕對位置，
     *  只有部分的View會根據apply所給定的動畫為移值來改變位置(但無法引響其子物件)
     *  
     *  這樣是否足以應付遊戲介面的變動性?
     *  
     */
    
    
    public void beforeReslove() {
        
    }
    
    public void beforeRender() {
        
    }
    
    
    public void reslove(int offx, int offy) {
        realX = fixedX + offx;
        realY = fixedY + offy;
        if (children != null) {
            for (SimpleLayout c : children) {
                if(c instanceof Button){
                    // TODO 需要修改SimpleLayout和特殊繼承元件的關聯
                    ((Button) c).XY(realX, realY);
                } else{
                    c.reslove(realX, realY);
                }
            }
        }
    }
    
    
    public void resloveSelf(){
        beforeReslove();
        //realX += animX;
        //realY += animY;
    }
    
    
    public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list) {
        resloveSelf();
        renderSelf(list);
        
        if (children != null) {
            for (SimpleLayout c : children) {
                if (c.visible) {
                   c.render(list);
               }
            }
        }
        return list;
    }
    
    public ArrayList<RenderEvent> renderSelf(ArrayList<RenderEvent> list) {
        beforeRender();
        return sprite.render(list, realX + animX, realY + animY);
    }
    
}
