create table items
(
    id int NOT NULL AUTO_INCREMENT,
    product_vendor int NOT NULL,
    cart_id int NOT NULL,
    primary key (id),
    foreign key (cart_id) references user(id),
    foreign key (product_vendor) references products(vendor_code)
);