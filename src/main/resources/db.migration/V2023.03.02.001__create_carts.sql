create table carts
(
    id int NOT NULL AUTO_INCREMENT,
    user_id int NOT NULL,
    primary key (id),
    foreign key (user_id) references user(id)
);