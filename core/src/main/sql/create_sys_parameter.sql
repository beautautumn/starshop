/*==============================================================*/
/* Table: tb_sys_parameter                                      */
/*==============================================================*/
create table tb_sys_parameter
(
    code                 varchar(40) not null,
    value                varchar(500) not null,
    primary key (code)
);