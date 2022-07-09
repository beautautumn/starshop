create table tb_product_image
(
    ID              bigint auto_increment
        primary key,
    product_id       bigint        not null,
    url_path        varchar(1000) not null,
    display_order   int           not null,
    create_time     datetime      not null,
    update_time     datetime      not null,
    thumb_url_path  varchar(1000) null,
    constraint fk_image_product
    foreign key (product_id) references tb_product2 (ID)
);

create index idx_image_product
    on tb_product_image (product_id);