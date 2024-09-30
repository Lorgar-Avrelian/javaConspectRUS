## Пример 1:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.2 Агрегатные функции**]](/conspect/8.md/#8322-агрегатные-функции)

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

