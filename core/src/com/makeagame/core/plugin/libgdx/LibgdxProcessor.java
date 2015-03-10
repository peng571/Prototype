package com.makeagame.core.plugin.libgdx;

import java.util.HashMap;

import org.json.JSONObject;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.makeagame.core.resource.InternalResource;
import com.makeagame.core.resource.Resource;
import com.makeagame.core.resource.Resource.ResourceState;
import com.makeagame.core.resource.process.Finder;
import com.makeagame.core.resource.process.Processor;
import com.makeagame.core.view.RenderEvent;

public class LibgdxProcessor implements Processor {

    private Finder finder;
    
    public HashMap<String, String> pathMap;
    
    public LibgdxProcessor(Finder finder){
        this.finder = finder;
        pathMap = new HashMap<String, String>();
    }
    
    
    private JSONObject find(String id){
        return finder.find(id);
    }

    @Override
    public boolean canHandle() {
        // TODO
        return false;
    }



    @Override
    public void handleResource(Resource res) {
        
        String id = res.getID();
        JSONObject attr = null;
        switch(res.getState()){
        case NAMED:
            res.setState(ResourceState.FINDING);
        case FINDING:
            attr = find(id);
            InternalResource payload = null;
            if(attr == null){
                res.setState(ResourceState.NOTFOUND);
                break;
            }
            res.setState(ResourceState.DECODING);
            res.src(attr.optInt("x"), attr.optInt("y"), attr.optInt("w", -1), attr.optInt("h", -1));
        case DECODING:
            String type = attr.optString("type");
            String path = attr.optString("path");
            System.out.println("attr " + attr);
            System.out.println("path " + path);
            if("img".equals(type)){
                res.type = RenderEvent.IMAGE;
                payload = new LibgdxResImage(path);
            } else if("snd".equals(type)){
                res.type = RenderEvent.SOUND;
                payload = new LibgdxResSound(path);
            } else if("atr".equals(type)){
                payload = new LibgdxResText(path);
            } else{
                res.setState(ResourceState.DECODEERROR);
                return;
            }
            res.setPayload(payload);
            res.setState(ResourceState.USABLE);
        }   
    }

}

