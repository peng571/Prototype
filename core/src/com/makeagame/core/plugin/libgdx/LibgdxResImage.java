package com.makeagame.core.plugin.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.makeagame.core.resource.InternalResource;

public class LibgdxResImage implements InternalResource{ // extends Texture
    
    Texture texture;

    public LibgdxResImage(FileHandle file) {
        System.out.println("LibgdxResImage(FileHandle file) " + file.toString());
        texture = new Texture(file);
    }
    
    public LibgdxResImage(String path){
        // XXX 在外部資源的存取架構弄好前，暫時先單純的存取內部資源
        System.out.println("LibgdxResImage(String path) " + path);
        texture = new Texture(Gdx.files.internal(path));
        
//        super(Gdx.files.classpath("../resource/" + path));
//        Gdx.files.internal(path);
    }
    
    public Texture get(){
        return texture;
    }
}

