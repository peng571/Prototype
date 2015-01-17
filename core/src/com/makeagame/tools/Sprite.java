package com.makeagame.tools;

import java.util.ArrayList;
import java.util.HashMap;

import com.makeagame.core.resource.ResourceManager;
import com.makeagame.core.view.RenderEvent;
import com.makeagame.tools.KeyTable.ApplyList;

public class Sprite {
    // 位移
    public int x = 0;
    public int y = 0;

    // 裁切
    public int srcX = 0;
    public int srcY = 0;
    public int srcW = -1;
    public int srcH = -1;
    
    // 縮放, 暫時不管
    //public double scalex = 1.0f;
    //public double scaley = 1.0f;

    // 旋轉, 暫時不管
    //public double rotateAngle = 0.0f;
    
    // 翻轉
    public boolean flipx = false;
    public boolean flipy = false;

    // 請參照 opneGL的 setColor
    public float red = 1.0f;
    public float green = 1.0f;
    public float blue = 1.0f;
    public float alpha = 1.0f;

    // 請參照 opneGL的 blendfunction
    public int srcFunc = -1;
    public int dstFunc = -1;
    
    // 圖片
    public String image;
    // 音效
    public String sound;
    public String palyedSound;
    public float soundVol = 1.0f;
    
    // 該 image 的中心點
    public int centerX;
    public int centerY;
    
    
    public void copyFrom(Sprite sprite) {
        this.x = sprite.x;
        this.y = sprite.y;

        this.srcX = sprite.srcX;
        this.srcY = sprite.srcY;
        this.srcW = sprite.srcW;
        this.srcH = sprite.srcH;
    
        this.flipx = sprite.flipx;
        this.flipy = sprite.flipy;

        this.red = sprite.red;
        this.green = sprite.green;
        this.blue = sprite.blue;
        this.alpha = sprite.alpha;
        
        this.srcFunc = sprite.srcFunc;
        this.dstFunc = sprite.dstFunc;
        
        this.image = sprite.image;
        
        this.centerX = sprite.centerX;
        this.centerY = sprite.centerY;
    }
    
    // 階層系統的子物件
    //public ArrayList<Sprite> children;

    public Sprite(String image) {
        this.image = image;
        this.sound = "";
        this.palyedSound = "";
    }

    // 基準線
    public Sprite() {
        this.image = "";
        this.sound = "";
        this.palyedSound = "";
    }
    
    
    
    public Sprite xy(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public Sprite center(int x, int y) {
        this.centerX = x;
        this.centerY = y;
        return this;
    }
    
    public Sprite srcRect(int x, int y, int w, int h) {
        srcX = x;
        srcY = y;
        srcW = w;
        srcH = h;
        return this;
    }

    public Sprite color(float r, float g, float b, float a) {
        this.red = r;
        this.green = g;
        this.blue = b;
        this.alpha = a;
        return this;
    }

    public Sprite flip(boolean x, boolean y) {
        this.flipx = x;
        this.flipy = y;
        return this;
    }

    // TODO: 自動抓取中心點(從設定檔?)
    public void reset_image(String image) {
        this.image = image;
        this.centerX = 0; // TODO
        this.centerY = 0; // TODO
    }

    public void set(String key, Object value) {
        if (key.equals("image")) { this.image = (String) value; }
        if (key.equals("sound")) { this.sound = (String) value; }
        if (key.equals("x")) { this.x = ((Double) value).intValue();}
        if (key.equals("y")) { this.y = ((Double) value).intValue();}
        if (key.equals("srcX")) { this.srcX = ((Double) value).intValue();}
        if (key.equals("srcY")) { this.srcY = ((Double) value).intValue();}
        if (key.equals("srcW")) { this.srcW = ((Double) value).intValue();}
        if (key.equals("srcH")) { this.srcH = ((Double) value).intValue();}
        if (key.equals("red")) { this.red = ((Double) value).floatValue();}
        if (key.equals("green")) { this.green = ((Double) value).floatValue();}
        if (key.equals("blue")) { this.blue = ((Double) value).floatValue();}
        if (key.equals("alpha")) { this.alpha = ((Double) value).floatValue();}
        if (key.equals("srcFunc")) { this.srcFunc = ((Integer) value).intValue();}
        if (key.equals("dstFunc")) { this.dstFunc = ((Integer) value).intValue();}

    }
    
    public void apply(ApplyList applylist) {
        HashMap<String, Object> map = applylist.map;
        if (map.containsKey("image")) {
            // TODO:
            //reset_image((String) map.get("image"));
            this.image = (String) map.get("image");
        }
        
        if (map.containsKey("sound")) {
            this.sound = (String) map.get("sound");
        }
        if (map.containsKey("x")) {
            x = ((Double) map.get("x")).intValue();
        }
        if (map.containsKey("y")) {
            y = ((Double) map.get("y")).intValue();
        }
        
        if (map.containsKey("srcX")) {
            srcX = ((Double) map.get("srcX")).intValue();
        }
        if (map.containsKey("srcY")) {
            srcY = ((Double) map.get("srcY")).intValue();
        }
        if (map.containsKey("srcW")) {
            srcW = ((Double) map.get("srcW")).intValue();
        }
        if (map.containsKey("srcH")) {
            srcH = ((Double) map.get("srcH")).intValue();
        }
        
        if (map.containsKey("red")) {
            red = ((Double) map.get("red")).floatValue();
        }
        if (map.containsKey("green")) {
            green = ((Double) map.get("green")).floatValue();
        }
        if (map.containsKey("blue")) {
            blue = ((Double) map.get("blue")).floatValue();
        }
        if (map.containsKey("alpha")) {
            alpha = ((Double) map.get("alpha")).floatValue();
        }
        if (map.containsKey("srcFunc")) {
            srcFunc = ((Double) map.get("srcFunc")).intValue();
        }
        if (map.containsKey("dstFunc")) {
            dstFunc = ((Double) map.get("dstFunc")).intValue();
        }
        
        // TODO: .......
    }
    
    private RenderEvent render_event_img = null;
    private RenderEvent render_event_snd = null;
    
    
//    public RenderEvent[] render(int offx, int offy) {
//        //ArrayList<RenderEvent> list = new ArrayList<RenderEvent>();
//        RenderEvent img=null, snd=null;
//        
//        if (image != "") {
//            if (this.render_event_img == null) {
//                this.render_event_img = new RenderEvent(ResourceManager.get().fetch(image));
//            } else {
//                this.render_event_img.Res(ResourceManager.get().fetch(image));
//            }
//            this.render_event_img
//                .XY(offx + x - centerX, offy + y - centerY)
//                .color(red, green, blue, alpha)
//                .src(srcX, srcY, srcW, srcH)
//                .filp(flipx, flipy)
//                .blend(srcFunc, dstFunc);
//            img = this.render_event_img;
//        }
//        if (sound != "" && sound != palyedSound) {
//            //list.add(new RenderEvent("").sound(sound, soundVol));
//            if (this.render_event_snd == null) {
//                this.render_event_snd = new RenderEvent(ResourceManager.get().fetch(sound));
//            } else {
//                this.render_event_snd.Res(ResourceManager.get().fetch(sound));
//            }
//            this.render_event_snd.vol = soundVol;
//            snd = this.render_event_snd;
//            //list.add(re);
//        }
//        palyedSound = sound;
//        
//        if (img != null && snd != null) {
//            return new RenderEvent[]{img, snd};
//        } else if (img != null) {
//            return new RenderEvent[]{img};
//        } else if (snd != null) {
//            return new RenderEvent[]{snd};
//        } else {
//            return new RenderEvent[]{};
//        }
//        //return list;
//    }
    
    public ArrayList<RenderEvent> render(int offx, int offy) {
            ArrayList<RenderEvent> list = new ArrayList<RenderEvent>();
            RenderEvent img=null, snd=null;
            
            if (image != "") {
                if (this.render_event_img == null) {
                    this.render_event_img = new RenderEvent(ResourceManager.get().fetch(image));
                } else {
                    this.render_event_img.Res(ResourceManager.get().fetch(image));
                }
                this.render_event_img
                    .XY(offx + x - centerX, offy + y - centerY)
                    .color(red, green, blue, alpha)
                    .src(srcX, srcY, srcW, srcH)
                    .filp(flipx, flipy)
                    .blend(srcFunc, dstFunc);
                img = this.render_event_img;
            }
            if (sound != "" && sound != palyedSound) {
                //list.add(new RenderEvent("").sound(sound, soundVol));
                if (this.render_event_snd == null) {
                    this.render_event_snd = new RenderEvent(ResourceManager.get().fetch(sound));
                } else {
                    this.render_event_snd.Res(ResourceManager.get().fetch(sound));
                }
                this.render_event_snd.vol = soundVol;
                snd = this.render_event_snd;
                //list.add(re);
            }
            palyedSound = sound;
            
           if (img != null) {
                 list.add(img);
            } if (snd != null) {
                 list.add(snd);
            }
            return list;
        }

}
