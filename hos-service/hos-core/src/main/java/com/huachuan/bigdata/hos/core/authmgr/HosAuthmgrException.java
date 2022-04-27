package com.huachuan.bigdata.hos.core.authmgr;

import com.huachuan.bigdata.hos.core.HosException;

public class HosAuthmgrException extends HosException {

    private int code;
    private String message;

    public HosAuthmgrException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public HosAuthmgrException(int code, String message) {
        super(message, null);
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int errorCode() {
        return this.code;
    }
}
