create table bee_members (
    user_id bigint unsigned not null,
    bee_id bigint unsigned not null,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`bee_id`) REFERENCES `bees` (`id`),
    PRIMARY KEY(`user_id`, `bee_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;