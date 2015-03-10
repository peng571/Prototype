package com.makeagame.core.resource;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.makeagame.core.resource.Resource.ResourceState;
import com.makeagame.core.resource.process.Processor;

/**
 * 使用者用來讀取資源的介面
 */
public class ResourceSystem{

    private final int MAX_OF_THREAD = 10;
    private ExecutorService executor;
    
    private ArrayList<Processor> processorList; 
   
    public static ResourceSystem instance;

    private ResourceSystem() {
        
        // TODO 尋找是否又更適合的 ThreadPool
        executor = Executors.newFixedThreadPool(MAX_OF_THREAD);
        
        processorList = new ArrayList<Processor>();
    }

    public static ResourceSystem get() {
        if (instance == null) {
            instance = new ResourceSystem();
        }
        return instance;
    }

    
    public void addProcessor(Processor process) {
        processorList.add(process);
    }

    public void removeProcessor(Processor process) {
        processorList.remove(process);
    }

    public void removeAllProcessor() {
        processorList.clear();
    }

    public void resetDefault() {
        removeAllProcessor();
    }

    
    public void startup() {
        // TODO 設定好系統之後, 要呼叫void startup()用來啟動固定資源系統, 一但啟動之後所有的外掛方法都不能再被使用
    }


    class FetchThread implements Runnable{
        
        Resource res;
        String id;
        
        public FetchThread(Resource res, String id){
            this.res = res;
            this.id = id;
        }
        
        
        // 在newFixedThreadPool中
        // Runnable的run結束後就會結束自己
        @Override
        public void run() {
            for(Processor process : processorList) {
                process.handleResource(res);
                if(res.getState() == ResourceState.USABLE){
                    break;
                }
            }
        }
    }
    
    
    public<T extends Resource> T fetch(String id) throws Exception{
        T res = (T)ResourceManager.get().getResource(id);
        
        // 還未被尋找過的才重新建立Thread
        // TODO 但這樣可能還是會有重複建立的問題，要再修改
        if(res.getState() == ResourceState.NAMED) {
            res.setState(ResourceState.FINDING);
            
            // OOPS: OpenGL不可在不同的執行序存取，改成只把檔案存取的部分由多序執行
//            Runnable worker = new FetchThread(res, id);  
//            executor.execute(worker);
            
            for(Processor process : processorList) {
                process.handleResource(res);
                if(res.getState() == ResourceState.USABLE){
                    break;
                }
            }
            
        }  
        return res;
    }
    
    
}
