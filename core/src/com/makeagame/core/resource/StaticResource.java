package com.makeagame.core.resource;

/**
 * 資源有可能是靜態的或是預先載入的, 我們設計了一個 StaticResource 來表達這一類的資源.
 * 這種資源不會一開始就存在也不會被釋放, 有著真正的不變性, 所以所有操作都不需要加鎖.
 */
public class StaticResource<T> extends Resource {

    // TODO
    
    public StaticResource(String id) {
        super(id);
        this.state = ResourceState.USABLE;
    }

    // 所有的方法不用加鎖
    // 只會有兩種狀態 NOTFOUND, USABLE
    public ResourceState getState(){
        return state;
    }

}
