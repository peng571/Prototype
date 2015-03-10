package com.makeagame.core.resource.process;

import com.makeagame.core.resource.Resource;

public interface Processor {
 
    // 
    public void handleResource(Resource res);
    
    // 若無法處哩，會提交給下一個 processor
    public boolean canHandle();
}
