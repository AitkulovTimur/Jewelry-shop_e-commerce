create table products
(
    vendor_code int NOT NULL AUTO_INCREMENT,
    name varchar(20) unique,
    price decimal(10, 2),
    description varchar(255),
    primary key (vendor_code)
);