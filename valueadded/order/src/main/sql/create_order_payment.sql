create table tb_order_payment
(
    id              bigint        not null
        primary key,
    order_id        bigint        null,
    refund_id       bigint        null comment '只有补收或退款，该字段才不为空。',
    pay_type        char          not null comment '1：订单支付；2：订单补收；3：订单退款；',
    user_id         bigint        null,
    prepay_id       varchar(100)  null,
    transaction_id  varchar(100)  null,
    request_message varchar(8000) null,
    result_message  varchar(8000) null,
    pay_time        datetime      null,
    status          char          not null comment '1：待支付；2：支付成功；3：支付失败；',
    create_time     datetime      not null,
    update_time     datetime      not null,
    cash_fee_fen    int           null,
    constraint fk_payment_refund
        foreign key (refund_id) references tb_order_refund (id),
    constraint fk_payment_order
        foreign key (order_id) references tb_order (ID),
    constraint fk_payment_user
        foreign key (user_id) references tb_user (ID)
);

create index idx_payment_order
    on tb_order_payment (order_id);

create index idx_payment_user
    on tb_order_payment (user_id);

