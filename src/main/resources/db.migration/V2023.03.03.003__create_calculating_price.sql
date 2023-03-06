create table calculating_price (
    id varchar(36) not null default (uuid()),
    stones_count float,
    min_weight float,
    work_price float,
    primary key (id)
);