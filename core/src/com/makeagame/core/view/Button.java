package com.makeagame.core.view;

import java.util.ArrayList;

import com.makeagame.core.model.Action;
import com.makeagame.core.model.EventListener;
import com.makeagame.core.view.SignalEvent.Signal;
import com.makeagame.tools.KeyTable;
import com.makeagame.tools.SimpleLayout;
import com.makeagame.tools.Sprite;
import com.makeagame.tools.State;

/**
 * 包含 EventListener 的按鈕型 SimpleLayout
 */
public class Button extends BaseViewComponent {

    public static final long clickTime = 300;

    // 進度條, 當達到 1.0 時進入Active, 不到時退回Inactive
    static double progress = 1.0;

    // RecrArea Value
    int x, y, w, h;

    public Action onClickAction;
    public Action onLongClickAction;

    // 暫時不支援空的建構子
    // public Button() {
    // super();
    // }

    public Button(Sprite bgS, Sprite s) {
        super(s);

        listener = new EventListener() {

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
                if (clickTime > 0 && System.currentTimeMillis() - clickTime < Button.clickTime) {
                    onClick();
                } else {
                    onLongClick();
                }
            }
        };

        backgroundSprite = bgS;
    }

    public Button(Sprite s) {
        this(null, s);
    }

    @Override
    @Deprecated  // 不建議使用者使用此方法來設定按鈕位置，改用RectArea
    public Button XY(int x, int y) {
        super.XY(x, y);
        this.x = x;
        this.y = y;
        return this;
    }

    // 按鈕監聽的範圍，這個一定要設
    public Button XY(int x, int y, int w, int h) {
        super.XY(x, y);
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        // TODO 有沒有辦法再自動取得圖片的大小來設置按鈕位置的預設值?

        listener.setRectArea(x, y, w, h);
        return this;
    }

    public void onClick() {
        if (onClickAction != null) {
            onClickAction.execute();
        }
    }

    public void onLongClick() {
        if (onLongClickAction != null) {
            onLongClickAction.execute();
        } else {
            // 預設是和 onClick 執行同樣動作
            onClick();
        }
    }

    //
    // public void setOnClickAction(Action action){
    //
    // }
    //
    // public void setOnLongClickAction(Action action){
    //
    // }

    public void setEnable(boolean bool) {
        listener.setEnable(bool);
    }

    
    @Override
    public void signal(ArrayList<SignalEvent> s){
        if(listener != null){
            listener.signal(s);
        }
    }
    
    
    @Override
    public void reslove(int offx, int offy) {
        beforeReslove();
        realX = fixedX + offx;
        realY = fixedY + offy;
        listener.setRectArea(realX, realY, w, h);
    }
    
    
    
    
    
    
    /**
     * 以下是動畫相關
     */
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
    
    // 這邊太過複雜了
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

        if (!visible) {
            return;
        }
        switch (enable_state.currentStat())
        {
        case EventListener.Disable:
            if (spDisable != null) {
                sprite.copyFrom(spDisable);
            }
            if (ktDisable != null) {
                sprite.apply(ktDisable.get(enable_state.elapsed()));
            }
            break;
        case EventListener.Inactive:
            if (spInactive != null) {
                sprite.copyFrom(spInactive);
            }
            if (ktInactive != null) {
                sprite.apply(ktInactive.get(enable_state.elapsed()));
            }
            break;
        case EventListener.Active:
            switch (action_state.currentStat())
            {
            case EventListener.Static:
                if (spStatic != null) {
                    sprite.copyFrom(spStatic);
                }
                // Engine.logI("stat: " + Long.toString(action_state.elapsed()));
                break;
            // mobile do not have Hovered
            case EventListener.Hovered:
                if (spHovered != null) {
                    sprite.copyFrom(spHovered);
                }
                break;
            case EventListener.Pushed:
                if (spPushed != null) {
                    sprite.copyFrom(spPushed);
                }
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