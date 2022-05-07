package com.stardata.starshop2.sharedcontext.south.port;

import com.stardata.starshop2.sharedcontext.domain.BizParameter;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/30 21:12
 */
public interface BizParameterRepository {
    BizParameter instanceOf(String key);

}
