create table bee_penalties (
    id bigint unsigned not null auto_increment primary key,
    user_id bigint unsigned not null,
    bee_id bigint unsigned not null,
    status tinyint,
    created_at datetime not null,
    updated_at datetime not null,
    FOREIGN KEY(`user_id`) REFERENCES `users` (`id`),
    FOREIGN KEY(`bee_id`) REFERENCES `bees` (`id`),
    INDEX idx_bee_id_user_id (bee_id, user_id),
    INDEx idx_status_bee_id_user_id (status, bee_id, user_id),
    INDEx idx_status_user_id_bee_id (status, user_id, bee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
