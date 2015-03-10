package com.makeagame.core.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.makeagame.core.resource.Resource;

public class RenderEvent {

    public Resource res;
    
    public int type;
    public String s;
    
    public float x = 0;
    public float y = 0;
    
    public int srcX = 0;
    public int srcY = 0;
    public int srcW = -1;
    public int srcH = -1;
    
    public int dstX;
    public int dstY;
    public int dstW;
    public int dstH;
    
    public float ratioX;
    public float ratioY;
    
    public float angle;
    
    public int gravity;
    
    public Color color;
    public int size;
    public boolean useBlend = false;
    public int srcFunc, dstFunc;
    public boolean flipX = false;
    public boolean flipY = false;
    
    public float vol;
    
    public static final int UNKNOW = 0;// 未指定類別的資源
    public static final int IMAGE = 0x001;
    public static final int LABEL = 0x002;
    public static final int SOUND = 0x004;
    
    private RenderEvent() {
        angle = 0;
        XY(0, 0);
        srcWH(-1, -1);
        gravity = 0;
        this.color = new Color(Color.WHITE);
    }

    public RenderEvent(String s) {
        this();
        this.type = LABEL;
        this.s = s;
    }
    
    public RenderEvent(Resource res) {
        this();
        Res(res);
    }

    
    public RenderEvent Res(Resource res) {
        this.res = res;
        this.type = res.type;
        
        switch(type){
        case IMAGE:
            int[] WH = res.getWH();
            srcH = WH[0];
            dstH = srcH;
            srcW = WH[1];
            dstW = srcW;
            break;
            
        case SOUND:
//            this.vol = res.vol;
            
        }
        
        return this;
    }
    
    public RenderEvent XY(float x, float y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public RenderEvent srcWH(int w, int h) {

        this.srcW = w;
        this.srcH = h;
        //this.dstH = h;
        //this.dstW = w;
        //this.ratioX = 1f;
        //this.ratioY = 1f;
        return this;
    }

    public RenderEvent src(int x, int y, int w, int h) {
        this.srcX = x;
        this.srcY = y;
        this.srcW = w;
        this.srcH = h;
        /*
        if (w != -1) {
            this.srcW = w;
            this.dstW = w;
        }
        if (h != -1) {
            this.srcH = h;
            this.dstH = h;
        }
        */
        
        //this.ratioX = 1f;
        //this.ratioY = 1f;
        return this;
    }
    
    public RenderEvent dstXY(int x, int y) {
        this.dstX = x;
        this.dstY = y;
        return this;
    }
    
    public RenderEvent dstWH(int w, int h) {
        this.dstH = h;
        this.dstW = w;
        //this.ratioX = dstW / srcW;
        //this.ratioY = dstH / srcH;
        return this;
    }

    public RenderEvent Ratio(float r) {
        this.ratioX = r;
        this.ratioY = r;
        this.dstH = (int) (srcH * r);
        this.dstW = (int) (srcW * r);
        return this;
    }

    public RenderEvent Rotation(float angle) {
        this.angle = angle;
        return this;
    }

    public RenderEvent Gravity(int gravity) {
        this.gravity = gravity;
        return this;
    }

    public RenderEvent filp(boolean x, boolean y) {
        //texture.flip(x, y);
        flipX = x;
        flipY = y;
        return this;
    }

    public RenderEvent color(int color) {
        this.color = new Color(color);
        return this;
    }

    public RenderEvent color(float r, float g, float b, float a) {
        color = new Color(r, g, b, a);
        return this;
    }

    // e.x ( GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA )
    public RenderEvent blend(int srcFunc, int dstFunc) {
        this.useBlend = true;
        this.srcFunc = (srcFunc == -1) ? GL20.GL_SRC_ALPHA : srcFunc;
        this.dstFunc = (dstFunc == -1) ? GL20.GL_ONE_MINUS_SRC_ALPHA : dstFunc;
        return this;
    }

    public RenderEvent size(int size) {
        this.size = size;
        return this;
    }
    
}
