create table tb_order_oper_log
(
    ID        bigint auto_increment
        primary key,
    order_id  bigint       not null,
    user_id   bigint       null,
    oper_type char         null comment '1：创建；2：支付；3：配货；4：发货；5：结束；6：退款；7：客户备注；8：商家备注；',
    oper_time datetime     null,
    oper_desc varchar(200) null,
    constraint fk_order_log_order
        foreign key (order_id) references tb_order (ID),
    constraint fk_order_log_user
        foreign key (user_id) references tb_user (ID)
);

create index idx_order_log_order
    on tb_order_oper_log (order_id);

create index idx_order_log_user
    on tb_order_oper_log (user_id);

