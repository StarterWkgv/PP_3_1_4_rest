insert into user (first_name, last_name, age, email, password)
values ('admin', 'admin', 20, 'admin@mail.ru', '$2a$10$XqhidiGP2qmGJ/j.4ZWIu.73sAxzqx3DaQzfog/3rCeUZee3fcXai');
insert into user (first_name, last_name, age, email, password)
values ('user', 'user', 10, 'user@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');


insert into user (first_name, last_name, age, email, password)
values ('Joe', 'Biden', 93, 'joe@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Donald', 'Trump', 94, 'donny@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Barack', 'Obama', 95, 'barack@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('George', 'Bush', 96, 'junior@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Bill', 'Clinton', 97, 'billy@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Ronald', 'Reagan', 98, 'ronny@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Jimmy', 'Carter', 99, 'jimmy@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');
insert into user (first_name, last_name, age, email, password)
values ('Richard', 'Nixon', 100, 'richard@mail.ru', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');

insert into role (role)
values ('ADMIN');
insert into role (role)
values ('USER');

insert into user_role (user_id, role_id)
values (1, 2);
insert into user_role (user_id, role_id)
values (1, 1);
insert into user_role (user_id, role_id)
values (2, 2);

insert into user_role (user_id, role_id) values (3, 2);
insert into user_role (user_id, role_id) values (4, 2);
insert into user_role (user_id, role_id) values (5, 2);
insert into user_role (user_id, role_id) values (6, 2);
insert into user_role (user_id, role_id) values (7, 2);
insert into user_role (user_id, role_id) values (8, 2);
insert into user_role (user_id, role_id) values (9, 2);
insert into user_role (user_id, role_id) values (10, 2);

# login: user password: user
# login: admin password: admin
