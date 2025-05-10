-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE expense
(
    id       SERIAL NOT NULL,
    title    TEXT   NOT NULL,
    date     DATE   NOT NULL,
    category TEXT,
    amount   FLOAT  NOT NULL
);

-- changeset tokovenko:2
INSERT INTO expense (id, title, date, category, amount)
VALUES (1, 'Проезд в автобусе', '2021-01-30', 'Транспорт', 50),
       (2, 'Проезд в метро', '2021-01-30', 'Транспорт', 50),
       (3, 'Покупка книги', '2021-01-31', 'Прочие покупки', 300),
       (4, 'Покупка продуктов', '2021-01-31', 'Покупка продуктов', 450),
       (5, 'Поход в кино', '2021-02-01', 'Развлечения', 400),
       (6, 'Кофе', '2021-02-01', 'Еда вне дома', 150),
       (7, 'Покупка продуктов', '2021-02-02', 'Покупка продуктов', 600),
       (8, 'Поход в театр', '2021-02-14', 'Развлечения', 1000),
       (9, 'Цветы', '2021-02-14', null, 500);

-- changeset tokovenko:3
CREATE INDEX category_index ON expense (category);

-- changeset tokovenko:4
CREATE TABLE reader
(
    id              BIGSERIAL,
    name            VARCHAR(12) NOT NULL,
    second_name     VARCHAR(16) NOT NULL,
    surname         VARCHAR(30) NOT NULL,
    personal_number INTEGER     NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

-- changeset tokovenko:5
INSERT INTO reader (id, name, second_name, surname, personal_number)
VALUES (1, 'Иван', 'Иванович', 'Иванов', 10),
       (2, 'Пётр', 'Петрович', 'Петров', 11),
       (3, 'Фёдор', 'Фёдорович', 'Фёдоров', 18),
       (4, 'Сидор', 'Фёдорович', 'Фёдоров', 110);

-- changeset tokovenko:6
CREATE TABLE book
(
    id        BIGSERIAL,
    author    VARCHAR(30) NOT NULL,
    title     VARCHAR(30) NOT NULL,
    year      SMALLINT CHECK ( year > 1970 ),
    reader_id BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (reader_id) REFERENCES reader (id)
);

-- changeset tokovenko:7
CREATE TABLE book_cover
(
    id            BIGSERIAL,
    file_path     VARCHAR(255) NOT NULL,
    file_size     INTEGER      NOT NULL,
    image_preview oid          NOT NULL,
    media_type    VARCHAR(30)  NOT NULL,
    book_id       BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (book_id) REFERENCES book (id)
);

-- changeset tokovenko:8
INSERT INTO book (id, author, title, year, reader_id)
VALUES (1, 'Л.Н.Толстой', 'Война и мир', 1986, null),
       (2, 'Н.В.Гоголь', 'Мёртвые души', 1986, 2),
       (3, 'А.С.Пушкин', 'Евгений Онегин', 1986, 1),
       (4, 'Ф.М.Достоевский', 'Идиот', 1986, null),
       (5, 'Ф.М.Достоевский', 'Братья Карамазовы', 1986, 2),
       (6, 'Ф.М.Достоевский', 'Преступление и наказание', 1987, null),
       (7, 'Н.В.Гоголь', 'Ревизор', 1987, null),
       (8, 'Н.В.Гоголь', 'Вий', 1989, null),
       (9, 'Н.В.Гоголь', 'Тарас Бульба', 1989, null),
       (10, 'А.С.Пушкин', 'Борис Годунов', 1987, null),
       (11, 'А.С.Пушкин', 'Капитанская дочка', 1987, null),
       (12, 'А.С.Пушкин', 'Медный всадник', 1989, 1),
       (13, 'А.С.Пушкин', 'Пиковая дама', 1989, null);

-- changeset tokovenko:9
CREATE INDEX reader_index ON book (reader_id);

-- changeset tokovenko:10
CREATE INDEX book_index ON book_cover (book_id);

-- changeset tokovenko:11
CREATE TABLE city
(
    id          BIGSERIAL,
    name        VARCHAR(255)    NOT NULL,
    latitude    NUMERIC(16, 13) NOT NULL,
    longitude   NUMERIC(16, 13) NOT NULL,
    country     VARCHAR(30)     NOT NULL,
    state       VARCHAR(30)     NOT NULL,
    local_names TEXT            NOT NULL,
    PRIMARY KEY (id)
);

-- changeset tokovenko:12
CREATE TABLE login
(
    id       BIGSERIAL,
    login    VARCHAR(30) NOT NULL UNIQUE,
    password TEXT        NOT NULL,
    role     SMALLINT    NOT NULL,
    PRIMARY KEY (id)
);