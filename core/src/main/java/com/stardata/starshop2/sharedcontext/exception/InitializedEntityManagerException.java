package com.stardata.starshop2.sharedcontext.exception;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/9 21:26
 */
public class InitializedEntityManagerException extends ApplicationInfrastructureException {
    public InitializedEntityManagerException(String message, Exception ex) {
        super(message, ex);
    }

    public InitializedEntityManagerException(String message) {
        super(message, null);
    }
}
