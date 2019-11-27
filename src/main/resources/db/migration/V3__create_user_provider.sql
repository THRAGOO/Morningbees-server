create table user_providers(
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    email varchar(255),
    provider varchar(255),
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;