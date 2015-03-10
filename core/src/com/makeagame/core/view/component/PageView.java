package com.makeagame.core.view.component;

import java.util.ArrayList;

import com.makeagame.core.model.Action;

public class PageView extends BaseViewLayout {
    
    ArrayList<Button> btns;
    ArrayList<BaseViewLayout> contexts;
    
    int currentPage = -1;  // index start from 0
    
    public PageView(){
        super();
        btns = new ArrayList<Button>();
        contexts = new ArrayList<BaseViewLayout>();
    }

    
    public void addTab(Button btn, final BaseViewLayout layout){
        
        final int page = btns.size();
        btn.onClickAction = new Action(){
            @Override
            public void execute(){
                changePage(page);
            }
        };
        btns.add(btn);
        addChild(btn);
        
        layout.withXY(0, btn.h + 30);  // TODO 可客製化 padding (可能在上下左右)
        // 預設顯示第一頁 
        if(currentPage == -1){
            currentPage = 0;
        }else{
            layout.visible = false;
        }
        contexts.add(layout);
        addChild(layout);
        
    }
    
    // index start from 0
    public void changePage(int i){
        
        // TODO 按鈕按下的動畫也要同時變更
        
        if(currentPage != i){
            BaseViewLayout tmpl;
            tmpl = contexts.get(currentPage);
            tmpl.visible = false;
            tmpl = contexts.get(i);
            if(tmpl != null){
                tmpl.visible = true;
                currentPage = i;
            }
        }
    }
    
    
    public int getCurrentPage(){
        return currentPage;
    }
    
    public int getPageCount(){
        return contexts.size();
    }
    
    
    
    
    @Override 
    public void beforeReslove(){
        // TODO 可客製化 padding (可能在上下左右)
    }
    
}