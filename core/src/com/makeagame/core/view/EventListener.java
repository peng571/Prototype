package com.makeagame.core.view;

import java.util.ArrayList;

import com.makeagame.core.view.SignalEvent.Signal;
import com.makeagame.tools.KeyTable;
import com.makeagame.tools.SimpleLayout;
import com.makeagame.tools.State;

// 取代Button
public class EventListener {

    /**
     * 可能的狀態:
     * 定性狀態:
     * Disable 時按鈕不能觸發, 且按鈕顯示灰色
     * Activate
     * InActivate 時按鈕不能觸發, 而且顯示為準備中
     * InVisible 時按鈕能觸發, 但不顯示
     * Gone 時按鈕不顯示, 也不能觸發
     * Hovered 代表滑鼠(指標)在感應區內時(行動平台上無效)
     * Pushed 代表被按著時
     */
    public static final int Invisible = 0;
    public static final int Visible = 1;
    public static final int Gone = 2;
    
    public static final int Disable = 0;
    public static final int Enable = 1;
    public static final int Active = 2;
    public static final int Inactive = 3;
    
    public static final int Static = 0;
    public static final int Hovered = 1;
    public static final int Pushed = 2;
    
    
    public State visible_state;
    public State enable_state;
    public State action_state;

    public EventListener()
    {
        action_state = new State(new long[][] {
                // Static Hovered Pushed
                { State.BLOCK, State.ALLOW, State.ALLOW },
                { State.ALLOW, State.BLOCK, State.ALLOW },  // mobile do not have Hovered
                { State.BLOCK, State.ALLOW, State.BLOCK }
        });
        action_state.reset(Static);
        
        visible_state = new State(new long[][] {
                // Invisible Visible Gone
                { State.BLOCK, State.ALLOW, State.ALLOW },
                { State.ALLOW, State.BLOCK, State.ALLOW },
                { State.ALLOW, State.ALLOW, State.BLOCK}
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

    public void setVisible(int visibleState) {
       switch(visibleState){
       case Visible:
            visible_state.enter(Visible);
            enable_state.enter(Enable);
            break;
       case Invisible:
            visible_state.enter(Invisible);
            enable_state.enter(Enable);
            break;
       case Gone:
           visible_state.enter(Gone); 
           enable_state.enter(Disable);
           break;
        }
    }

    public boolean isInArea(int _x, int _y) {
        if (_x > x && _x < x + w && _y > y && _y < y + h) {
            return true;
        }
        return false;
    }

    public void OnMouseIn(Signal s) {
     // Override to add method
      //  System.out.println("OnMouseIn");
    }

    public void OnMouseOut(Signal s) {
     // Override to add method
      //  System.out.println("OnMouseOut");
    }

    public void OnMouseDown(Signal s) {
     // Override to add method
       // System.out.println("OnMouseDown");
    }

    public void OnMouseUp(Signal s) {
     // Override to add method
      //  System.out.println("OnMouseUp");
    }
    
    public void OnMouseMove(Signal s){
       // Override to add method
    }

    // @Override
    public void signal(ArrayList<SignalEvent> signalList) {
        if (enable_state.currentStat() != Active) {
            action_state.reset(Static);
            return;
        }
        for (SignalEvent s : signalList) {
            if (s.type == SignalEvent.MOUSE_EVENT || s.type == SignalEvent.TOUCH_EVENT) {
                if (isInArea(s.signal.x, s.signal.y)) {
                    
                    // 改成相對位置
                    s.signal.x -= x;
                    s.signal.y -=y;
                    
                    if (s.action == SignalEvent.ACTION_MOVE) {
                        if (action_state.enter(Hovered)) {
                            OnMouseIn(s.signal);
                        } else {
                            OnMouseMove(s.signal);
                        }
                    } else if (s.action == SignalEvent.ACTION_DOWN) {
                        if (action_state.enter(Pushed)) {
                            OnMouseDown(s.signal);
                        }
                    } else if (s.action == SignalEvent.ACTION_UP) {
                        if (action_state.enter(Hovered)) {
                            OnMouseUp(s.signal);
                        }
                    }
                } else {
                    if (action_state.enter(Static)) {
                        OnMouseOut(s.signal);
                    }
                }
            }
        }
    }
}