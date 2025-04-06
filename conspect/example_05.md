## Пример 1:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8321-select-запросы)

- выбрать все столбцы в таблице:

```sql
SELECT *
FROM book;
```

- выбрать один столбец в таблице:

```sql
SELECT author
FROM book;
```

- выбрать несколько столбцов в таблице:

```sql
SELECT author, title
FROM book;
```

- выбрать несколько столбцов из нескольких таблиц:

```sql
SELECT book.author, book.title, reader.name, reader.surname
FROM book,
     reader;
```

- выбрать несколько столбцов из нескольких таблиц с псевдонимами:

```sql
SELECT b.author, b.title, r.name, r.surname
FROM book AS b,
     reader AS r;
```

- выбрать все столбцы из нескольких таблиц:

```sql
SELECT book.*, reader.*
FROM book,
     reader;
```

- выбрать все столбцы из нескольких таблиц с псевдонимами:

```sql
SELECT b.*, r.*
FROM book AS b,
     reader AS r;
```

## Пример 2:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8321-select-запросы)

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых больше указанного значения:

```sql
SELECT *
FROM book
WHERE id > 10;
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых равны указанному значению:

```sql
SELECT *
FROM book
WHERE id = 10;
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых меньше указанного значения:

```sql
SELECT *
FROM book
WHERE id < 10;
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых не равны нулю:

```sql
SELECT *
FROM book
WHERE reader_id IS NOT NULL;
```

```sql
SELECT *
FROM book
WHERE reader_id NOTNULL;
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых равны нулю:

```sql
SELECT *
FROM book
WHERE reader_id IS NULL;
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых находятся в диапазоне значений:

```sql
SELECT *
FROM book
WHERE author IN ('Н.В. Гоголь', 'А.С. Пушкин');
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых не находятся в диапазоне значений:

```sql
SELECT *
FROM book
WHERE author NOT IN ('Н.В. Гоголь', 'А.С. Пушкин');
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых содержат указанное значение:

```sql
SELECT *
FROM book
WHERE author LIKE '%Гоголь%';
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых заканчиваются указанным значением:

```sql
SELECT *
FROM book
WHERE author LIKE '%Гоголь';
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбце которых начинаются указанным значением:

```sql
SELECT *
FROM book
WHERE author LIKE 'А.С.%';
```

- выбрать все столбцы таблицы и вывести только те строки, значения в столбцах которых соответствуют обоим условиям:

```sql
SELECT *
FROM book
WHERE author NOT IN ('Н.В. Гоголь', 'А.С. Пушкин')
  AND reader_id IS NOT NULL;
```

```sql
SELECT *
FROM book
WHERE author NOT IN ('Н.В. Гоголь', 'А.С. Пушкин')
   OR reader_id IS NOT NULL;
```

## Пример 3:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8321-select-запросы)

- выбрать столбец таблицы, выполнить над ним агрегатную функцию и вывести результат выполнения агрегатной функции в
  соответствии со столбцом, по которому она выполнялась:

```sql
SELECT author, COUNT(author)
FROM book
GROUP BY author;
```

- выбрать столбец таблицы, выполнить над ним агрегатную функцию и вывести результат выполнения агрегатной функции в
  соответствии со столбцом, по которому она выполнялась, где выполняется условие:

```sql
SELECT author, COUNT(author)
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author;
```

- выбрать несколько столбцов таблицы, выполнить над одним из них агрегатную функцию и вывести результат выполнения
  агрегатной функции, где выполняется условие, отсортированный сначала по столбцу, по которому выполнялась агрегатная
  функция, а потом по столбцу, по которому - нет:

```sql
SELECT author, year, COUNT(author)
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author, year;
```

- выбрать несколько столбцов таблицы и вывести результат, где выполняется условие, отсортированный сначала по первому
  столбцу, а потом по второму столбцу:

```sql
SELECT author, year
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author, year;
```

## Пример 4:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8321-select-запросы)

- выбрать несколько столбцов таблицы и вывести результат, где выполняется условие, отсортированный сначала по первому
  столбцу, а потом по второму столбцу, где значения в одном из столбцов выполняют условие:

```sql
SELECT author, year
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author, year
HAVING year > 1986;
```

- выбрать несколько столбцов таблицы, выполнить над одним из них агрегатную функцию и вывести результат выполнения
  агрегатной функции, где выполняется условие, отсортированный сначала по столбцу, по которому выполнялась агрегатная
  функция, а потом по столбцу, по которому - нет, при этом, результат вывода должен выполнять условие:

```sql
SELECT author, year, COUNT(author)
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author, year
HAVING COUNT(author) < 2;
```

## Пример 5:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8321-select-запросы)

- выбрать несколько столбцов таблицы и отсортировать выводимые значения по возрастанию по одному столбцу:

```sql
SELECT *
FROM book
ORDER BY id;
```

- выбрать несколько столбцов таблицы и отсортировать выводимые значения по возрастанию по нескольким столбцам:

```sql
SELECT *
FROM book
ORDER BY author, year;
```

- выбрать несколько столбцов таблицы, провести над ними операции, и отсортировать выводимые значения по убыванию по
  одному столбцу:

```sql
SELECT author, year, COUNT(author)
FROM book
WHERE reader_id IS NOT NULL
GROUP BY author, year
HAVING COUNT(author) < 2
ORDER BY year DESC;
```

## Пример 6:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/08.md/#8312-оператор-alter-table)

###### Примеры использования оператора _ALTER TABLE_:

```sql
CREATE TABLE users
(
    id   INTEGER,
    name VARCHAR(30)
);

INSERT INTO users(id, name)
VALUES (1, 'Иван'),
       (2, 'Пётр'),
       (3, 'Фёдор');
```

- ADD COLUMN:

```sql
ALTER TABLE users
    ADD COLUMN age INTEGER;

ALTER TABLE users
    ADD COLUMN salary NUMERIC(8, 2) DEFAULT 0.00;

ALTER TABLE users
    ADD COLUMN position VARCHAR(30) NOT NULL DEFAULT 'junior';

ALTER TABLE users
    ADD COLUMN passport_number VARCHAR(30) UNIQUE;
```

- ALTER COLUMN:

```sql
ALTER TABLE users
    ALTER COLUMN id
        TYPE BIGINT;

ALTER TABLE users
    ADD UNIQUE (id);

ALTER TABLE users
    DROP CONSTRAINT users_id_key;

ALTER TABLE users
    ADD PRIMARY KEY (id);

ALTER TABLE users
    ALTER COLUMN name
        SET NOT NULL;

ALTER TABLE users
    ALTER COLUMN age
        SET DATA TYPE SMALLINT;

ALTER TABLE users
    ALTER COLUMN age
        SET DEFAULT 1;

ALTER TABLE users
    ALTER COLUMN age
        DROP DEFAULT;
```

- ADD CONSTRAINT:

```sql
ALTER TABLE users
    ADD CONSTRAINT age_check CHECK (age > 0);
```

- DROP CONSTRAINT:

```sql
ALTER TABLE users
    DROP CONSTRAINT users_id_key;
```

- RENAME COLUMN:

```sql
ALTER TABLE users
    RENAME COLUMN name
        TO fio;
```

- DROP COLUMN:

```sql
ALTER TABLE users
    DROP COLUMN passport_number;
```

## Пример 7:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.6 Оператор JOIN**]](/conspect/08.md/#836-оператор-join)

- INNER JOIN:

```sql
SELECT employees.name, departments.department_name
FROM employees
         INNER JOIN departments ON employees.department_id = departments.department_id;
```

- LEFT JOIN:

```sql
SELECT employees.name, departments.department_name
FROM employees
         LEFT JOIN departments ON employees.department_id = departments.department_id;
```

- RIGHT JOIN:

```sql
SELECT employees.name, departments.department_name
FROM employees
         RIGHT JOIN departments ON employees.department_id = departments.department_id;
```

- FULL JOIN:

```sql
SELECT employees.name, departments.department_name
FROM employees
         FULL JOIN departments ON employees.department_id = departments.department_id;
```



