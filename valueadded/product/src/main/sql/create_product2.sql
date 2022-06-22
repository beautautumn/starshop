-- auto-generated definition
create table tb_product2
(
    id                 bigint auto_increment
        primary key,
    category_id        bigint           not null,
    shop_id            bigint           not null,
    name               varchar(60)      not null,
    introduction       varchar(500)     null,
    unit               varchar(10)      not null,
    min_purchase       decimal(6, 2)    default 1.00 null,
    original_price_fen int              null,
    display_order      int              null comment '显示顺序从小到大排列',
    labels             varchar(1000)    null comment 'json字符串格式保存标签。例： ["特价优惠", "51庆典"]',
    is_available       char(1) default '0' not null comment '0：售罄；1：有货。',
    on_shelves         char(1) default '0' not null comment '0：已下架；1：已上架；',
    discount_type      char default '0' null comment '1：按比例折扣；2：固定金额。',
    discount_rate      decimal(3, 2)    null,
    discount_price_fen int              null,
    purchase_limit     int              null,
    is_valid           char default '1' not null comment '0：无效记录（被删除）；1：有效记录。',
    create_time        datetime         not null,
    update_time        datetime         not null,
    constraint FK_product2_category
        foreign key (category_id) references tb_prod_category (ID),
    constraint FK_product2_shop
        foreign key (shop_id) references tb_shop (id)
)
    charset = utf8mb3;

create index idx_product2_category
    on tb_product2 (category_id);

create index idx_product2_shop
    on tb_product2 (shop_id);

