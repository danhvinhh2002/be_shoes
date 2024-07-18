package com.shoescms.common.exception;

import lombok.Getter;

/**
 * @author : tonyson
 * @date : 2021/11/02 11:41 오전
 * @desc :
 */
@Getter
public class ObjectNotFoundException extends RuntimeException{
    private int code;
    public ObjectNotFoundException(int code) {
        super();
        this.code = code;
    }

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}