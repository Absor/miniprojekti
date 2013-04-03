# --- Sample dataset

# --- !Ups

insert into reference (id,reference_type,author,title,year,publisher) values (1001,'book','testaut1','testtit1','1','testpub1');
insert into reference (id,reference_type,author,title,year,publisher) values (1002,'book','testaut2','testtit2','2','testpub2');
insert into reference (id,reference_type,author,title,year,publisher) values (1003,'book','testaut3','testtit3','3','testpub3');
insert into reference (id,reference_type,author,title,year,publisher) values (1004,'book','testaut4','testtit4','4','testpub4');
insert into reference (id,reference_type,author,title,year,publisher) values (1005,'book','testaut5','testtit5','5','testpub5');

# --- !Downs

delete from reference;