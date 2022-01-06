create table persons
(
    id           int auto_increment
        primary key,
    firstname    varchar(70) not null,
    lastname     varchar(80) not null,
    nationalCode varchar(10) not null,
    age          int         not null,
    email        varchar(50) not null,
    mobile       varchar(15) not null,
    constraint nationalCode
        unique (nationalCode)
);
alter table customer change customercity customer_city VARCHAR(225);

ALTER TABLE table
    ADD [COLUMN] column_name_1 column_1_definition [FIRST|AFTER existing_column],
ADD [COLUMN] column_name_2 column_2_definition [FIRST|AFTER existing_column],
...;

