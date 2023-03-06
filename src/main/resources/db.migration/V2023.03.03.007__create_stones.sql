create table stones (
    id varchar(36) not null default (uuid()),
    name varchar(20),
    price float,
    primary key (id)
);