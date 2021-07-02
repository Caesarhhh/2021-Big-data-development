-- drop schema if exists week5 CASCADE;
-- create schema week5;
-- 地市
drop table if exists week5.city;
create table week5.city
(
  city_code VARCHAR(4) PRIMARY KEY,
  city_name VARCHAR(4)
);
-- Add comments to the table 
comment on table week5.city
  is '区域';
-- Add comments to the columns 
comment on column week5.city.city_code
  is '地市编码';
comment on column week5.city.city_name
  is '地市名称';
-- 用户
drop table if exists week5.users;
create table week5.users
(
  user_id VARCHAR(4) PRIMARY KEY,
  user_name VARCHAR(20),
  user_gender VARCHAR(6), --1为男，0为女
  register_date VARCHAR(6),
  user_years VARCHAR(6),
  email VARCHAR(100),
  phone VARCHAR(100)
);
comment on table week5.users
  is '用户';
comment on column week5.users.user_id
  is '用户编码';
comment on column week5.users.user_name
  is '用户名称';
comment on column week5.users.user_gender
  is '性别';
comment on column week5.users.register_date
  is '用户注册月份';
 comment on column week5.users.user_years
  is '用户出生年份';
 comment on column week5.users.email
  is '用户邮箱';
 comment on column week5.users.phone
  is '用户手机号码';
 -- 商家
drop table if exists week5.shop;
create table week5.shop
(
  shop_id VARCHAR(4) PRIMARY KEY,
  shop_name VARCHAR(20)
);
comment on table week5.shop
  is '商店';
comment on column week5.shop.shop_id
  is '商店编码';
comment on column week5.shop.shop_name
  is '商店名称';
  -- 商品
drop table if exists week5.goods;
create table week5.goods
(
  good_id VARCHAR(4) PRIMARY KEY,
  good_name VARCHAR(20),
  good_type_id VARCHAR(4),
  good_city VARCHAR(100),
  price VARCHAR(20),
  shop_id VARCHAR(4),
  num VARCHAR(420)
);
comment on table week5.goods
  is '商品';
comment on column week5.goods.good_id
  is '商品编码';
comment on column week5.goods.good_name
  is '商品名称';
 comment on column week5.goods.good_type_id
  is '商品类别编码';
 comment on column week5.goods.num
  is '商品数量';
 comment on column week5.goods.good_city
  is '发货城市';
 comment on column week5.goods.shop_id
  is '商品所在商店编码';
 comment on column week5.goods.price
  is '商品价格';
 -- 商品类别
 drop table if exists week5.goodtype;
create table week5.goodtype
(
  goodtype_id VARCHAR(4) PRIMARY KEY,
  goodtype_name VARCHAR(20)
);
comment on table week5.goodtype
  is '商品类别';
comment on column week5.goodtype.goodtype_id
  is '商品类别编码';
comment on column week5.goodtype.goodtype_name
  is '商品类别名称';
  -- 商品购买
 drop table if exists week5.purchase;
create table week5.purchase
(
  user_id VARCHAR(4) ,
  good_id VARCHAR(4),
  receive_city VARCHAR(20),
  num VARCHAR(20),
	price VARCHAR(20),
  purchase_date VARCHAR(6)
);
comment on table week5.purchase
  is '商品购买';
comment on column week5.purchase.user_id
  is '购买用户编码';
comment on column week5.purchase.good_id
  is '购买商品编码';
comment on column week5.purchase.receive_city
  is '收货城市';
 comment on column week5.purchase.num
  is '购买数量';
 comment on column week5.purchase.purchase_date
  is '购买月份';
