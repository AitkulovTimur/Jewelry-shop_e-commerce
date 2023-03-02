create table calculating_price_stones (
    id int auto_increment primary key,
    stone_id int foreign key references stones(id),
    calculating_price int foreign key references calculating_price(id)

);