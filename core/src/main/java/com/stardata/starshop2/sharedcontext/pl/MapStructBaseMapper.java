package com.stardata.starshop2.sharedcontext.pl;

import com.stardata.starshop2.sharedcontext.domain.LongIdentity;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/6/28 09:27
 */
public interface MapStructBaseMapper {
    default Long identityToLong(LongIdentity id) {
        return id.value();
    }
}
