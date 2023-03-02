CREATE DATABASE products;

create table product (
    id int auto_increment primary key,
    name varchar(20),
    description varchar(255),
    vendorCode int UNIQUE,
    type_id int foreign key references types(id),
    calculating_price_id int unique foreign key references calculating_price(id),
    photo_path varchar(50),
    default_price float,
    default_weight float,

);

create table types (
    id int auto_increment primary key,
    name varchar(20)

);

create table calculating_price (
    id int auto_increment primary key,
    stones_count float,
    min_weight float,
    work_price float

);

create table calculating_price_metals (
    id int auto_increment primary key,
    metal_id int foreign key references metals(id),
    calculating_price int foreign key references calculating_price(id)

);

create table calculating_price_stones (
    id int auto_increment primary key,
    stone_id int foreign key references stones(id),
    calculating_price int foreign key references calculating_price(id)

);

create table metals (
    id int auto_increment primary key,
    name varchar (20),
    price float

);

create table stones (
    id int auto_increment primary key,
    name varchar(20),
    price float

);