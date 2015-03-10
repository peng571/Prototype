package com.makeagame.core.resource;

import java.util.HashMap;

/**
 * 管理所有的 Resource
 * 供系統操作用
 * 不建議使用者自行呼叫
 */
class ResourceManager {

    private static ResourceManager instance;
    private HashMap</* ID= */String, Resource> resourceMap;

    private ResourceManager() {
        resourceMap = new HashMap<String, Resource>();
    }

    static ResourceManager get() {
        if (instance == null) {
            instance = new ResourceManager();
        }
        return instance;
    }
    
    Resource getResource(String id){
        Resource res = resourceMap.get(id);
        
        if(res == null){
            res = new Resource(id);
        }
        
        resourceMap.put(id, res);
        return res;
    }

}
