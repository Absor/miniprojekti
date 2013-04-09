# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table field_type (
  field_name                varchar(255) not null,
  constraint pk_field_type primary key (field_name))
;

create table reference (
  id                        bigint not null,
  reference_type_id         bigint,
  author                    varchar(255),
  title                     varchar(255),
  year                      varchar(255),
  publisher                 varchar(255),
  constraint pk_reference primary key (id))
;

create table reference_type (
  id                        bigint not null,
  name                      varchar(255),
  constraint pk_reference_type primary key (id))
;


create table RequiredFields (
  reference_type_id              bigint not null,
  field_type_field_name          varchar(255) not null,
  constraint pk_RequiredFields primary key (reference_type_id, field_type_field_name))
;

create table OptionalFields (
  reference_type_id              bigint not null,
  field_type_field_name          varchar(255) not null,
  constraint pk_OptionalFields primary key (reference_type_id, field_type_field_name))
;
create sequence field_type_seq;

create sequence reference_seq;

create sequence reference_type_seq;

alter table reference add constraint fk_reference_referenceType_1 foreign key (reference_type_id) references reference_type (id) on delete restrict on update restrict;
create index ix_reference_referenceType_1 on reference (reference_type_id);



alter table RequiredFields add constraint fk_RequiredFields_reference_t_01 foreign key (reference_type_id) references reference_type (id) on delete restrict on update restrict;

alter table RequiredFields add constraint fk_RequiredFields_field_type_02 foreign key (field_type_field_name) references field_type (field_name) on delete restrict on update restrict;

alter table OptionalFields add constraint fk_OptionalFields_reference_t_01 foreign key (reference_type_id) references reference_type (id) on delete restrict on update restrict;

alter table OptionalFields add constraint fk_OptionalFields_field_type_02 foreign key (field_type_field_name) references field_type (field_name) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists field_type;

drop table if exists reference;

drop table if exists reference_type;

drop table if exists RequiredFields;

drop table if exists OptionalFields;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists field_type_seq;

drop sequence if exists reference_seq;

drop sequence if exists reference_type_seq;

