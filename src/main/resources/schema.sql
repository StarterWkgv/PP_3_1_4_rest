drop table if exists user_role;
drop table if exists role;
drop table if exists user;

create table if not exists role
(
    role_id int auto_increment primary key,
    role    varchar(255) not null unique
);

create table if not exists user
(
    user_id    bigint auto_increment primary key,
    age        tinyint      not null,
    email      varchar(255) not null unique,
    first_name varchar(50)  not null,
    last_name  varchar(50)  not null,
    password   varchar(255) not null
);

create table if not exists user_role
(
    user_id bigint not null,
    role_id int not null,
    primary key (user_id, role_id),
    foreign key (user_id) references user (user_id),
    foreign key (role_id) references role (role_id)
);