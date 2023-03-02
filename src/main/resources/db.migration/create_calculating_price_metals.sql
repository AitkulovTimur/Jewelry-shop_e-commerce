create table calculating_price_metals (
    id int auto_increment primary key,
    metal_id int foreign key references metals(id),
    calculating_price int foreign key references calculating_price(id)

);