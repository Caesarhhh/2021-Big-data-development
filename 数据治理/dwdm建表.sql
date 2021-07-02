-- 地市商品类别汇总
drop table if exists week5.citytype_collect;
create table week5.citytype_collect
(
  int_date  VARCHAR(6),
  city_name VARCHAR(4),
  splb      VARCHAR(34),
  xse      VARCHAR(30)
);
comment on table week5.citytype_collect
  is '地市销售汇总';
comment on column week5.citytype_collect.int_date
  is '月份';
comment on column week5.citytype_collect.city_name
  is '地市名称';
comment on column week5.citytype_collect.splb
  is '商品类别';
comment on column week5.citytype_collect.xse
  is '销售额';
-- 性别商品类别汇总
drop table if exists week5.gendertype_collect;
create table week5.gendertype_collect
(
  int_date  VARCHAR(6),
  gender VARCHAR(4),
  splb      VARCHAR(34),
  xse      VARCHAR(30)
);
comment on table week5.gendertype_collect
  is '性别商品类别汇总';
comment on column week5.gendertype_collect.int_date
  is '月份';
comment on column week5.gendertype_collect.gender
  is '性别';
comment on column week5.gendertype_collect.splb
  is '商品类别';
comment on column week5.gendertype_collect.xse
  is '销售额';
	-- 收货地市汇总
drop table if exists week5.city_collect;
create table week5.city_collect
(
  int_date  VARCHAR(6),
  city_name VARCHAR(4),
  xse      VARCHAR(30)
);
comment on table week5.city_collect
  is '地市销售汇总';
comment on column week5.city_collect.int_date
  is '月份';
comment on column week5.city_collect.city_name
  is '地市名称';
comment on column week5.city_collect.xse
  is '销售额';
	-- 月份信息汇总
drop table if exists week5.date_collect;
create table week5.date_collect
(
  int_date  VARCHAR(6),
	zcrs VARCHAR(20),
  xse      VARCHAR(30)
);
comment on table week5.date_collect
  is '月份信息汇总';
comment on column week5.date_collect.zcrs
  is '注册人数';
comment on column week5.date_collect.xse
  is '销售额';
	