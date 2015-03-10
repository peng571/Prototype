package com.makeagame.core.plugin.libgdx;

import com.badlogic.gdx.audio.Sound;
import com.makeagame.core.resource.InternalResource;

public class LibgdxResSound implements InternalResource{
    private Sound sound;

    public LibgdxResSound(String path) {
//        newSound = Audio.newSound(new FileHandle(path));
    }
    
    public Sound get(){
        return sound;
    }
}
