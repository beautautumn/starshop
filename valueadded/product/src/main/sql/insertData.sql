# 插入一个商品分类
insert into tb_prod_category(id, shop_id, name, display_order, is_valid, create_time, update_time)
values (1, 1, 'testCategory', 1, 1, now(), now());

# 分别插入限购/不限购、有/无优惠、两种不同优惠模式的测试商品
insert into tb_product2 (id, category_id, shop_id, name, introduction, unit, min_purchase, original_price_fen, display_order, labels, is_available, on_shelves, discount_type, discount_rate, discount_price_fen, purchase_limit, is_valid, create_time, update_time)
values
    (1, 1, 1, 'testProduct1', null, '斤', 0.50, 1000, 1, null, '1', '1', null, null, null, null, '1', now(), now()),
    (2, 1, 1, 'testProduct2', null, '斤', 1.00, 2000, 2, null, '1', '1', '2', null, 990, 1, '1', now(), now()),
    (3, 1, 1, 'testProduct3', null, '斤', 1.00, 3000, 3, null, '1', '1', '1', 0.9, null, 10, '1', now(), now()),
    (4, 1, 1, 'testProduct4_not_available', null, '公斤', 1.00, 4600, 4, null, '0', '1', null, null, null, 10, '1', now(), now()),
    (5, 1, 1, 'testProduct5_not_onshelves', null, '公斤', 1.00, 4600, 4, null, '1', '0', null, null, null, 10, '1', now(), now());
