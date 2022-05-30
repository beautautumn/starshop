package com.stardata.starshop2.authcontext.south.adapter;


import com.stardata.starshop2.authcontext.domain.WxLoginErrorException;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.south.port.WxSessionCheckingClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:19
 */
@Conditional(DevOrTestProfileCondition.class)
@Component
public class WxSessionCheckingSimulatorAdapter implements WxSessionCheckingClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public void code2session(String code, WxAuthInfo wxAuthInfo) throws WxLoginErrorException {
        logger.info(" WxSessionCheckingClient implementation is simulator adapter....");

        if (code.startsWith("test")) {
            throw new WxLoginErrorException(40029, "code 无效");
        }

        if (code.equals("091FHp0w3KT3yY2VjV1w3aMiIB3FHp0B")) {
            throw new WxLoginErrorException(40029, "code is used");
        }

        if (code.equals("081sloll28T8d94nl8nl2hxKhh3slolv")) {
            wxAuthInfo.setOpenid("oVsAw5cdcnIxaae-x98ShoH93Hu0");
            wxAuthInfo.setUnionid("oeX0e6HcoHXWQsYqM7nUkB6QcaAI");
            wxAuthInfo.setSessionKey("NRTHiwwdbwQEmyWZopiUPQ==");
        }

        if (code.equals("011NXJ000zQ8VN1iJZ000DWjjS2NXJ0e")) {
            wxAuthInfo.setOpenid("oVsAw5cdcnIxaae-x98ShoH93Hu0");
            wxAuthInfo.setUnionid("oeX0e6HcoHXWQsYqM7nUkB6QcaAI");
            wxAuthInfo.setSessionKey("EfUbtTDsTz/S3lSvdkx2jg==");
        }
    }
}
