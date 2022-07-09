create table tb_product_sale_stat
(
    ID          bigint auto_increment
        primary key,
    product_id  bigint   not null,
    month       char(6)  not null comment '格式：YYYYMM',
    sale_count  int      not null,
    create_time datetime not null,
    update_time datetime not null,
    constraint fk_sale_stat_product
        foreign key (product_id) references tb_product2 (ID)
);

create index idx_sale_stat_product
    on tb_product_sale_stat (product_id);
