package com.stardata.starshop2.sharedcontext.exception;


/**
 * 北向网关应用服务抛出的领域逻辑错误引起的异常，
 * 一般被远程服务所捕获以用于判断返回符合 HTTP 协议标准的 HttpStatus
 */
public class ApplicationDomainException extends ApplicationException {
    public ApplicationDomainException( String message) {super(message);}
    public ApplicationDomainException(int errCode, String message) {super(errCode, message);}
    public ApplicationDomainException(int errCode, String message, Exception ex) {
        super(errCode, message, ex);
    }
}