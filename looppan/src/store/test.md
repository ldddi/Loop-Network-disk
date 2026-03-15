create table file_info
(
    file_id          varchar(12)          not null comment '文件ID',
    user_id          int                  not null comment '用户ID',
    file_last_path   varchar(1000)        null comment '文件的md5值',
    file_pid         varchar(12)          null comment '文件的父 ID',
    file_size        varchar(100)         null comment '文件的大小',
    file_name        varchar(200)         null comment '文件名',
    file_cover       varchar(100)         null comment '文件封面',
    file_path        varchar(1000)        null comment '文件路径',
    create_time      datetime             null comment '创建时间',
    last_update_time datetime             null comment '最后修改时间',
    folder_type      tinyint              null comment '0：文件， 1：目录',
    file_category    tinyint              null comment '文件分类：1视频，2音频，3图片，4文档，5其他',
    recovery_time    datetime             null comment '进入回收站的时间',
    del_flag         tinyint    default 2 null comment '0删除，1回收站，2正常',
    shared           tinyint(1) default 0 null,
    primary key (file_id, user_id)
)
    engine = InnoDB;

create index idx_create_time
    on file_info (create_time desc);

create index idx_del_flag
    on file_info (del_flag);

create index idx_file_pid
    on file_info (file_pid);

create index idx_md5
    on file_info (file_last_path);

create index idx_user_id
    on file_info (user_id);

create table file_shared
(
    share_id        varchar(20)              not null comment '分享的文件id',
    share_p_id      varchar(20)  default '0' null,
    file_id         varchar(12)              not null comment '文件id',
    user_id         int                      not null comment '用户id',
    user_nick_name  varchar(20)              null comment '分享用户的昵称',
    user_avatar     varchar(500)             null comment '用户头像',
    file_name       varchar(200)             null comment '文件名',
    file_path       varchar(1000)            null,
    file_category   tinyint                  null comment '文件分类：1视频，2音频，3图片，4文档，5其他',
    file_size       varchar(100)             null,
    file_url        varchar(200) default '0' null comment '文件分享的url',
    share_time      datetime                 null comment '分享时间',
    fail_time       datetime                 null comment '失效时间',
    extraction_code varchar(5)               null comment '提取码',
    views           int          default 0   null
)
    comment '分享的文件' engine = InnoDB;

create index file_shared_file_id_user_id_index
    on file_shared (file_id, user_id);

create index file_shared_share_id_index
    on file_shared (share_id);

create table user_info
(
    user_id         int auto_increment comment '用户id'
        primary key,
    nick_name       varchar(20)              null comment '昵称',
    email           varchar(150)             null comment '邮箱',
    avatar          varchar(500)             null comment '头像',
    password        varchar(80)              null comment '密码',
    join_time       datetime                 null comment '登陆日期',
    last_login_time datetime                 null comment '最后一次登陆日期',
    total_space     bigint default 524288000 null comment '总空间',
    use_space       bigint                   null comment '使用空间',
    constraint key_mail
        unique (email),
    constraint key_nick_name
        unique (nick_name),
    constraint user_info_pk
        unique (nick_name)
)
    comment 'userInfo 表' engine = InnoDB;

