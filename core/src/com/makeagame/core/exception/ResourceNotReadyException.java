package com.makeagame.core.exception;

public class ResourceNotReadyException extends Exception {

    /**
     * getPayload 時 ， accessable() == false or ready() == false 的情況下會拋出的異常
     */
    private static final long serialVersionUID = 5665831097046569528L;
    
    public ResourceNotReadyException() { super(); }
    public ResourceNotReadyException(String message) { super(message); }
    public ResourceNotReadyException(String message, Throwable cause) { super(message, cause); }
    public ResourceNotReadyException(Throwable cause) { super(cause); }
    
}
