create table comments (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    mission_id bigint unsigned not null,
    comment varchar(255),
    created_at datetime not null,
    updated_at datetime not null,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`mission_id`) REFERENCES `missions` (`id`),
    INDEX idx_user_id_mission_id (user_id, mission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;