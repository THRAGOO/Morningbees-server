alter table bees drop time;
alter table bees add start_time time after description;
alter table bees add end_time time after start_time;