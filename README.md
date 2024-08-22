# **КОНСПЕКТ ПО JAVA**

# ОГЛАВЛЕНИЕ

[**_1 Синтаксис языка_**](/conspect/1.md/#1-синтаксис-языка)

[**1.1 Переменные**](/conspect/1.md/#11-переменные)

[_1.1.1 Виды переменных_](/conspect/1.md/#111-виды-переменных)

[_1.1.2 Типизация переменных_](/conspect/1.md/#112-типизация-переменных)

[_1.1.3 Значения переменных по умолчанию_](/conspect/1.md/#113-значения-переменных-по-умолчанию)

[**1.2 Условные операторы**](/conspect/1.md/#12-условные-операторы)

[_1.2.1 Виды условных операторов в Java_](/conspect/1.md/#121-виды-условных-операторов-в-java)

[1.2.1.1 Оператор if](/conspect/1.md/#1211-оператор-if)  
[1.2.1.2 Оператор if-else](/conspect/1.md/#1212-оператор-if-else)  
[1.2.1.3 Оператор switch](/conspect/1.md/#1213-оператор-switch)  
[1.2.1.4 Тернарный оператор](/conspect/1.md/#1214-тернарный-оператор)

[**1.3 Циклы**](/conspect/1.md/#13-циклы)

[_1.3.1 Цикл for_](/conspect/1.md/#131-цикл-for)

[_1.3.2 Цикл while_](/conspect/1.md/#132-цикл-while)

[_1.3.3 Цикл do-while_](/conspect/1.md/#133-цикл-do-while)

[_1.3.4 Цикл for each_](/conspect/1.md/#134-цикл-for-each)

[**1.4 Массивы**](/conspect/1.md/#14-массивы)

[_1.4.1 Виды массивов_](/conspect/1.md/#141-виды-массивов)

[_1.4.2 Способы создания массивов_](/conspect/1.md/#142-способы-создания-массивов)  
[1.4.2.1 Объявление массива](/conspect/1.md/#1421-объявление-массива)  
[1.4.2.2 Инициализация массива](/conspect/1.md/#1422-инициализация-массива)

[_1.4.3 Получение значения элемента массива_](/conspect/1.md/#143-получение-значения-элемента-массива)  
[_1.4.4 Свойство массива length_](/conspect/1.md/#144-свойство-массива-length)

[_1.4.5 Класс Arrays_](/conspect/1.md/#145-класс-arrays)  
[1.4.5.1 toString()](/conspect/1.md/#1451-tostring)  
[1.4.5.2 fill()](/conspect/1.md/#1452-fill)  
[1.4.5.3 equals()](/conspect/1.md/#1453-equals)  
[1.4.5.4 copyOf()](/conspect/1.md/#1454-copyof)  
[1.4.5.5 sort()](/conspect/1.md/#1455-sort)

[**1.5 Строки (Класс String)**](/conspect/1.md/#15-строки-класс-string)

[_1.5.1 Пул строк (String pool)_](/conspect/1.md/#151-пул-строк-string-pool)

[_1.5.2 Конкатенация (сложение) строк_](/conspect/1.md/#152-конкатенация-сложение-строк)

[_1.5.3 Методы для работы со строками_](/conspect/1.md/#153-методы-для-работы-со-строками)  
[1.5.3.1 equals()](/conspect/1.md/#1531-equals)  
[1.5.3.2 equalsIgnoreCase()](/conspect/1.md/#1532-equalsignorecase)  
[1.5.3.3 length()](/conspect/1.md/#1533-length)  
[1.5.3.4 isEmpty()](/conspect/1.md/#1534-isempty)  
[1.5.3.5 isBlank()](/conspect/1.md/#1535-isblank)  
[1.5.3.6 contains()](/conspect/1.md/#1536-contains)  
[1.5.3.7 endsWith()](/conspect/1.md/#1537-endswith)  
[1.5.3.8 startsWith()](/conspect/1.md/#1538-startswith)  
[1.5.3.9 charAt()](/conspect/1.md/#1539-charat)  
[1.5.3.10 substring()](/conspect/1.md/#15310-substring)  
[1.5.3.11 toUpperCase()](/conspect/1.md/#15311-touppercase)  
[1.5.3.12 toLowerCase()](/conspect/1.md/#15312-tolowercase)  
[1.5.3.13 trim()](/conspect/1.md/#15313-trim)  
[1.5.3.14 split()](/conspect/1.md/#15314-split)  
[1.5.3.15 toCharArray()](/conspect/1.md/#15315-tochararray)  
[1.5.3.16 replace()](/conspect/1.md/#15316-replace)  
[1.5.3.17 repeat()](/conspect/1.md/#15317-repeat)

[_1.5.4 Формирование строк из массивов_](/conspect/1.md/#154-формирование-строк-из-массивов)  
[1.5.4.1 Символьный массив](/conspect/1.md/#1541-символьный-массив)  
[1.5.4.2 Массив байтов](/conspect/1.md/#1542-массив-байтов)

[**1.6 Изменяемые строки (StringBuilder)**](/conspect/1.md/#16-изменяемые-строки-stringbuilder)

[_1.6.1 append()_](/conspect/1.md/#161-append)

[_1.6.2 toString()_](/conspect/1.md/#162-tostring)

[**1.7 Класс Scanner**](/conspect/1.md/#17-класс-scanner)

[_1.7.1 nextLine()_](/conspect/1.md/#171-nextline)

[_1.7.2 nextInt()_](/conspect/1.md/#172-nextint)

[_1.7.3 hasNext()_](/conspect/1.md/#173-hasnext)

[_1.7.4 hasNextInt()_](/conspect/1.md/#174-hasnextint)

[_1.7.5 hasNextLine()_](/conspect/1.md/#175-hasnextline)

[_1.7.6 hasNextByte()_](/conspect/1.md/#176-hasnextbyte)

[_1.7.7 hasNextShort()_](/conspect/1.md/#177-hasnextshort)

[_1.7.8 hasNextLong()_](/conspect/1.md/#178-hasnextlong)

[_1.7.9 hasNextFloat()_](/conspect/1.md/#179-hasnextfloat)

[_1.7.10 hasNextDouble()_](/conspect/1.md/#1710-hasnextdouble)

[_1.7.11 useDelimiter()_](/conspect/1.md/#1711-usedelimiter)

[**Перечень использованных определений**](/conspect/definitions.md/#перечень-использованных-определений)