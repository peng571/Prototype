package com.makeagame.core.view;

import java.util.ArrayList;

import com.makeagame.core.resource.ResourceManager;
import com.makeagame.tools.Sprite;

/**
 * 使用圖片顯示的數字圖
 * 創建時須帶入0~9的連續數字圖檔
 */
public class NumberView extends BaseViewComponent {
    
    int number;
    String output;

    int numberWidth = 12;
    
    public NumberView(Sprite sprite, int numberWidth) {
        super(sprite);
        output = new String();
    }

    public void setNumber(int num) {
        number = num;
        output = String.valueOf(number);
    }

    @Override
    public ArrayList<RenderEvent> renderSelf(ArrayList<RenderEvent> list) {
        beforeRender();

        int offset = -output.length() * numberWidth;
        for (int i = 0; i < output.length(); i++) {
            int idx = output.codePointAt(i) - '0';
            offset += numberWidth;
            list.add(new RenderEvent(ResourceManager.get().fetch(this.sprite.image))
                    .XY(realX + offset, realY)
                    .src(idx * numberWidth, 0, numberWidth, numberWidth*2)
                    );
        }
        return list;
    }
}
