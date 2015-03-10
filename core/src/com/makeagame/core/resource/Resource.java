package com.makeagame.core.resource;

import com.makeagame.core.exception.ResourceNotReadyException;
import com.makeagame.core.view.RenderEvent;

/**
 * 固定資源
 *
 * @param <T>
 */
public class Resource <T extends InternalResource> {

    private String ID;
    private boolean isAccessable;
    
    protected ResourceState state;

    private T payload;
    public int type = RenderEvent.UNKNOW;
    
    public enum ResourceState {
        INVALID, // 無效, 進入此狀態的資源將不被再引擎所管理(也就是說這是個中止狀態)
        NAMED, // 才剛被指定ID
//        UNLOADED, // 未載入(這個狀態暗示之前有載入成功, 只不過現在被卸載了)
        FINDING, // 尋找中
//        LOADING, // 讀取中
        DECODING, // 解碼中
        NOTFOUND, // 404
//        LOADERROR, // 讀取錯誤(通常是IO錯誤)
        DECODEERROR, // 解碼錯誤(通常是格式錯誤或是檔案損毀)
        USABLE // 全部流程跑完. 可使用的
    };

    
    Resource(String id){
        this.ID = id;
        this.isAccessable = true;
        this.setState(ResourceState.NAMED);
    }

    public String getID() {
        return this.ID;
    }

    // 以下方法需要加鎖以保證 thread-safe
    public ResourceState getState() {
        return state;
    }
    
    public boolean accessable() {
        return isAccessable; 
    }

    public boolean ready() {
        if (state == ResourceState.USABLE) {
            return true;
        }
        return false;
    }

    // accessable() == false or ready() == false 的情況下會拋出異常
    public T getPayload() throws ResourceNotReadyException {
        if(ready() && accessable() && payload!= null){
            return payload;
        }
        throw new ResourceNotReadyException();
    }

    
    // 以下只可被引擎內部使用的方法

    // 此方法 InternalResource 轉成 T 的轉型失敗後, 會將狀態設定微 INVALID
    // 並且拋棄這個 Resource
    public void setPayload(T payload) {
        this.payload = payload;
        if(false){ // TODO 轉型失敗
            state = ResourceState.INVALID;
        }
    }

    public void setState(ResourceState state) {
        System.out.println("Set to " + state.name());
        this.state = state;
    }
  

    
    int srcX = 0, srcY = 0, srcW = -1, srcH = -1;

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

    public int[] getWH() {
        return new int[] {srcW, srcH};
    }

    public int[] getSrcDim() {
        return new int[] {srcX, srcY, srcW, srcH};
    }
      
      
}

    

