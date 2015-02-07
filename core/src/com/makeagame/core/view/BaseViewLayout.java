package com.makeagame.core.view;

import java.util.ArrayList;

import com.makeagame.tools.Sprite;

/**
 * 可容納多個子元素<BaseViewComponent> 的標準物件
 */
public class BaseViewLayout extends BaseViewComponent {

        // 階層系統的子物件
        public ArrayList<BaseViewComponent> children;
        
        public BaseViewLayout() {
            super();
            children = new ArrayList<BaseViewComponent>();
        }
        
        public BaseViewLayout(Sprite bgSprite) {
            this();
            // 若有圖片則帶入為背景 
            // UNSURE: 使用前景背景在這邊似乎沒差，但使用背景似乎比較合乎一般邏輯?
            super.background(bgSprite);
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
        
        public BaseViewLayout addChild(BaseViewComponent layout) {
            if (children == null) {
                children = new ArrayList<BaseViewComponent>();
            }
            children.add(layout);
            
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
                for(BaseViewComponent c : children){
                    if (c.visible) {
                       c.signal(s);
                   } 
                }
            }
            
            // TODO (擴充)階層式觸發事件   e.x. public boolean execute(){}
            if(listener != null){
                listener.signal(s);
            }
        }
        
        
        // 需在頁面編排完成時，手動呼叫
        public void reslove(int offx, int offy) {
            realX = fixedX + offx;
            realY = fixedY + offy;
            if (children != null) {
                for (BaseViewComponent c : children) {
                    c.reslove(realX, realY);
                }
            }
        }
        
        
        public ArrayList<RenderEvent> render(ArrayList<RenderEvent> list) {
            renderSelf(list);
            
            if (children != null) {
                for (BaseViewComponent c : children) {
                    if (c.visible) {
                       c.render(list);
                   }
                }
            }
            return list;
        }
        

        
        
        
        
        
        
        
        // TODO: 可擴充方向
        
        // 自動排列子元素的 RawLayout
        class RawLayout{
//            public enum ORIENTATION{
//                HORIZONTTAL, // 水平
//                VERTICAL // 垂直
//            }
        }
        
        // 依(相對於此物件的重心)絕對位置定位排列
        class AbsoluteLayout{
            
        }
        

}
