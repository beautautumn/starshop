-- auto-generated definition
create table tb_prod_category
(
    ID            bigint auto_increment primary key,
    shop_id       bigint           null,
    name          varchar(40)      null,
    display_order int              null comment '显示顺序从小到大排列',
    is_valid      char default '1' not null comment '0：记录无效；1：记录有效。',
    create_time   datetime         not null,
    update_time   datetime         not null,
    constraint prod_category_shop
        foreign key (shop_id) references tb_shop (id)
);

create index idx_prod_category_shop
    on tb_prod_category (shop_id);