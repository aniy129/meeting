/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2019/11/16 20:33:47                          */
/*==============================================================*/


drop table if exists corporation;

drop table if exists manager;

drop table if exists meeting;

drop table if exists resource;

drop table if exists resource_Info;

drop table if exists user;

drop table if exists user_meeting_ship;

/*==============================================================*/
/* Table: corporation                                           */
/*==============================================================*/
create table corporation
(
   id                   int not null auto_increment comment '主键，自增',
   name                 varchar(50) not null comment '企业名称',
   manager              varchar(15) comment '企业联系人',
   phone                char(11) comment '限11位数电话号码',
   email                varchar(30) comment '格式为：xxx@xx.com',
   address              varchar(100) comment '详细地址',
   image                varchar(200) comment '企业的宣传图片，仅限一张',
   intro                varchar(20) comment '对企业的简略介绍，仅限文本，限20字',
   detail               varchar(500) comment '对企业的详细介绍，可文本+图片等，长度无上限',
   vip                  varchar(10) not null comment '根据企业在平台申请会议情况对企业有vip之分：0-普通会员，1-vip会员',
   reg_time             datetime not null comment '企业在该平台的注册时间',
   primary key (id)
);

alter table corporation comment '企业信息表';

/*==============================================================*/
/* Table: manager                                               */
/*==============================================================*/
create table manager
(
   id                   int not null auto_increment comment '主键，自增',
   code                 varchar(15) not null comment '管理员编码',
   account              varchar(50) comment '登录账号，限制15个字符长度',
   password             varchar(32) not null comment '登录密码，限制32个字符长度',
   name                 varchar(30) not null comment '管理员姓名',
   sex                  tinyint comment '0-男，1-女',
   age                  int comment '年龄限制18-100岁',
   phone                varchar(11) comment '仅限11位电话号码',
   email                varchar(30) comment '管理员邮箱',
   photo                varchar(200) comment '管理员照片',
   available            tinyint(2) NOT NULL DEFAULT '1' COMMENT '是否可用',
   primary key (id)
);

alter table manager comment '管理员表
对平台上的管理人员进行权限管理，实现对管理人员信息、权限的增删改查';

/*==============================================================*/
/* Table: meeting                                               */
/*==============================================================*/
create table meeting
(
   id                   int not null auto_increment comment '主键',
   res_id               int comment '资源主键，自增',
   cor_id               int comment '主键，自增',
   title                varchar(20) not null comment '会议主标题',
   subtitle             varchar(50) comment '会议副标题',
   intro                varchar(20) comment '对会议的简略介绍，仅限文本，长度<=20',
   detail               varchar(500) comment '对会议的详细介绍，可文本+图片等，长度无上限',
   max_num              int comment '会议的参会人数上限（不包括工作人员）',
   days                 int comment '天数>0，以1天为单位',
   audit                tinyint not null comment '0-待审核，1-审核通过，2-审核未通过',
   state                tinyint not null comment '0-待举办，1-举办中，2-已结束',
   start_time           datetime comment '会议开始时间',
   end_time             datetime comment '会议结束时间，须迟于会议开始时间',
   apply_time           datetime comment '会议在平台的申请时间，须早于会议开始时间',
   cost                 decimal comment '会议的总费用，人民币',
   primary key (id)
);

alter table meeting comment '会议的信息表
';

/*==============================================================*/
/* Table: resource                                              */
/*==============================================================*/
create table resource
(
   id                   int not null auto_increment comment '主键，自增',
   type                 char(1) not null comment '0-会场资源，1-住宿资源',
   intro                varchar(20) comment '对资源的简单描述，仅限文本，长度<=20字',
   primary key (id)
);

alter table resource comment '资源总表，根据类型区分资源的种类，例如：会场资源，住宿资源等';

/*==============================================================*/
/* Table: resource_Info                                         */
/*==============================================================*/
create table resource_Info
(
   id                   int not null auto_increment comment '资源主键，自增',
   res_id               int comment '主键，自增',
   type                 tinyint not null comment '0-会场资源，1-住宿资源',
   name                 varchar(50) comment '资源的名称，例如xx会场，xx酒店，xx汽车租用公司',
   state                tinyint not null comment '0-占用中,不可用，1-空闲中,可用',
   address              varchar(100) not null comment '资源的具体位置（会场资源-会场详细到层数房间号，住宿资源-住宿地址）',
   count                int comment '资源总共的数量（不考虑是否可用），例如，会场资源的会场数量，住宿资源的客房数量',
   toplimit             int comment '资源能够容纳安排的总人数',
   image                varchar(200) comment '资源的宣传图片，仅限一张',
   intro                varchar(20) comment '对资源的简单描述，仅限文本，长度<=20字',
   detail               varchar(500) comment '对资源的详细简述，可文本+图片等，长度不限',
   cost                 decimal comment '人民币为单位，会场资源的费用以会场平均价格为单位，住宿资源的费用以客房平均每间为单位',
   manager              varchar(15) comment '该资源的联系人姓名',
   phone                varchar(11) not null comment '限定11位数手机号码',
   email                varchar(30) comment '格式为：xxx@xx.com',
   primary key (id)
);

alter table resource_Info comment '详细资源表
会场
住宿
资源的信息';

/*==============================================================*/
/* Table: user                                                  */
/*==============================================================*/
create table user
(
   id                   int not null auto_increment comment '主键，自增',
   name                 varchar(15) not null comment '真实姓名',
   nickname             varchar(15) not null comment '昵称,作为登录账号',
   password             varchar(50) not null comment '登录密码，限制50个字符长度',
   identity             tinyint not null comment '登录身份：0-企业管理员，1-企业用户',
   identify_num         char(18) not null comment '18位数身份证号码',
   phone                char(11) comment '11位数电话号码',
   email                varchar(30) comment '用户邮箱',
   cor_id               int(11) DEFAULT NULL COMMENT '企业ID',
   primary key (id)
);

alter table user comment '个人用户表
在企业端上
记录用户的姓名等详细信息';

/*==============================================================*/
/* Table: user_meeting_ship                                     */
/*==============================================================*/
create table user_meeting_ship
(
   use_id               int not null comment '主键，自增',
   id                   int not null comment '主键',
   primary key (use_id, id)
);

alter table meeting add constraint FK_Relationship_2 foreign key (cor_id)
      references corporation (id) on delete restrict on update restrict;

alter table meeting add constraint FK_Relationship_3 foreign key (res_id)
      references resource_Info (id) on delete restrict on update restrict;

alter table resource_Info add constraint FK_Relationship_1 foreign key (res_id)
      references resource (id) on delete restrict on update restrict;

alter table user_meeting_ship add constraint FK_meeting_ship foreign key (id)
      references meeting (id) on delete restrict on update restrict;

alter table user_meeting_ship add constraint FK_user_ship foreign key (use_id)
      references user (id) on delete restrict on update restrict;

/*==============================================================*/
/* Data: 基础数据                                               */
/*==============================================================*/
INSERT INTO manager (id, code, account, `password`, name, available)
VALUES ('1', '1', 'root', 'root', 'root', '1');
INSERT INTO user (id, name, nickname, `password`, identity, identify_num)
VALUES ('1', 'Root', 'root', 'e10adc3949ba59abbe56e057f20f883e', '0', '100100199901010001');
INSERT INTO user (id, name, nickname, `password`, identity, identify_num)
VALUES ('2', 'User', 'user', 'e10adc3949ba59abbe56e057f20f883e', '1', '100100199901010002');

