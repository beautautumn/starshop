package com.stardata.starshop2.sharedcontext.exception;

/**
 * 北向网关应用服务抛出的输入数据、权限校验等引起的异常，
 * 一般被远程服务所捕获以用于判断返回符合 HTTP 协议标准的 HttpStatus
 */
public class ApplicationValidationException extends ApplicationException {
    public ApplicationValidationException(String message) {
        super(message);
    }
}