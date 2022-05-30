package com.stardata.starshop2.sharedcontext.exception;

/**
 * 北向网关应用服务抛出的底层设施错误（各类端口内部引起，如：数据库异常、客户端异常等）引起的异常，
 * 一般被远程服务所捕获以用于判断返回符合 HTTP 协议标准的 HttpStatus
 */
public class ApplicationInfrastructureException extends ApplicationException {
    public ApplicationInfrastructureException(String message) {super(message);}
    public ApplicationInfrastructureException(int errCode, String message) {super(errCode, message);}
    public ApplicationInfrastructureException(String message, Exception ex) {super(message, ex);}
    public ApplicationInfrastructureException(int errCode, String message, Exception ex) {super(errCode, message, ex);}
}