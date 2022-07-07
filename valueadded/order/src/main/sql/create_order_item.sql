create table tb_order_item
(
    id                bigint auto_increment
        primary key,
    order_id          bigint           not null,
    product_type      char             null comment '1：商品库商品；2：接龙商品；',
    actual_product_id bigint           not null comment '可能是商品销售关系表的主键ID（如果归属订单类型是店铺订单或子订单），也可能是接龙商品表的主键ID（如果归属订单类型是接龙订单或子订单）。',
    purchase_count    int              not null,
    subtotal_fen      int              not null,
    is_valid          char default '1' not null comment '0：无效记录（被删除）；1：有效记录。',
    create_time       datetime         not null,
    update_time       datetime         not null,
    product_snapshot  varchar(4000)    null,
    status            char default '1' not null comment '1：待配货；2：待发货；3：已发货；',
    constraint fk_order_item_order
        foreign key (order_id) references tb_order (ID)
);

create index idx_order_item_order
    on tb_order_item (order_id);

create index idx_order_item_product
    on tb_order_item (actual_product_id);

