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
        foreign key (user_id) references user (user_id),
    constraint FKa68196081fvovjhkek5m97n3y
        foreign key (role_id) references role (role_id)
);