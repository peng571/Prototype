package com.makeagame.core.plugin.libgdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.makeagame.core.resource.InternalResource;

public class LibgdxResText implements InternalResource {

    private String text;
    
    public LibgdxResText(String path) {
        FileHandle handle = Gdx.files.internal(path);
        if (handle != null && handle.exists()) {
            text = handle.readString();
        }
    }
    
    public String get(){
        return text;
    }
}
