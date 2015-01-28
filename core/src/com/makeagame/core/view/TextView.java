package com.makeagame.core.view;

import java.util.ArrayList;

import com.makeagame.tools.SimpleLayout;
import com.makeagame.tools.Sprite;

/** 
 * 負責文字的特效處裡
 *
 */
public class TextView extends SimpleLayout {
    
    String word;
    int size;
    boolean bold;
    int color;
    
    // TODO 字體
    //
    
    public TextView(String s){
        super();
        this.word = s;
        size = 16;
        color = 0xffffff;
        
        //sprite = null;
        //children = null;
    }
    
    public TextView Size(int size){
        this.size = size;
        return this;
    }
    
    public TextView Bold(boolean b){
        this.bold = b;
        return this;
    }
    
    public TextView Color(int c){
        this.color = c;
        return this;
    }
    
    @Override
    public ArrayList<RenderEvent> renderSelf(ArrayList<RenderEvent> list) {
        beforeRender();
        list.add(new RenderEvent(word).size(size).color(color).XY(realX, realY));
        return list;
    }
    
    
    @Deprecated
    @Override
    // 此階層的元件不允許再加入子元件
    public SimpleLayout addChild(SimpleLayout sprite) {
        return this;
    }
    
    @Deprecated
    @Override
    // 此類別的元件不允許設置圖片
    public SimpleLayout Sprite(Sprite sp) {
        return this;
    }

    
}
