create table mission_votes(
    user_id bigint unsigned not null,
    mission_id bigint unsigned not null,
    type tinyint,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`mission_id`) REFERENCES `missions` (`id`),
    PRIMARY KEY(`user_id`, `mission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;