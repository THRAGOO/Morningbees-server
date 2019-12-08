create table user_tokens (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    fcm_token varchar(255),
    refresh_token varchar(255),
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;