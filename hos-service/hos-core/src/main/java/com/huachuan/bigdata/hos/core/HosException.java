package com.huachuan.bigdata.hos.core;

//所有模块的异常基类
public abstract class HosException extends RuntimeException {

    protected String errorMessage;

    public HosException(String message, Throwable cause) {
        super(cause);
        this.errorMessage = message;
    }

    public abstract int errorCode();

    public String errorMessage() {
        return this.errorMessage;
    }
}
