create table users(
    id bigint not null auto_increment primary key,
    nickname varchar(255),
    status int(1),
    created_at datetime,
    updated_at datetime,
    index idx_status(status),
    index idx_nickname(nickname)
);