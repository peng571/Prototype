package com.makeagame.core.resource;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.makeagame.core.Engine;
import com.makeagame.core.resource.Resource.TYPE;

public class ResourceManager {

    public static ResourceManager instance;
    public HashMap<String, Resource> resourceMap;
    
    public HashMap<String, Texture> textureMap;
    public HashMap<String, Sound> soundMap;
    
    String url;

    private ResourceManager() {
        resourceMap = new HashMap<String, Resource>();
        textureMap = new HashMap<String, Texture>();
        soundMap = new HashMap<String, Sound>();
    }

    public static ResourceManager get() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }

    //public Resource use(String id) {
    //    return resourceMap.get(id);
    //}
    
    private void loadImage(String path) {
        // TODO: Finder
        //Engine.logI(new Texture(path).toString());
        textureMap.put(path, new Texture(path));
    }
    
    private void loadSound(String path) {
        // TODO: Finder
        soundMap.put(path, Gdx.audio.newSound(Gdx.files.internal(path)));
    }
    
    public Resource fetch(String id) {
        if (resourceMap.get(id) == null) {
            // TODO: Error handle
            //loadImage(id);
            //Resource resource = new Resource(Resource.TYPE.IMAGE, id);
            //bind(id, resource);
            //return resource;
            Engine.logE("can't find resource at '" + id + "'");
            return null;
        }
        return resourceMap.get(id);
    }
    
//    public Texture fetch(String id) {
//        if (resourceMap.get(id) == null) {
//            Engine.logE("can't find resource at '" + id + "'");
//            return null;
//        }
//        return resourceMap.get(id).texture;
//    }

    public String read(String id) {
        if (resourceMap.get(id) == null) {
            Engine.logE("can't find resource at '" + id + "'");
            return "";
        }
        return resourceMap.get(id).file;
    }
    
    public Resource bindImage(String id, String path) {
        loadImage(path);
        Resource t = new Resource(TYPE.IMAGE, path);
        resourceMap.put(id, t);
        return t;
    }
    
    public Resource bindSound(String id, String path) {
        loadSound(path);
        Resource t = new Resource(TYPE.SOUND, path);
        resourceMap.put(id, t);
        return t;
    }
    
    
    public void bind(String id, Resource resource) {
        resourceMap.put(id, resource);
    }

}
