package com.stardata.starshop2.ordercontext.command.domain.order;

import com.stardata.starshop2.sharedcontext.usertype.PersistentCharEnum;

/**
 * @author Samson Shu
 * @version 1.0
 * @email shush@stardata.top
 * @date 2022/4/29 15:22
 */
public enum OrderOperType implements PersistentCharEnum {
    CREATE('1'),        //创建
    PAY('2'),           //支付
    PICK('3'),          //配货
    DISPATCH('4'),      //发货
    CLOSE('5'),         //关闭
    CANCEL('6'),        //取消
    RECHARGE('7'),      //补收
    REFUND('8'),        //退款
    SHOP_REMARK('9'),   //店主备注
    SET_INVISIBLE('A'), //设置不可见
    MARK_READ('B');     //标为已读

    private final char value;

    OrderOperType(char value) {
        this.value = value;
    }

    @Override
    public char getValue() {
        return this.value;
    }
}
