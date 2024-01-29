package com.stardata.starshop2.sharedcontext.domain;


import java.io.Serializable;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 18:45
 */
public abstract class AbstractEntity<ID extends Identity> implements Serializable {
   public abstract ID id();
}
