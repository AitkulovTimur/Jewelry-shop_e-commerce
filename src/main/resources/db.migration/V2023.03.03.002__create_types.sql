create table types (
    id varchar(36) not null default (uuid()),
    name varchar(20),
    primary key (id)
);