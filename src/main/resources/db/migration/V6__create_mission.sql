create table missions (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    bee_id bigint unsigned not null,
    image_url VARCHAR(255),
    type tinyint,
    created_at datetime not null,
    updated_at datetime not null,
    FOREIGN KEY(`bee_id`) REFERENCES `bees` (`id`),
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    INDEX idx_user_id_bee_id (user_id, bee_id),
    INDEX idx_cretaed_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;