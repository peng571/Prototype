package com.makeagame.tools;

import java.util.ArrayList;

import com.makeagame.core.view.SignalEvent;

public class Button {// implements View {
    /**
     * 可能的狀態:
     * 定性狀態:
     * Disable disable 時按鈕不能觸發, 且按鈕顯示灰色
     * Activate
     * Inactivate InActivate 時按鈕不能觸發, 而且顯示為準備中
     * Invisible InVisible 時按鈕能觸發, 且不顯示
     * Hovered 代表滑鼠(指標)在感應區內時(行動平台上無效)
     * Pushed 代表被按著時
     */
    public static final int Invisible = 0;
    public static final int Visible = 1;
    public static final int Disable = 0;
    public static final int Enable = 1;
    public static final int Active = 2;
    public static final int Inactive = 3;
    public static final int Static = 0;
//    public static final int Hovered = 1;
    public static final int Pushed = 1;
    
    // 進度條, 當達到 1.0 時進入Active, 不到時退回Inactive
    static double progress = 1.0;
    public State visible_state;
    public State enable_state;
    public State action_state;

    public Button()
    {
        // mobile do not have Hovered
        action_state = new State(new long[][] {
                // Static Pushed
                { State.BLOCK, State.ALLOW },
                { State.ALLOW, State.BLOCK }
        });
//        action_state = new State(new long[][] {
//                // Static Hovered Pushed
//                { State.BLOCK, State.ALLOW, State.BLOCK },
//                { State.ALLOW, State.BLOCK, State.ALLOW },
//                { State.BLOCK, State.ALLOW, State.BLOCK }
//        });
        action_state.reset(Static);
        visible_state = new State(new long[][] {
                // Invisible Visible
                { State.BLOCK, State.ALLOW },
                { State.ALLOW, State.BLOCK }
        });
        visible_state.reset(Visible);
        // 只能由Inactive->Active
        // Enable 是過渡狀態
        enable_state = new State(new long[][] {
                // Disable Enable Active Inactive
                { State.BLOCK, State.ALLOW, State.BLOCK, State.BLOCK },
                { State.ALLOW, State.BLOCK, State.ALLOW, State.ALLOW },
                { State.ALLOW, State.BLOCK, State.BLOCK, State.ALLOW },
                { State.ALLOW, State.BLOCK, State.ALLOW, State.BLOCK }
        });
        enable_state.reset(Active);
    }

    int x, y, w, h;

    public void setRectArea(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void setEnable(boolean bool) {
        if (bool) {
            enable_state.enter(Enable);
        } else {
            enable_state.enter(Disable);
        }
    }

    public void setVisible(boolean bool) {
        if (bool) {
            visible_state.enter(Visible);
        } else {
            visible_state.enter(Invisible);
        }
    }

    public boolean isInArea(int _x, int _y) {
        if (_x > x && _x < x + w && _y > y && _y < y + h) {
            return true;
        }
        return false;
    }

    public void OnMouseIn() {
    }

    public void OnMouseOut() {
    }

    public void OnMouseDown() {
    }

    public void OnMouseUp() {
    }

    // @Override
    public void signal(ArrayList<SignalEvent> signalList) {
        if (enable_state.currentStat() != Active) {
            // Engine.logI("es: " + Integer.toString(enable_state.currentStat()));
            action_state.reset(Static);
            return;
        }
        for (SignalEvent s : signalList) {
            if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                // Engine.logI(Integer.toString(s.signal.x));
                if (isInArea(s.signal.x, s.signal.y)) {
                    if (s.action == SignalEvent.ACTION_MOVE) {
//                        if (action_state.enter(Hovered)) {
//                            OnMouseIn();
//                        }
                    } else if (s.action == SignalEvent.ACTION_DOWN) {
                        if (action_state.enter(Pushed)) {
                            OnMouseDown();
                        }
                    } else if (s.action == SignalEvent.ACTION_UP) {
//                        if (action_state.enter(Hovered)) {
//                            OnMouseUp();
//                        }
                    }
                } else {
                    if (action_state.enter(Static)) {
                        OnMouseOut();
                    }
                }
            }
        }
    }

    // @Override
    public String info() {
        return "button view";
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

    // @Override
    // public ArrayList<RenderEvent> render(ArrayList<String> build) {
    public void apply(SimpleLayout sprite) {
        /*
         * if (progress >= 1.0) {
         * enable_state.enter(Active);
         * } else {
         * enable_state.enter(Inactive);
         * }
         */
        // �p�G invisible�h�����
        // TODO: ����b��
        // ArrayList<RenderEvent> renderlist = new ArrayList<RenderEvent>();
        if (visible_state.currentStat() == Invisible) {
            return;
        }
        switch (enable_state.currentStat())
        {
        case Disable:
            if (spDisable != null) { sprite.copyFrom(spDisable); }
            if (ktDisable != null) {
                sprite.apply(ktDisable.get(enable_state.elapsed()));
            }
            break;
        case Inactive:
            if (spInactive != null) { sprite.copyFrom(spInactive); }
            if (ktInactive != null) {
                sprite.apply(ktInactive.get(enable_state.elapsed()));
            }
            break;
        case Active:
            switch (action_state.currentStat())
            {
            case Static:
                if (spStatic != null) { sprite.copyFrom(spStatic); }
                //Engine.logI("stat: " + Long.toString(action_state.elapsed()));
                break;
                // mobile do not have Hovered
//            case Hovered:
//                if (spHovered != null) { sprite.copyFrom(spHovered); }
//                break;
            case Pushed:
                if (spPushed != null) { sprite.copyFrom(spPushed); }
                break;
            }
            if (ktStatic != null) {
                sprite.apply(ktStatic.get(action_state.elapsed(Static)));
            }
            // mobile do not have Hovered
//            if (ktHovered != null) {
//                sprite.apply(ktHovered.get(action_state.elapsed(Hovered)));
//            }
            if (ktPushed != null) {
                sprite.apply(ktPushed.get(action_state.elapsed(Pushed)));
            }
            break;
        }
        
    }
}