create table comments (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    mission_id bigint unsigned not null,
    comment varchar(255),
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`mission_id`) REFERENCES `missions` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;