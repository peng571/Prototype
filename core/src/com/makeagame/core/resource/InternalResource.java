package com.makeagame.core.resource;

/**
 * 內部資源 / 實體資源
 * 儲藏著資源的實際資料 (ex. 圖片的點陣資料)
 * 在創建的過程是可變的, 一但建立程序結束之後就不可在改變內部狀態.
 */
public interface InternalResource {

    public<T> T get();
}
