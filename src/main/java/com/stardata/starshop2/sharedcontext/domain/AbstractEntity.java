package com.stardata.starshop2.sharedcontext.domain;


/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/24 18:45
 */
public abstract class AbstractEntity<ID extends Identity>  {
   public abstract ID id();
}
