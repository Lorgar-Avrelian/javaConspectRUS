## Пример 1:

> [[_оглавление_]](../README.md/#83-sql)

> [**[8.3.8 Liquibase]**](/conspect/8.md/#838-liquibase)

- добавление зависимости в файле _pom.xml_:

```xml

<dependency>
    <groupId>org.liquibase</groupId>
    <artifactId>liquibase-core</artifactId>
</dependency>
```

- добавление пути файла _Liquibase_, управляющего и содержащего изменения в БД, в _properties_-файл:

```properties
# Application properties
spring.application.name=Java conspectus RUS
server.port=8080
# Specific parameter for RandomizeConfig class
rand.diapazon=1000
# Specific parameter for BookCoverService implementations
books.covers.dir.path=books/covers
# Spring Data JPA parameters for DB connection
spring.datasource.url=jdbc:postgresql://localhost:5432/library
spring.datasource.username=library_user
spring.datasource.password=123
# Hibernate settings
# Hibernate ddl auto (create, create-drop, validate, update)
spring.jpa.hibernate.ddl-auto=validate
# Settings of showing SQL-requests Spring Data JPA
spring.jpa.show-sql=true
# Liquibase change-log settings
spring.liquibase.change-log=classpath:liquibase/changelog-master.yml
```

- создание файла _changelog-master.yml_ и задание в нём файла изменений в БД:

```yaml
databaseChangeLog:
  - include:
      file: liquibase/scripts/conspectus.sql
```

- создание _sql_-файла _conspectus.sql_ для хранения изменений в БД в директории _src/main/resources/liquibase/scripts_:

```sql
-- liquibase formatted sql

-- changeset tokovenko:1
CREATE TABLE expense
(
    id       SERIAL  NOT NULL,
    title    TEXT    NOT NULL,
    date     DATE    NOT NULL,
    category TEXT,
    amount   INTEGER NOT NULL
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
    id              SERIAL,
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
    id        SERIAL,
    author    VARCHAR(30) NOT NULL,
    title     VARCHAR(30) NOT NULL,
    year      SMALLINT CHECK ( year > 1970 ),
    reader_id INTEGER,
    PRIMARY KEY (id),
    FOREIGN KEY (reader_id) REFERENCES reader (id)
);

-- changeset tokovenko:7
CREATE TABLE book_cover
(
    id            SERIAL,
    file_path     VARCHAR(255) NOT NULL,
    file_size     INTEGER      NOT NULL,
    image_preview oid          NOT NULL,
    media_type    VARCHAR(30)  NOT NULL,
    book_id       INTEGER,
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
```