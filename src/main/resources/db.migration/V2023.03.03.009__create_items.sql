create table items
(
    id int NOT NULL AUTO_INCREMENT,
    product_vendor int NOT NULL,
    cart_id int NOT NULL,
    quantity int NOT NULL default 0,
    max_quantity int,
    primary key (id),
    foreign key (cart_id) references carts(id),
    foreign key (product_vendor) references products(vendor_code)
);