## Пример 1:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.2 Агрегатные функции**]](/conspect/07.md/#8322-агрегатные-функции)

- создание и заполнение значениями _SQL_-таблицы:

```sql
CREATE TABLE IF NOT EXISTS employees
(
    name      VARCHAR(50) NOT NULL,
    office_id INT,
    salary    INT,
    role      VARCHAR(50) NOT NULL
);
```

```sql
INSERT INTO employees
VALUES ('Ivan', 1, 500, 'SWE'),
       ('Misha', 2, 750, 'Manager'),
       ('Olya', 2, 600, 'QA'),
       ('Kolya', 1, 900, 'SWE'),
       ('Max', 2, NULL, 'Manager');
```

- функция _COUNT()_:
    * посчитать количество сотрудников для каждой роли:
      ```sql
      SELECT role, COUNT(*) number_of_empoyee
      FROM employees
      GROUP BY role;
      ```
    * посчитать количество уникальных офисов сотрудников:
      ```sql
      SELECT COUNT(DISTINCT office_id) AS unique_offices
      FROM employees;
      ```
- функция _SUM()_:
    * посчитать суммарную зарплату для всех сотрудников:
      ```sql
      SELECT SUM(salary) AS total_salary
      FROM employees;
      ```

- функции _MAX()_ и _MIN()_:
    * найти минимальную зарплату среди всех сотрудников:
      ```sql
      SELECT MIN(salary) AS min_salary
      FROM employees;
      ```
    * найти максимальную зарплату для каждого офиса, где больше двух сотрудников:
      ```sql
      SELECT office_id, MAX(salary) AS max_salary
      FROM employees
      GROUP BY office_id
      HAVING COUNT(*) > 2;
      ```
- функция _AVG()_:
    * рассчитать среднюю зарплату по всем сотрудникам:
      ```sql
      SELECT AVG(salary) AS avg_salary
      FROM employees;
      ```

## Пример 2:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.2 Агрегатные функции**]](/conspect/07.md/#8322-агрегатные-функции)

```sql
CREATE TABLE expense
(
    id       INTEGER DEFAULT nextval('table_name_id_seq'::regclass) NOT NULL,
    title    TEXT                                                   NOT NULL,
    date     DATE                                                   NOT NULL,
    category TEXT,
    amount   INTEGER                                                NOT NULL
);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (1, 'Проезд в автобусе', '2021-01-30', 'Транспорт', 50);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (2, 'Проезд в метро', '2021-01-30', 'Транспорт', 50);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (3, 'Покупка книги', '2021-01-31', 'Прочие покупки', 300);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (4, 'Покупка продуктов', '2021-01-31', 'Покупка продуктов', 450);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (5, 'Поход в кино', '2021-02-01', 'Развлечения', 400);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (6, 'Кофе', '2021-02-01', 'Еда вне дома', 150);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (7, 'Покупка продуктов', '2021-02-02', 'Покупка продуктов', 600);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (8, 'Поход в театр', '2021-02-14', 'Развлечения', 1000);

INSERT INTO public.expense (id, title, date, category, amount)
VALUES (9, 'Цветы', '2021-02-14', null, 500);

SELECT *
FROM expense;

SELECT *
FROM expense
ORDER BY amount DESC;

SELECT COUNT(*)
FROM expense;

SELECT COUNT(DISTINCT (category))
FROM expense;

SELECT category, COUNT(*)
FROM expense
GROUP BY category;

SELECT category, MIN(amount), MAX(amount), AVG(amount)
FROM expense
GROUP BY category;

SELECT category, SUM(amount)
FROM expense
GROUP BY category;

SELECT category, COUNT(*)
FROM expense
GROUP BY category
HAVING COUNT(*) > 1;

SELECT category, SUM(amount)
FROM expense
GROUP BY category
HAVING SUM(amount) > 1000;

SELECT *
FROM expense
LIMIT 4;

SELECT *
FROM expense
OFFSET 4;

SELECT *
FROM expense
LIMIT 2 OFFSET 4;
```