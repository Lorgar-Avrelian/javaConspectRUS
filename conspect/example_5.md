## Пример 1:

> [[_оглавление_]](../README.md/#83-sql)

> [[**8.3.2.1 SELECT-запросы**]](/conspect/8.md/#8321-select-запросы)

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

