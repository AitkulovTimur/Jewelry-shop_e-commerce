create table products
(
    id varchar(36) not null default (uuid()),
    name varchar(20) unique,
    price decimal(10, 2),
    description varchar(255),
    vendor_code int,
    photo_path varchar(50),
    primary key (id)
);