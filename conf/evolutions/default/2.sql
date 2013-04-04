# --- Sample dataset

# --- !Ups

insert into reference_type (id,name) values (1,'inproceedings');

insert into reference (id,reference_type_id,author,title,year,publisher) values (1001,1,'testaut1','testtit1','1','testpub1');
insert into reference (id,reference_type_id,author,title,year,publisher) values (1002,1,'testaut2','testtit2','2','testpub2');
insert into reference (id,reference_type_id,author,title,year,publisher) values (1003,1,'testaut3','testtit3','3','testpub3');
insert into reference (id,reference_type_id,author,title,year,publisher) values (1004,1,'testaut4','testtit4','4','testpub4');
insert into reference (id,reference_type_id,author,title,year,publisher) values (1005,1,'åöä{"$','testtit5','5','testpub5');

# --- !Downs

delete from reference;
delete from reference_type;