drop table if exists user_role;
drop table if exists role;
drop table if exists user;

create table if not exists role
(
    role_id bigint auto_increment primary key,
    role    varchar(255) null
);

create table if not exists user
(
    user_id bigint auto_increment primary key,
    age        tinyint      null,
    email      varchar(255) null,
    first_name varchar(50)  null,
    last_name  varchar(50)  null,
    password   varchar(255) null,
    constraint UK_ob8kqyqqgmefl0aco34akdtpe
        unique (email)
);

create table if not exists user_role
(
    user_id bigint not null,
    role_id bigint not null,
    constraint FK859n2jvi8ivhui0rl0esws6o
        foreign key (user_id) references spring_hiber.user (user_id),
    constraint FKa68196081fvovjhkek5m97n3y
        foreign key (role_id) references spring_hiber.role (role_id)
);

insert into user (first_name, last_name, age, email, password)
values ('Admin', 'Adminov', 20, 'admin', '$2a$10$XqhidiGP2qmGJ/j.4ZWIu.73sAxzqx3DaQzfog/3rCeUZee3fcXai');
insert into user (first_name, last_name, age, email, password)
values ('User', 'Userson', 10, 'user', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');

insert into role (role)
values ('ROLE_ADMIN');
insert into role (role)
values ('ROLE_USER');

insert into user_role (user_id, role_id)
values (1, 1);
insert into user_role (user_id, role_id)
values (2, 2);