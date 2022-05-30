package com.stardata.starshop2.sharedcontext.exception;

/**
 * 本应用所有运行态异常的抽象父类
 */
public abstract class ApplicationException extends RuntimeException {
    public static int UNKNOWN_ERR_CODE = -1;
    public static String UNKNOWN_ERR_MESSAGE = "unknown error";

    private final int errCode;
    private final String errMsg;

    public ApplicationException() {
        super();
        this.errCode = UNKNOWN_ERR_CODE;
        this.errMsg = UNKNOWN_ERR_MESSAGE;
    }

    public ApplicationException(String message) {
        super(message);
        this.errCode = UNKNOWN_ERR_CODE;
        this.errMsg = message;
    }

    public ApplicationException(int errCode, String message) {
        super(message);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public ApplicationException(String message, Exception ex) {
        super(message, ex);
        this.errCode = UNKNOWN_ERR_CODE;
        this.errMsg = message;
    }

    public ApplicationException(int errCode, String message, Exception ex) {
        super(message, ex);
        this.errCode = errCode;
        this.errMsg = message;
    }

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }
}