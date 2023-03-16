create table users
(
    id int NOT NULL AUTO_INCREMENT,
    username varchar(36) unique,
    password varchar(36),
    primary key (id)
);