create table user_providers (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    email varchar(255),
    provider varchar(255),
    created_at datetime not null,
    updated_at datetime not null,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    UNIQUE idx_user_id (`user_id`),
    UNIQUE idx_email_provider_unique (`email`, `provider`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;