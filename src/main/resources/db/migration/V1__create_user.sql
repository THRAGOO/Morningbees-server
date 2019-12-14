create table users (
    id bigint unsigned not null auto_increment primary key,
    nickname varchar(255),
    status int(1),
    created_at datetime not null,
    updated_at datetime not null,
    index idx_status(status),
    UNIQUE idx_nickname(nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;