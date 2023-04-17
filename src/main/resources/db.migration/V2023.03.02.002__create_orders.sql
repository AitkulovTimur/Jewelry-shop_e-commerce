create table orders
(
    id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    user_phone_num varchar(255),
    user_custom_name varchar(255),
    order_time timestamp,
    order_status ENUM("WAITING_APPROVE", "IN_PROGRESS", "CLOSED"),
    primary key (id),
    foreign key (user_id) references user(id)
);