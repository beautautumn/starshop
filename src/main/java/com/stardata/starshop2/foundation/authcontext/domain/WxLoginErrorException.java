package com.stardata.starshop2.foundation.authcontext.domain;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/27 16:39
 */
public class WxLoginErrorException extends RuntimeException {
    public WxLoginErrorException() {super("Wechat login error."); }

    public WxLoginErrorException(String msg) {super("Wechat login failed: " + msg); }

    public WxLoginErrorException(Throwable cause) {super("Wechat login error. %s", cause); }
}
