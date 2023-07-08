package com.stardata.starshop2.sharedcontext.usertype;

import com.stardata.starshop2.sharedcontext.domain.Gender;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2023/7/8 19:37
 */
public class GenderUserType extends PersistentEnumUserType<Gender> {

    @Override
    public Class<Gender> returnedClass() {
        return Gender.class;
    }
}
