/*==============================================================*/
/* Table: tb_customer                                           */
/*==============================================================*/
create table tb_customer
(
    id                   bigint(20) not null auto_increment,
    shop_id              bigint(20) not null,
    user_id              bigint(20),
    avatarUrl            varchar(500),
    name                 varchar(60) not null,
    gender               char(1) comment '1：男；2：女；3：未知；',
    mobile_phone         varchar(20),
    level                varchar(20),
    birthday             date,
    country              varchar(40),
    province             varchar(40),
    city                 varchar(40),
    district             varchar(40),
    labels               varchar(1000) comment 'json字符串格式保存标签。例： ["忠实客户", "宝妈宝爸"]',
    memo                 varchar(500),
    has_read             char(1) not null default '1' comment '0：未读；1：已读。',
    is_valid             char(1) not null default '1' comment '0：无效记录（被删除）；1：有效记录。',
    create_time          datetime not null,
    update_time          datetime not null,
    primary key (id)
);

/*==============================================================*/
/* Index: idx_cust_shop                                         */
/*==============================================================*/
create index idx_cust_shop on tb_customer
    (
     shop_id
        );

/*==============================================================*/
/* Index: idx_cust_user                                         */
/*==============================================================*/
create index idx_cust_user on tb_customer
    (
     user_id
        );