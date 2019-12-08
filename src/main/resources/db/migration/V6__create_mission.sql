create table missions (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    bee_id bigint unsigned not null,
    image_url VARCHAR(255),
    type tinyint,
    created_at datetime,
    updated_at datetime,
    FOREIGN KEY(`bee_id`) REFERENCES `bees` (`id`),
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;