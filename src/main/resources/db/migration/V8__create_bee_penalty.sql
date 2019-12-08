create table bee_penalties (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    bee_id bigint unsigned not null,
    status tinyint,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`bee_id`) REFERENCES `bees` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;