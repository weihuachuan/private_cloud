package com.huachuan.bigdata.hos.core.usermgr;

import com.huachuan.bigdata.hos.core.HosException;

/**
 * 用户管理模块异常.
 */
public class HosUsermgrException extends HosException {

    private int code;
    private String message;

    public HosUsermgrException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }

    public HosUsermgrException(int code, String message) {
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

