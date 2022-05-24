package com.stardata.starshop2.authcontext.south.adapter;


import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.stardata.starshop2.authcontext.domain.WxLoginErrorException;
import com.stardata.starshop2.authcontext.domain.user.WxAuthInfo;
import com.stardata.starshop2.authcontext.south.port.WxSessionCheckingClient;
import com.stardata.starshop2.sharedcontext.annotation.Adapter;
import com.stardata.starshop2.sharedcontext.annotation.PortType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/5/11 11:19
 */
@Adapter(PortType.Client)
@Conditional(ProductionProfileCondition.class)
@Component
public class WxSessionCheckingClientAdapter implements WxSessionCheckingClient {
    @Override
    public void code2session(String code, WxAuthInfo wxAuthInfo) throws WxLoginErrorException {
        final WxMaService wxService = WxConfiguration.getMaService();
        try {
            WxMaJscode2SessionResult session = wxService.getUserService().getSessionInfo(code);
            wxAuthInfo.setOpenid(session.getOpenid());
            wxAuthInfo.setSessionKey(session.getSessionKey());
        } catch (WxErrorException e) {
            WxError wxError = e.getError();
            throw new WxLoginErrorException(wxError.getErrorCode(), wxError.getErrorMsg());
        }
    }
}
