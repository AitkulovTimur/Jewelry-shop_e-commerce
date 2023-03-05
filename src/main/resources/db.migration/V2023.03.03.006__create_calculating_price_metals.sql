create table calculating_price_metals
(
    id varchar(36) not null default (uuid()),
    metal_id varchar(36),
    calculating_price_id varchar(36),
    primary key (id),
    foreign key (metal_id) references metals(id),
    foreign key (calculating_price_id) references calculating_price(id)
);