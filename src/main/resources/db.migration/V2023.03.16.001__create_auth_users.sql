create table auth_users (
    id varchar(36) not null default (uuid()),
    email varchar(20),
    password varchar(20),
    primary key (id)
);