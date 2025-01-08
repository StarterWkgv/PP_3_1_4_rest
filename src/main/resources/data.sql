insert into user (first_name, last_name, age, email, password)
values ('Admin', 'Adminov', 20, 'admin', '$2a$10$XqhidiGP2qmGJ/j.4ZWIu.73sAxzqx3DaQzfog/3rCeUZee3fcXai');
insert into user (first_name, last_name, age, email, password)
values ('User', 'Userson', 10, 'user', '$2a$10$xTfGvf6E./HJxELXL1w13O2H/XNOW3p29EmrfK7yX42FAhogV1ZYe');

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
