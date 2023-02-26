create table product (
    id int auto_increment primary key,
    name varchar(20),
    description varchar(255),
    vendorCode int UNIQUE,
    type_id int foreign key references types(id),
    calculating_price_id int unique foreign key references calculating_price(id),
    photo_path varchar(50),
    default_price float,
    default_weight float

);