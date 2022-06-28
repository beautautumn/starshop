create table tb_shopping_cart
(
    ID          bigint   not null
        primary key,
    user_id     bigint   not null,
    shop_id     bigint   not null,
    update_time datetime not null,
    create_time datetime not null,
    constraint fk_shopping_cart_shop
        foreign key (shop_id) references tb_shop (id),
    constraint fk_shopping_cart_user
        foreign key (user_id) references tb_user (ID)
);

create index idx_shopping_cart_shop
    on tb_shopping_cart (shop_id);

create index idx_shopping_cart_user
    on tb_shopping_cart (user_id);

-- auto-generated definition
create table tb_shopping_cart_item
(
    ID            bigint             not null
        primary key,
    cart_id       bigint             null,
    product_id    bigint             not null,
    category_id   bigint             not null,
    count         smallint           not null,
    display_order smallint default 0 not null,
    constraint FK_shopping_cart_item_cart
        foreign key (cart_id) references tb_shopping_cart (ID),
    constraint FK_shopping_cart_item_product
        foreign key (product_id) references tb_product2 (ID),
    constraint FK_shopping_cart_item_category
        foreign key (category_id) references tb_prod_category (ID)
);

create index idx_cart_item_cart
    on tb_shopping_cart_item (cart_id);

create index idx_cart_item_product
    on tb_shopping_cart_item (product_id);


