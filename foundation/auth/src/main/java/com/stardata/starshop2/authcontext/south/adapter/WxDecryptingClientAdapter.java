package com.stardata.starshop2.authcontext.south.adapter;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.google.gson.JsonSyntaxException;
import com.stardata.starshop2.authcontext.domain.user.UserToken;
import com.stardata.starshop2.authcontext.south.port.WxDecryptingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import com.stardata.starshop2.sharedcontext.domain.MobileNumber;
import com.stardata.starshop2.sharedcontext.exception.ApplicationValidationException;
import me.chanjar.weixin.common.error.WxRuntimeException;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:19
 */
@Adapter( PortType.Client)
@Component
public class WxDecryptingClientAdapter implements WxDecryptingClient {

    @Override
    public MobileNumber decryptMobileNumber(UserToken userToken, String encryptedData, String iv) {
        final WxMaService wxService = WxConfiguration.getMaService();
        try{
            WxMaPhoneNumberInfo phoneNumberInfo = wxService.getUserService()
                    .getPhoneNoInfo(userToken.getSessionKey(), encryptedData, iv);
            return MobileNumber.from(phoneNumberInfo.getPhoneNumber());
        }
        catch(JsonSyntaxException | WxRuntimeException e) {
            throw new ApplicationValidationException(ApplicationValidationException.INVALID_REQUEST_DATA, "encryptedData or iv data error", e);
        }
    }
}
