package com.stardata.starshop2.sharedcontext.exception;

/**
 * 北向网关应用服务抛出的输入数据、权限校验等引起的异常，
 * 一般被远程服务所捕获以用于判断返回符合 HTTP 协议标准的 HttpStatus
 */
public class ApplicationValidationException extends ApplicationException {
    public static int INVALID_REQUEST_ENTITY = 1;
    public static int INVALID_REQUEST_DATA = 2;

    public ApplicationValidationException(String message) {
        super(message);
    }
    public ApplicationValidationException(int errCode, String message) {
        super(errCode, message);
    }

    public ApplicationValidationException(int errCode, String message, Exception cause) {
        super(errCode, message, cause);
    }
}