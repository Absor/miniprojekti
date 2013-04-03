# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table reference (
  id                        bigint not null,
  reference_type            varchar(255),
  author                    varchar(255),
  title                     varchar(255),
  year                      varchar(255),
  publisher                 varchar(255),
  constraint pk_reference primary key (id))
;

create sequence reference_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists reference;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists reference_seq;

