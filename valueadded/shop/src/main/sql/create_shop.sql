-- auto-generated definition
create table tb_shop
(
    id                   bigint auto_increment
        primary key,
    user_id              bigint                       null,
    name                 varchar(60)                  not null,
    name_abbr            varchar(12)                  not null,
    introduction         varchar(500)                 null,
    contact_person       varchar(60) charset utf8mb4  not null,
    mobile_number        varchar(20)                  not null,
    wx_qrcode_url        varchar(1000)                null comment '存放微信二维码的图片URL地址',
    can_home_delivery    char          default '1'    not null comment '0：不支持送货上门；
            1：支持送货上门；',
    geo_position         varchar(500)                 null comment 'json字符串保存地理定位。例如：

            ｛
                  name: "武汉软件新城".
                  address: "湖北省武汉市洪山区花城大道9号”,
                  latitude: 30.55573
                  longitude: 114.50387
            ｝',
    geo_latitude         decimal(12, 6)               null comment '地理位置冗余字段，方便用来查询某位置附近的店铺。',
    geo_longitude        decimal(12, 6)               null comment '地理位置冗余字段，方便用来查询某位置附近的���铺。',
    detail_address       varchar(200)                 null,
    slogan               varchar(100)                 null,
    opening_start        time                         not null,
    opening_end          time                         not null,
    allow_order_break    char          default '1'    not null comment '0：不允许休息时下单；
            1：允许休息时下单；',
    allow_join           char          default '1'    not null comment '0：不允许加盟；
            1：允许加盟；',
    join_commission_rate decimal(5, 4) default 0.1000 not null,
    join_introduction    varchar(500)                 null,
    join_stock_type      char          default '1'    not null comment '1：统一库存；2：分散库存；',
    join_price_type      char          default '1'    not null comment '1：统一定价；2：自主定价；',
    join_cashier_type    char          default '1'    not null comment '1：集中收银；2：自主收银；',
    status               char          default '0'    not null comment '0：正常营业；1：歇业；',
    is_valid             char          default '1'    not null comment '0：记录无效；1：记录有效。',
    create_time          datetime                     not null,
    update_time          datetime                     not null,
    min_delivery_time    int           default 30     null,
    order_seq            int                          null,
    order_seq_date       date                         null,
    constraint tb_shop_user
        foreign key (user_id) references tb_user (ID)
)
    comment 'ID为0的店铺，属于特殊店铺。用于设置系统商品模板、关联无店铺接龙等。并且，ID为0的店铺，is_valid字段标记该记录为无效状态。' ;

create index idx_shop_contact
    on tb_shop (contact_person);

create index idx_shop_mobilephone
    on tb_shop (mobile_number);

create index idx_shop_name
    on tb_shop (name);

create index idx_shop_user
    on tb_shop (user_id);

