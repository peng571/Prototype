package com.makeagame.tools;

import java.util.ArrayList;

import com.makeagame.core.action.Action;

public class PageView extends SimpleLayout {
    
    ArrayList<Button> btns;
    ArrayList<SimpleLayout> contexts;
    
    SimpleLayout currentContext;
    int currentPage = -1;
    
    public PageView(){
        super();
        btns = new ArrayList<Button>();
        contexts = new ArrayList<SimpleLayout>();
    }

    
    public void addTab(Button btn, final SimpleLayout layout){
        
        final int page = btns.size();
        btn.onClickAction = new Action(){
            @Override
            public void execute(){
                System.out.println("change page to "+ page);
                changePage(page);
            }
        };
        btns.add(btn);
        addChild(btn);
        
        layout.XY(0, btn.h + 20);  // TODO 可客製化 padding (可能在上下左右)
        contexts.add(layout);
        
        // 預設顯示第一頁 
        if(currentContext == null){
            currentContext = new SimpleLayout();
            currentContext.copyFrom(layout);
            currentPage = 0;
            addChild(currentContext);
        }
        
    }
    
    // index start from 0
    public void changePage(int i){
        
        // TODO 按鈕按下的動畫也要同時變更
        
        if(currentPage != i){
            SimpleLayout l = contexts.get(i);
            if(l != null){
                currentContext.copyFrom(l);
                currentPage = i;
            }
        }
    }
    
}