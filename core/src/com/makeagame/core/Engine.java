package com.makeagame.core;

import java.util.ArrayList;

import com.makeagame.core.view.RenderEvent;
import com.makeagame.core.view.SignalEvent;

/**
 * 處理訊號的輸入輸出.
 * 提供整個遊戲的迴圈幫浦.
 * 提供除錯和紀錄功能.
 */
public class Engine {

    public static boolean LOG = true;
    public static boolean DEBUG = true;
    public static final String TAG = "MakeAGame";

    private Driver driver;

    static private ArrayList<SignalEvent> signalList = new ArrayList<SignalEvent>();
    static private ArrayList<RenderEvent> renderList = new ArrayList<RenderEvent>();
    
    public Engine(Bootstrap bootstrap) {
        driver = bootstrap.getDriver();
        driver.setEngine(this);
        
        Controler.get().register( bootstrap.getMainModel(), bootstrap.getMainView());
    }

    

    // 主要迴圈: 所有的使用者操作指令，和繪圖指令都在這邊整合
    // TODO 可能會有 ArrayList 的同步問題
    public void tickOne() {

        // 處理輸入(使用者操作)的指令
        signalList = driver.signal(signalList);
        Controler.get().signal(signalList);
        signalList.clear();        
        
        // 處理輸出(繪圖)的指令
        renderList =  Controler.get().render(renderList);
        renderList = driver.render(renderList);
        renderList.clear();
    }

    
    
    /*
     * LOG 機制
     * TODO log工具
     */
    public static void logI(String s) {
        if (LOG) {
            System.out.println(s);
        }
    }

    public static void logD(String d) {
        if (LOG) {
            System.out.println(d);
        }
    }

    public static void logE(String e) {
        System.out.println(e);
    }

    public static void logE(Exception e) {
        System.out.println(e);
    }

    
}
