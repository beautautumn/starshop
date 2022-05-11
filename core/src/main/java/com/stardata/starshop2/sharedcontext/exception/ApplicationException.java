package com.stardata.starshop2.sharedcontext.exception;

/**
 * 本应用所有运行态异常的抽象父类
 */
public abstract class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Exception ex) {
        super(message, ex);
    }
}