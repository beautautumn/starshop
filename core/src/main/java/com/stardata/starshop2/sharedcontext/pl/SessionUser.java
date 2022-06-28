package com.stardata.starshop2.sharedcontext.pl;

import lombok.Data;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/26 18:00
 */
@Data
public class SessionUser {
    private Long id;


    protected SessionUser(Long id) {
        this.id = id;
    }

    public static SessionUser from(Long id) {
        return new SessionUser(id);
    }
}
