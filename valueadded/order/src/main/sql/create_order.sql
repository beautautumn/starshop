create table tb_order
(
    ID                 bigint auto_increment
        primary key,
    user_id            bigint           not null,
    cust_id            bigint           null,
    shop_id            bigint           null,
    chain_id           bigint           null,
    type               char             not null comment '1：店铺订单；2：店铺子订单（��于品牌商接收订单）；3：接龙订单；4：接龙子订单（用于品牌商接收订单）；',
    parent_id          bigint           null,
    total_amount_fen   int              not null,
    order_number       varchar(40)      not null,
    pay_time           datetime         null,
    confirm_time       datetime         null,
    close_time         datetime         null,
    delivery_type      char             null comment '1：到店自提；2：送货上门；',
    delivery_info      varchar(500)     null comment '用json字符串保����订单提货信息。如果是到店自提，则保存：自提店铺ID、提货手机号；如果是送货上门，则保存：送货地理位置、送货详细地址、收货人、收货人手机号、送货时间。',
    chain_predict_date date             null comment '根据接龙的提货时间类型，自动计算出一个最晚到货时间。如果该字段为空，且本订单为接龙订单，则表示到货时间未定。',
    cust_remark        varchar(200)     null,
    shop_remark        varchar(200)     null,
    status             char             not null comment '1：待支付；2：已支付；3：已配货；4：已发货；5：已结束；6：已取消；',
    delivery_number    varchar(20)      null,
    has_read           char default '1' not null comment '0：未读；1：已读。',
    is_valid           char default '1' not null comment '0：不可见；1：可见。',
    create_time        datetime         not null,
    update_time        datetime         not null,
    visible            char default '1' not null comment '消费者是否可见。当消费者选择删除某订单后，该订单被标记为不可见。',
    constraint fk_order_user
        foreign key (user_id) references tb_user (ID),
    constraint fk_order_shop
        foreign key (shop_id) references tb_shop (id),
    constraint fk_order_parent
        foreign key (parent_id) references tb_order (ID),
    constraint fk_order_customer
        foreign key (cust_id) references tb_customer (id),
    constraint fk_order_chain
        foreign key (chain_id) references tb_chain_activity (ID)
);

create index idx_order_chain
    on tb_order (chain_id);

create index idx_order_cust
    on tb_order (cust_id);

create index idx_order_number
    on tb_order (order_number);

create index idx_order_parent
    on tb_order (parent_id);

create index idx_order_shop
    on tb_order (shop_id);

create index idx_order_user
    on tb_order (user_id);

