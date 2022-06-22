create table tb_user
(
    ID              bigint auto_increment
        primary key,
    name            varchar(40) charset utf8mb3      null,
    password        varchar(40) charset utf8mb3      null comment '微信登录的用户，password就是openid',
    gender          char charset utf8mb3             not null comment '0：未知、1：男、2：女',
    type            char charset utf8mb3 default '1' not null comment '1：普通用户；2：团长；3：店主；',
    nickname        varchar(100)                     null,
    avatarUrl       varchar(500) charset utf8mb3     null,
    country         varchar(40) charset utf8mb3      null,
    province        varchar(40) charset utf8mb3      null,
    city            varchar(40) charset utf8mb3      null,
    language        varchar(40) charset utf8mb3      null,
    mobile          varchar(20) charset utf8mb3      null,
    openid          varchar(40) charset utf8mb3      not null,
    is_valid        char charset utf8mb3 default '1' not null comment '0：无效记录（被删除）；1：有效记录。',
    register_time   datetime                         not null,
    last_login_time datetime                         null,
    register_ip     varchar(20) charset utf8mb3      null,
    last_login_ip   varchar(20) charset utf8mb3      null,
    update_time     datetime                         not null,
    cur_shop_id     bigint                           null comment '记录用户当前打开浏览的店铺ID'
);

create index idx_user_openid
    on tb_user (openid);

create table tb_user_token
(
    user_id     bigint       not null
        primary key,
    token       varchar(32)  not null,
    session_key varchar(100) not null,
    expire_time datetime     not null,
    update_time datetime     not null,
    constraint FK_Reference_45
        foreign key (user_id) references tb_user (ID)
)
    charset = utf8mb3;

create index idx_token_user
    on tb_user_token (user_id);



