package com.makeagame.core.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class Resource {
    //Texture texture;
    //public Sound _sound;
    
    public enum TYPE {
        IMAGE,
        SOUND
    };
    
    public TYPE type;
    public String path;
    
    int centerX, centerY;
    int srcX, srcY, srcW, srcH;
    String file;
    
    //public Resource() {}
    
    public Resource(TYPE type, String path) {
        this.type = type;
        this.path = path;
        if (type.equals(TYPE.IMAGE)) {
            int w = ResourceManager.get().textureMap.get(path).getWidth();
            int h = ResourceManager.get().textureMap.get(path).getHeight();
            //Engine.logI("w: " + new Integer(w).toString());
            //Engine.logI("h: " + new Integer(h).toString());
            src(w, h);
        }
    }
    
    //public int[] getWH() {
    //    return new int[] {srcW, srcH};
    //}
    
    public int[] getSrcDim() {
        return new int[] {srcX, srcY, srcW, srcH};
    }
    
    /*
    public Resource image( String texture) {
        //this.texture = new TextureRegion(new Texture(texture));
        this.texture = new Texture(texture);
        return this;
    }
    
    public Resource sound( String path) {
        //this.texture = new TextureRegion(new Texture(texture));
        this._sound = Gdx.audio.newSound(Gdx.files.internal(path));
        return this;
    }
    */

    public Resource attribute(String file) {
        FileHandle handle = Gdx.files.internal(file);
        if (handle != null && handle.exists()) {
            this.file = handle.readString();
        }
        return this;
    }

    public Resource src(int srcX, int srcY, int srcW, int srcH) {
        this.srcX = srcX;
        this.srcY = srcY;
        this.srcW = srcW;
        this.srcH = srcH;
        return this;
    }

    public Resource src(int srcW, int srcH) {
        return src(0, 0, srcW, srcH);
    }

//
//    public Resource dst(int dstX, int dstY, int dstW, int dstH) {
//        this.x = dstX;
//        this.y = dstY;
//        this.w = dstW;
//        this.h = dstH;
//        return this;
//    }
//
//    public Resource center(int x, int y) {
//        this.centerX = x;
//        this.centerY = y;
//        return this;
//    }
}