create table bees (
    id bigint unsigned not null auto_increment primary key,
    title varchar(30),
    time time,
    pay int unsigned,
    created_at datetime,
    updated_at datetime
) ENGINE=InnoDB DEFAULT CHARSET=utf8;