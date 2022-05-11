package com.stardata.starshop2.sharedcontext.exception;

/**
 * 领域层（领域服务或聚合）抛出的领域业务逻辑异常，一般被应用服务所捕获
 */
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
