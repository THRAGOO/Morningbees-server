create table bees (
    id bigint unsigned not null auto_increment primary key,
    title varchar(100),
    description varchar(255),
    time time,
    pay int unsigned,
    created_at datetime not null,
    updated_at datetime not null
) ENGINE=InnoDB DEFAULT CHARSET=utf8;