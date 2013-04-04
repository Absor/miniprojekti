# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

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

create sequence reference_seq;

create sequence reference_type_seq;

alter table reference add constraint fk_reference_referenceType_1 foreign key (reference_type_id) references reference_type (id) on delete restrict on update restrict;
create index ix_reference_referenceType_1 on reference (reference_type_id);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists reference;

drop table if exists reference_type;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists reference_seq;

drop sequence if exists reference_type_seq;

