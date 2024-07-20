create table if not exists task
(
    id          bigint generated always as identity primary key,
    title       varchar(100)  not null unique,
    description varchar(1000) not null
);

create index if not exists task_title_index ON task (title);

comment on table task is 'Таблица заданий';
comment on column task.id is 'Идентификатор задания';
comment on column task.title is 'Название задания';
comment on column task.description is 'Описание задания';