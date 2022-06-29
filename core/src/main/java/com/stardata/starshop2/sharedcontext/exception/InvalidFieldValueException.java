package com.stardata.starshop2.sharedcontext.exception;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 22:57
 */
public class InvalidFieldValueException extends ApplicationException{
    public InvalidFieldValueException( String message) {super(message);}
    public InvalidFieldValueException(int errCode, String message) {super(errCode, message);}
    public InvalidFieldValueException(int errCode, String message, Exception ex) {
        super(errCode, message, ex);
    }
}
