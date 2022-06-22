create table tb_user_login_log
(
    ID              bigint      not null
        primary key,
    user_id         bigint      null,
    last_login_time datetime    null,
    register_ip     varchar(20) null,
    last_login_ip   varchar(20) null,
    constraint fk_login_log_user
        foreign key (user_id) references tb_user (ID)
);

create index idx_loginlog_user
    on tb_user_login_log (user_id);

