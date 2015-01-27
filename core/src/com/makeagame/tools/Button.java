package com.makeagame.tools;

import com.makeagame.core.action.EventListener;
import com.makeagame.core.action.Action;
import com.makeagame.core.view.SignalEvent.Signal;


/**
 * 包含 EventListener 的按鈕型 SimpleLayout
 */
public class Button extends SimpleLayout{
    /**
     * 可能的狀態:
     * 定性狀態:
     * InVisible 時按鈕能觸發， 但不顯示
     * Visable 時按鈕能正常觸發
     * Gone 時按鈕無法觸發，且不顯示
     */
    public static final int Invisible = 0;
    public static final int Visible = 1;
    public static final int Gone = 2;
    
    public static final long clickTime = 300;
    
    // 進度條, 當達到 1.0 時進入Active, 不到時退回Inactive
    static double progress = 1.0;
    public State visible_state;

    // 
    int x, y, w, h;
    
    public Action onClickAction;
    public Action onLongClickAction;
   
    // TODO 按鈕的前景背景可分離
    SimpleLayout backgrountLayout;
    
    // 暫時不支援空的建構子
//    public Button() {
//        super();
//    }

    public Button(Sprite s){
        super(s);
        
        listener = new EventListener(){

            Long clickTime = -1l;
            
            @Override
            public void OnMouseDown(Signal s) {
                clickTime = System.currentTimeMillis();
            }
            
            @Override
            public void OnMouseOut(Signal s) {
                clickTime = -1l;
            }
            
            @Override
            public void OnMouseUp(Signal s) {
                if(clickTime > 0 && System.currentTimeMillis() - clickTime < Button.clickTime){
                    onClick();
                } else {
                    onLongClick();
                }
            }
        };
        
        visible_state = new State(new long[][] {
                // Visible Invisible Gone
                { State.BLOCK, State.ALLOW, State.ALLOW},
                { State.ALLOW, State.BLOCK, State.ALLOW},
                { State.ALLOW, State.ALLOW, State.BLOCK}
        });
        visible_state.enter(Visible);
        
        // TODO 最好能有通用的預設按鈕背景
        backgrountLayout = new SimpleLayout();
        
    }
    
    @Override
    @Deprecated
    // 改用 setRectArea 來設置按鈕的位置
    public SimpleLayout XY(int x, int y){
        return this;
    }


    // 按鈕監聽的範圍，這個一定要設
    public Button RectArea(int x, int y, int w, int h) {
        super.XY(x, y);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        
        // TODO 有沒有辦法再自動取得圖片的大小來設置按鈕位置的預設值?
        
        listener.setRectArea(x, y, w, h);
        return this;
    }
    
    public void onClick(){
        if(onClickAction != null){
            onClickAction.execute();
        }
    }
    
    public void onLongClick(){
        if(onLongClickAction != null){
            onLongClickAction.execute();
        }else{
            // 預設是和 onClick 執行同樣動作
            onClick();
        }
    }
    
    
    // 
//    public void setOnClickAction(Action action){
//        
//    }
//    
//    public void setOnLongClickAction(Action action){
//        
//    }
    
    public void setEnable(boolean bool) {
        listener.setEnable(bool);
    }

    public void setVisible(int visibleState) {
        switch(visibleState){
        case Visible:
            if(visible_state.enter(Visible)) {
                visible = true;
                setEnable(true);
            }
            break;
        case Invisible:
             if(visible_state.enter(Invisible)) {
                 visible = false;
                 setEnable(true);
             }
             break;
        case Gone:
            if(visible_state.enter(Gone)) { 
                visible = false;
                setEnable(false);
            }
            break;
         }
     }

    SimpleLayout spDisable;
    SimpleLayout spInactive;
    SimpleLayout spStatic;
    SimpleLayout spHovered;
    SimpleLayout spPushed;
    KeyTable ktDisable;
    KeyTable ktInactive;
    KeyTable ktStatic;
    KeyTable ktHovered;
    KeyTable ktPushed;

    public void setDisableSprite(SimpleLayout sprite) {
        spDisable = sprite;
    }

    public void setDisableAnimation(KeyTable keytable) {
        ktDisable = keytable;
    }

    public void setInactiveSprite(SimpleLayout sprite) {
        spInactive = sprite;
    }

    public void setInactiveAnimation(KeyTable keytable) {
        ktInactive = keytable;
    }

    public void setStaticSprite(SimpleLayout sprite) {
        spStatic = sprite;
    }

    public void setStaticAnimation(KeyTable keytable) {
        ktStatic = keytable;
    }

    public void setHoveredSprite(SimpleLayout sprite) {
        spHovered = sprite;
    }

    public void setHoveredAnimation(KeyTable keytable) {
        ktHovered = keytable;
    }

    public void setPushedSprite(SimpleLayout sprite) {
        spPushed = sprite;
    }

    public void setPushedAnimation(KeyTable keytable) {
        ktPushed = keytable;
    }

    public void setActiveSprite(SimpleLayout sprite) {
        spStatic = sprite;
        spHovered = sprite;
        spPushed = sprite;
    }

    public void setActiveAnimation(KeyTable keytable) {
        ktStatic = keytable;
        ktHovered = keytable;
        ktPushed = keytable;
    }

    public void apply(SimpleLayout sprite) {
        
        State enable_state = listener.enable_state;
        State action_state = listener.action_state;
        
        /*
         * if (progress >= 1.0) {
         * enable_state.enter(Active);
         * } else {
         * enable_state.enter(Inactive);
         * }
         */
        // 如果 invisible則不顯示
        // TODO: 之後在做
        // ArrayList<RenderEvent> renderlist = new ArrayList<RenderEvent>();
        
        if (visible_state.currentStat() == Invisible) {
            return;
        }
        switch (enable_state.currentStat())
        {
        case EventListener.Disable:
            if (spDisable != null) { sprite.copyFrom(spDisable); }
            if (ktDisable != null) {
                sprite.apply(ktDisable.get(enable_state.elapsed()));
            }
            break;
        case EventListener.Inactive:
            if (spInactive != null) { sprite.copyFrom(spInactive); }
            if (ktInactive != null) {
                sprite.apply(ktInactive.get(enable_state.elapsed()));
            }
            break;
        case EventListener.Active:
            switch (action_state.currentStat())
            {
            case EventListener.Static:
                if (spStatic != null) { sprite.copyFrom(spStatic); }
                //Engine.logI("stat: " + Long.toString(action_state.elapsed()));
                break;
                // mobile do not have Hovered
            case EventListener.Hovered:
                if (spHovered != null) { sprite.copyFrom(spHovered); }
                break;
            case EventListener.Pushed:
                if (spPushed != null) { sprite.copyFrom(spPushed); }
                break;
            }
            if (ktStatic != null) {
                sprite.apply(ktStatic.get(action_state.elapsed(EventListener.Static)));
            }
            // mobile do not have Hovered
            if (ktHovered != null) {
                sprite.apply(ktHovered.get(action_state.elapsed(EventListener.Hovered)));
            }
            if (ktPushed != null) {
                sprite.apply(ktPushed.get(action_state.elapsed(EventListener.Pushed)));
            }
            break;
        }
        
    }
}