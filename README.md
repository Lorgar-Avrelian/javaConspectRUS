# **КОНСПЕКТ ПО JAVA**

# ОГЛАВЛЕНИЕ

[**_1 Синтаксис языка_**](#1-синтаксис-языка)

[**1.1 Переменные**](#11-переменные)  
[_1.1.1 Виды переменных_](#111-виды-переменных)  
[_1.1.2 Типизация переменных_](#112-типизация-переменных)  
[_1.1.3 Значения переменных по умолчанию_](#113-значения-переменных-по-умолчанию)

[**1.2 Условные операторы**](#12-условные-операторы)  
[_1.2.1 Виды условных операторов в Java_](#121-виды-условных-операторов-в-java)  
[1.2.1.1 Оператор if](#1211-оператор-if)  
[1.2.1.2 Оператор if-else](#1212-оператор-if-else)  
[1.2.1.3 Оператор switch](#1213-оператор-switch)  
[1.2.1.4 Тернарный оператор](#1214-тернарный-оператор)

[**1.3 Циклы**](#13-циклы)  
[_1.3.1 Цикл for_](#131-цикл-for)  
[_1.3.2 Цикл while_](#132-цикл-while)  
[_1.3.3 Цикл do-while_](#133-цикл-do-while)  
[_1.3.4 Цикл for each_](#134-цикл-for-each)

[**1.4 Массивы**](#14-массивы)  
[_1.4.1 Виды массивов_](#141-виды-массивов)  
[_1.4.2 Способы создания массивов_](#142-способы-создания-массивов)  
[1.4.2.1 Объявление массива](#1421-объявление-массива)  
[1.4.2.2 Инициализация массива](#1422-инициализация-массива)  
[_1.4.3 Получение значения элемента массива_](#143-получение-значения-элемента-массива)  
[_1.4.4 Свойство массива length_](#144-свойство-массива-length)  
[_1.4.5 Класс Arrays_](#145-класс-arrays)  
[1.4.5.1 toString()](#1451-tostring)  
[1.4.5.2 fill()](#1452-fill)  
[1.4.5.3 equals()](#1453-equals)  
[1.4.5.4 copyOf()](#1454-copyof)  
[1.4.5.5 sort()](#1455-sort)

[**1.5 Строки (Класс String)**](#15-строки-класс-string)  
[_1.5.1 Пул строк (String pool)_](#151-пул-строк-string-pool)  
[_1.5.2 Конкатенация (сложение) строк_](#152-конкатенация-сложение-строк)  
[_1.5.3 Методы для работы со строками_](#153-методы-для-работы-со-строками)  
[1.5.3.1 equals()](#1531-equals)  
[1.5.3.2 equalsIgnoreCase()](#1532-equalsignorecase)  
[1.5.3.3 length()](#1533-length)  
[1.5.3.4 isEmpty()](#1534-isempty)  
[1.5.3.5 isBlank()](#1535-isblank)  
[1.5.3.6 contains()](#1536-contains)  
[1.5.3.7 endsWith()](#1537-endswith)  
[1.5.3.8 startsWith()](#1538-startswith)  
[1.5.3.9 charAt()](#1539-charat)  
[1.5.3.10 substring()](#15310-substring)  
[1.5.3.11 toUpperCase()](#15311-touppercase)  
[1.5.3.12 toLowerCase()](#15312-tolowercase)  
[1.5.3.13 trim()](#15313-trim)  
[1.5.3.14 split()](#15314-split)  
[1.5.3.15 toCharArray()](#15315-tochararray)  
[1.5.3.16 replace()](#15316-replace)  
[1.5.3.17 repeat()](#15317-repeat)  
[_1.5.4 Формирование строк из массивов_](#154-формирование-строк-из-массивов)  
[1.5.4.1 Символьный массив](#1541-символьный-массив)  
[1.5.4.2 Массив байтов](#1542-массив-байтов)

[**1.6 Изменяемые строки (StringBuilder)**](#16-изменяемые-строки-stringbuilder)

[**Перечень использованных определений**](#перечень-использованных-определений)

# 1 Синтаксис языка

> [[_оглавление_]](#оглавление)

## 1.1 Переменные

> [[_оглавление_]](#оглавление)

[**Переменная** ](#перечень-использованных-определений)– это ячейка в памяти компьютера, которой можно присвоить имя и в
которой можно хранить данные.  
[**Объявление переменной**](#перечень-использованных-определений) – это создание переменной (выделение ячейки памяти,
присвоение ей имени).  
[**Инициализация переменной**](#перечень-использованных-определений) – это присваивание какого-то значения переменной.
Выделение памяти для хранения переменных происходит в момент их инициализации.  
[**Синтаксис языка программирования**](#перечень-использованных-определений) – это набор правил и конструкций, из
которых строится язык.

### 1.1.1 Виды переменных:

> [[_оглавление_]](#оглавление)

* примитивные;
* не примитивные (ссылочные).

### 1.1.2 Типизация переменных

> [[_оглавление_]](#оглавление)

|           **Тип данных**            |         **Обозначение**         |                                                             **Диапазон значений**                                                             |             **Объём памяти**              |
|:-----------------------------------:|:-------------------------------:|:---------------------------------------------------------------------------------------------------------------------------------------------:|:-----------------------------------------:|
|           _Целочисленные_           | byte<br/>short<br/>int<br/>long | от -128 до 127<br/>от -32 768 до 32 768<br/>от -2 147 483 648 до 2 147 483 648<br/>от -9 223 372 036 854 775 808 до 9 223 372 036 854 775 807 | 1 байт<br/>2 байта<br/>4 байта<br/>8 байт |
| _Вещественные (с плавающей точкой)_ |        float<br/>double         |                                              от -3.4E+38 до 3.4E+38<br/>от -1.7E+308 до 1.7E+308                                              |            4 байта<br/>8 байт             |
|            _Символьные_             |              char               |                                                                от 0 до 65 536                                                                 |                  2 байта                  |
|            _Логические_             |             boolean             |                                                                  true/false                                                                   |                   1 бит                   |

[**Целочисленные типы**](#перечень-использованных-определений) – это типы, которые содержат в себе целое число (без
дробной части).  
[**Вещественные типы (типы с плавающей точкой)**](#перечень-использованных-определений) – это типы, которые содержат в
себе не только целую, но еще и дробную часть.  
[**Логический тип переменных**](#перечень-использованных-определений) – это тип, в котором хранится информация в формате
true/false (т. е. истина/ложь).  
[**Символьные переменные**](#перечень-использованных-определений) – это тип, который позволяет хранить значения
символов, а также буквы или цифры. Значения символьных переменных пишутся в одинарных кавычках (' ').
> [**Scope (Область видимости)**](#перечень-использованных-определений) – это участок кода, сгруппированный в фигурные
> скобки ({}). Все переменные, объявленные внутри блока, не видны за его пределами.

### 1.1.3 Значения переменных по умолчанию

> [[_оглавление_]](#оглавление)

|        **Тип переменной**        | **Значение по умолчанию** |
|:--------------------------------:|:-------------------------:|
|               byte               |             0             |
|              short               |             0             |
|               int                |             0             |
|               long               |            0L             |
|              float               |           0.0f            |
|              double              |           0.0d            |
|               char               |             0             |
| String (или любой другой объект) |           null            |
|             boolean              |           false           |

В случае, если переменная определённого типа объявлена, но не проинициализирована, то при обращении к ней будет получено
значение «по-умолчанию» из таблицы выше.

## 1.2 Условные операторы

> [[_оглавление_]](#оглавление)

| Оператор |      Функция оператора      |   Пример   |
|:--------:|:---------------------------:|:----------:|
|    <     |      Операция "Меньше"      |  `a < b`   |
|    <=    | Операция "Меньше или равно" |  `a <= b`  |
|    >     |      Операция "Больше"      |  `a > b`   |
|    >=    | Операция "Больше или равно" |  `a >= b`  |
|    ==    |      Операция "Равно"       |  `a == b`  |
|    !=    |     Операция "Не равно"     |  `a != b`  |
|    &&    |        Операция "И"         |  `a && b`  |
|   \|\|   |       Операция "Или"        | `a \|\| b` |

### 1.2.1 Виды условных операторов в Java:

#### 1.2.1.1 Оператор if

> [[_оглавление_]](#оглавление)

**if** – это условный оператор или оператор ветвления. Он позволяет запускать код только в том случае, когда условия,
помещенные в скобки, являются истиной, т. е. соблюдаются.

#### 1.2.1.2 Оператор if-else

> [[_оглавление_]](#оглавление)

**if-else** – это условный оператор или оператор ветвления. Он позволяет запускать код только в том случае, когда
условия, помещенные в скобки, являются истиной, т. е. соблюдаются, а если они не соблюдаются, то выполняется тот блок
кода, который помещён в блок else.  
Конструкция else-if используется при вложенности.  
[**Вложенный условный оператор**](#перечень-использованных-определений) – это условный оператор, который находится
внутри другого условного оператора.

#### 1.2.1.3 Оператор switch

> [[_оглавление_]](#оглавление)

**switch** – это условный оператор, который применяется при выборе между несколькими решениями, когда вариантов много, и
прописывать для каждого if-else будет долго.  
В операторе switch необходимо не забывать использовать ключевое слово break.
> [**break**](#1213-оператор-switch) – ключевое слово, которое применяется для прерывания выполнения блока кода.

Конструкция оператора switch выглядит следующим образом:

```java
public static void switchMethod(int variable) {
    switch (variable) {
        case 1:
            // код выполнится, если variable == 1
            action1();
            break;
        case 2:
            // код выполнится, если variable == 2
            action2();
            break;
        case 3:
            // код выполнится, если variable == 3
            action3();
            break;
        default:
            // код выполнится, если ни одно значение в case не было равно variable
            actionByDefault();
    }
}
```

Пример использования:

```java
private static void dayOfWeek(int day) {
    switch (day) {
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
            System.out.println("Работаем!");
            break;
        case 6:
        case 7:
            System.out.println("Отдыхаем!");
            break;
        default:
            System.out.println("Такого дня не существует!");
    }
}
```

По сравнению с конструкциями if-else, которые могут быть громоздкими, оператор switch представляется удобным решением.
Однако он позволяет сравнивать переключатель только с конкретными значениями. Задать условие с помощью операторов
сравнения для switch нельзя.

#### 1.2.1.4 Тернарный оператор

> [[_оглавление_]](#оглавление)

[**Тернарный оператор**](#перечень-использованных-определений) – это условный оператор, который содержит три
выражения.  
Тернарный оператор используется вместо оператора if-else. В коде это выглядит так:

```java
private static void ternarOperator(int a, int b) {
    int result = a > b ? 1 : 2;
    switchMethod(result);
}
```

Если первое выражение верно (true), то возвращается значение 1, иначе (false) - 2.

## 1.3 Циклы

> [[_оглавление_]](#оглавление)

[**Цикл**](#перечень-использованных-определений) – это конструкция кода, которая повторяет одно и то же действие
несколько (столько, сколько нам потребуется) раз.  
[**Итерация**](#перечень-использованных-определений) – это один повтор какого-то действия (одно прохождение цикла).

### 1.3.1 Цикл for

> [[_оглавление_]](#оглавление)

Конструкция цикла `for` выглядит следующим образом:

`for ([блок объявления переменной]; [блок условия]; [действие при каждой итерации]) {[тело цикла]}`

Например:

```java
private static void forExample() {
    for (int i = 0; i < 10; i++) {
        action1();
    }
}
```

[**Инкремент**](#перечень-использованных-определений) – это операция, которая увеличивает значение переменной.  
Блоки цикла `for` не являются строго стандартизированными и могут отсутствовать.  
В первом блоке возможно объявить большое количество переменных внутри себя, но так делать не рекомендуется.  
Второй блок тоже может отсутствовать. В таком случае мы получим вечный цикл, который каждый шаг будет вызывать третий
блок.  
Третий блок, как и два других, тоже не является обязательным и может быть пропущен. В таком случае есть вероятность
запустить вечный цикл, если инициализировать переменную в первом блоке, во втором проверить условие ее размера, а в
третьем не изменять ее.  
Цикл `for` позволяет создать себя с полностью пустыми блоками. В этой ситуации будет создан бесконечный
цикл: `for (;;) {}`

### 1.3.2 Цикл while

> [[_оглавление_]](#оглавление)

Конструкция цикла `while` выглядит следующим образом: `while ([условие]) {[тело цикла]}`.

Например:

```java
private static void whileExample() {
    int i = 10;
    while (i > 0) {
        action1();
        i--;
    }
}
```

Цикл `while` может быть запущен в бесконечном режиме. Но при этом зачастую используют оператор `break`, который
прерывает цикл вне зависимости от условия.  
Также с циклом `while` часто применяют ключевое слово `continue`.

> [**continue**](#132-цикл-while) – ключевое слово, которое применяется для пропуска выполнения блока кода в цикле.

### 1.3.3 Цикл do-while

> [[_оглавление_]](#оглавление)

Конструкция цикла `do-while` выглядит следующим образом: `do {[действие]} while ([условие])`.

Например:

```java
private static void doWhileExample() {
    int i = 0;
    do {
        action1();
        i++;
    } while (i < 10);
}
```

По принципу работы цикл `do-while` похож на `while`. Отличие в том, что `while` может не выполниться ни разу (если
условие изначально равняется `false`), а цикл `do-while` выполнится минимум 1 раз.
Цикл `while` сначала проверяет условие в скобках и затем выполняет блок, а цикл `do-while` сначала запускает блок, а
потом проверяет условие.

### 1.3.4 Цикл for each

> [[_оглавление_]](#оглавление)

Цикл `for each` позволяет проходить по всем элементам массива без необходимости работать с индексами ячеек и применяется
при работе с массивами (класс `Arrays`), коллекциями (класс `Collection`) и мапами (класс `Map`).  
Конструкция цикла `for each` выглядит следующим образом:  
`for (тип_переменной имя : массив) {[действия с переменной, которая создана в первом блоке]}`.

Например:

```java
private static void forEachExample() {
    List<Integer> testList = new ArrayList<>(List.of(1, 2, 3));
    for (Integer i : testList) {
        System.out.println("i = " + i);
    }
}
```

## 1.4 Массивы

> [[_оглавление_]](#оглавление)

[**Массив**](#перечень-использованных-определений) – это структура данных, которая позволяет хранить несколько значений
одного типа; это непрерывная область памяти, в которой хранятся однотипные данные.  
Массив является объектом и хранит в себе данные того типа, которым он инициализирован.  
Размер массива изменять нельзя.  
[**Индекс**](#перечень-использованных-определений) – это порядковый номер элемента в массиве.  
Первый элемент массива находится в ячейке под номером 0.  
[**Ячейка (элемент) массива**](#перечень-использованных-определений) – это место в памяти, где хранится значение.  
Элемент массива – это также само значение, которое хранится в ячейке массива.  
Массив при создании формирует не просто ячейки типов, но и заполняет
их [стандартными значениями](#113-значения-переменных-по-умолчанию).

### 1.4.1 Виды массивов

- одномерные;
- многомерные.

> [[_оглавление_]](#оглавление)

### 1.4.2 Способы создания массивов

#### 1.4.2.1 Объявление массива

> [[_оглавление_]](#оглавление)

Признаком того, что данная переменная является массивом, являются квадратные скобки (`[]`), которые в момент объявления:

* следуют, как правило, после типа массива: `dataType[] arrayName;`
* или (что не рекомендуется делать, но также работает) могут размещаться после имени переменной: `dataType arrayName[];`

#### 1.4.2.2 Инициализация массива

> [[_оглавление_]](#оглавление)

Массив может быть проинициализирован как в момент, так и после его объявления.  
Инициализация массива может производиться:

- путём непосредственного присвоения значений;
- с использованием ключевого слова new и указания размера массива.

Например:

```java
private static void arraysExample() {
    int[] array1 = {1, 2, 3};
    int[][] array2 = {
            {1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10}
    };
    int[] array3 = new int[]{1, 2, 3};
    int[][] array4 = new int[][]{
            array3,
            {4, 5, 6, 7},
            {8, 9, 10}
    };
    int array5[] = array1;
    int array6[][] = array2;
    System.out.println(Arrays.toString(array5));
    System.out.println(Arrays.toString(array6[0]));
    System.out.println(Arrays.toString(array6[1]));
    System.out.println(Arrays.toString(array6[2]));
    System.out.println(Arrays.equals(array4, array2));
    System.out.println(Arrays.equals(array3, array1));
}
```

### 1.4.3 Получение значения элемента массива

> [[_оглавление_]](#оглавление)

Получение значения элемента массива осуществляется по его индексу.

```java
int value1 = array1[1];
int value2 = array2[2][0];
```

### 1.4.4 Свойство массива length

> [[_оглавление_]](#оглавление)

Свойство массива `length` возвращает длину массива, то есть количество элементов.

```java
boolean[] array;
int i = array.length;
```

Длина массива всегда имеет целое значение, поэтому свойство `length` всегда возвращает значение типа `int`.

### 1.4.5 Класс Arrays

> [[_оглавление_]](#оглавление)

В Java для работы с массивами существует utility-класс `Arrays`, который содержит соответствующие методы.
> Методы класса `Arrays` рассчитаны для работы с одномерными массивами. Поэтому для использования этих методов в
> многомерных массивах необходимо разбивать последний на одномерные составляющие и использовать их уже по отношению к
> этим
> составляющим.

#### 1.4.5.1 toString()

> [[_оглавление_]](#оглавление)

Наиболее часто используемый метод класса `Arrays` метод `toString()`, используемый для распечатывания элементов массива,
когда формат вывода элементов не принципиален, и в который необходимо передавать распечатываемый массив в виде
аргумента.

```java
private static void arraysExample() {
    int[] array1 = {1, 2, 3};
    int[][] array2 = {
            {1, 2, 3},
            {4, 5, 6, 7},
            {8, 9, 10}
    };
    System.out.println(Arrays.toString(array1));
    System.out.println(Arrays.toString(array2[0]));
    System.out.println(Arrays.toString(array2[1]));
    System.out.println(Arrays.toString(array2[2]));
}
```

#### 1.4.5.2 fill()

> [[_оглавление_]](#оглавление)

Метод `fill()` класса `Arrays` заполняет массив одинаковыми значениями.  
Синтаксис метода выглядит следующим образом: `Arrays.fill([имя массива], [значение]);`.
Первым параметром передаётся имя массива, а вторым – значение, которым его надо заполнить.

#### 1.4.5.3 equals()

> [[_оглавление_]](#оглавление)

Метод `equals()` класса `Arrays` позволяет сравнивать два массива с учётом значений их элементов.  
Синтаксис метода выглядит следующим образом: `Arrays.equals([имя массива 1], [имя массива 2]);`.  
Результат сравнения будет значением типа `boolean` и может быть присвоен переменной типа `boolean`.

#### 1.4.5.4 copyOf()

> [[_оглавление_]](#оглавление)

Метод `copyOf()` класса `Arrays` помогает создать копию уже существующего массива, при этом сначала указывается тип
нового массива и его имя, затем в скобках метода имя имеющегося массива и необходимая длина нового массива.  
Синтаксис метода выглядит следующим образом:  
`тип[] имя2 = Arrays.copyOf(имя1, [длина массива 2]);`  
Если длина нового массива меньше длины существующего, то лишние значения игнорируются.  
Если длина нового массива больше длины старого, ячейки заполняются значениями по умолчанию.

#### 1.4.5.5 sort()

> [[_оглавление_]](#оглавление)

Метод `sort()` класса `Array`s сортирует массив по возрастанию.  
Синтаксис метода выглядит следующим образом: `Arrays.sort([имя массива]);`.

## 1.5 Строки (Класс String)

> [[_оглавление_]](#оглавление)

[**Строка** (тип данных **String**)](#перечень-использованных-определений) – это последовательность символов
типа `char`.  
Значения строк пишутся в двойных кавычках (" ").  
Строки, как и массивы, являются объектами. Если им не присвоить значение (не инициализировать), они будут содержать в
себе `null`.  
Строка имеет внутренние свойства. Главное из них – массив типа `byte`, где и хранятся все символы конкретной строки.  
Строка – неизменяемый объект, после создания изменить её невозможно. Все создаваемые в коде строки попадают в пул
строк.  
Неизменяемость строк обусловлена способом их хранения в памяти, а также соображениями безопасности.

### 1.5.1 Пул строк (String pool)

> [[_оглавление_]](#оглавление)

[**Пул строк**](#перечень-использованных-определений) – это набор строк, который хранится в памяти Java heap; это один
из внутренних механизмов Java, благодаря которому в памяти сохраняется только один экземпляр строки идентичного
содержания.  
Когда мы используем двойные кавычки для создания строки, сначала ищется строка в пуле с таким же значением, если
находится, то просто возвращается ссылка, иначе создается новая строка в пуле, а затем возвращается ссылка.  
Когда же строка создаётся с помощью ключевого слова `new`, мы принуждаем класс `String` создать новую строку в пуле.

### 1.5.2 Конкатенация (сложение) строк

> [[_оглавление_]](#оглавление)

[**Конкатенация**](#перечень-использованных-определений) – это операция склеивания объектов линейной структуры, обычно
строк.

### 1.5.3 Методы для работы со строками

> [[_оглавление_]](#оглавление)

#### 1.5.3.1 equals()

> [[_оглавление_]](#оглавление)

Для сравнения строк используют метод `equals()`. Этот метод в качестве параметра принимает другую строку и возвращает
`true`/`false`.  
Синтаксис метода выглядит следующим образом: `s2.equals(s1);`.

#### 1.5.3.2 equalsIgnoreCase()

> [[_оглавление_]](#оглавление)

Для сравнения строк без учёта регистра используют метод `equalsIgnoreCase()`.  
Синтаксис метода выглядит следующим образом: `s2.equalsIgnoreCase(s1);`.

#### 1.5.3.3 length()

> [[_оглавление_]](#оглавление)

Для определения длины строки используют метод `length()`, аналогичный по своему назначению со свойством `length`
массивов. Не смотря на то, что строки являются массивами символов, строки свойства `length` не имеют.  
Синтаксис метода выглядит следующим образом: `int stringLength = s.length()`.

#### 1.5.3.4 isEmpty()

> [[_оглавление_]](#оглавление)

Для определения является ли строка пустой, то есть не содержащей ни одного символа, используют метод `isEmpty()`.  
Синтаксис метода выглядит следующим образом: `boolean sIsEmpty = s.isEmpty()`.

#### 1.5.3.5 isBlank()

> [[_оглавление_]](#оглавление)

Для определения является ли строка пустой, то есть не содержащей ни одного символа, или содержащей только пробелы
используют метод `isBlank()`.  
Синтаксис метода выглядит следующим образом: `boolean sIsBlank = s.isBlank()`.  
Если в строке будет только пробел (" "), то метод `isEmpty()` вернет `false`, метод `isBlank()` – `true`.

#### 1.5.3.6 contains()

> [[_оглавление_]](#оглавление)

Метод `contains()` проверяет наличие в строке последовательности символов.  
Синтаксис метода выглядит следующим образом: `boolean sContains = s.contains("последовательность символов")`.

#### 1.5.3.7 endsWith()

> [[_оглавление_]](#оглавление)

Метод `endsWith()` проверяет наличие в окончании строки последовательности символов.  
Синтаксис метода выглядит следующим образом: `boolean sEndsWith = s.endsWith("последовательность символов")`.

#### 1.5.3.8 startsWith()

> [[_оглавление_]](#оглавление)

Метод `startsWith()` проверяет наличие в начале строки последовательности символов.  
Синтаксис метода выглядит следующим образом: `boolean sStartsWith = s.startsWith("последовательность символов")`.

#### 1.5.3.9 charAt()

> [[_оглавление_]](#оглавление)

Метод `charAt()` возвращает символ строки с указанным индексом (позицией).  
Синтаксис метода выглядит следующим образом: `char c = s.charAt(2)`.

#### 1.5.3.10 substring()

> [[_оглавление_]](#оглавление)

Метод `substring()` извлекает символы, начиная с первого параметра в ячейке включительно, заканчивая вторым параметром
не включительно.  
Синтаксис метода выглядит следующим образом: `String s2 = s1.substring(2, 4)`.

#### 1.5.3.11 toUpperCase()

> [[_оглавление_]](#оглавление)

Метод `toUpperCase()` возвращает значение строки, преобразованное в верхний регистр.  
Синтаксис метода выглядит следующим образом: `String s2 = s1.toUpperCase()`.

#### 1.5.3.12 toLowerCase()

> [[_оглавление_]](#оглавление)

Метод `toLowerCase()` возвращает значение строки, преобразованное в нижний регистр.  
Синтаксис метода выглядит следующим образом: `String s2 = s1.toLowerCase()`.

#### 1.5.3.13 trim()

> [[_оглавление_]](#оглавление)

Метод `trim()` удаляет все символы пробелов (" ") с начала и конца строки.  
Синтаксис метода выглядит следующим образом: `String s2 = s1.trim()`.

#### 1.5.3.14 split()

> [[_оглавление_]](#оглавление)

Метод `split()` создает из строки массив, разбив ее на части. Разделитель, по которому будет произведена разбивка,
указывается в скобках.  
Синтаксис метода выглядит следующим образом: `String[] strings = s1.split(" ")`.

#### 1.5.3.15 toCharArray()

> [[_оглавление_]](#оглавление)

Метод `toCharArray()` преобразует строку в массив символов.  
Синтаксис метода выглядит следующим образом: `char[] c = s.toCharArray()`.

#### 1.5.3.16 replace()

> [[_оглавление_]](#оглавление)

Метод `replace()` возвращает новую строку с символами, замененными на указанные.  
Синтаксис метода выглядит следующим образом: `String s2 = s1.replace(" ", "")`.

#### 1.5.3.17 repeat()

> [[_оглавление_]](#оглавление)

Метод `repeat()` возвращает новую строку, которая содержит указанное количество соединенных вместе копий строки, на
которой был вызван метод.  
Синтаксис метода выглядит следующим образом:

```java
String s1 = "#";
String s2 = s1.repeat(10);
```

Метод `repeat()` не изменяет строку, на которую был вызван, а возвращает новую: `s2` получит строку `s1`, которая будет
повторена 10 раз (значение из скобок).

### 1.5.4 Формирование строк из массивов

> [[_оглавление_]](#оглавление)

#### 1.5.4.1 Символьный массив

> [[_оглавление_]](#оглавление)

Бывают ситуации, когда у нас есть символьный массив, а мы хотим превратить его в строку.  
В такой ситуации нам поможет другой способ создания строки:

```java
char[] symbols = {'a', 'b', 'c'};
String string = new String(symbols);
```

Этот способ позволит нам получить строку на основе тех символов, что лежат в массиве.

#### 1.5.4.2 Массив байтов

> [[_оглавление_]](#оглавление)

Из массива байтов тоже можно собрать строку.  
Веб-приложения, помимо текста, могут передавать данные еще и в байтах.
Это быстрее, безопаснее и зачастую выигрывает в производительности.  
Предположим, что мы получили от соседнего приложения текстовые данные в байтовом формате и собрали их в массив.

```java
private static void textFromBytes() {
    byte[] textInBytes = {33, 33, 33};
    String stringFromBytes = new String(textInBytes);
    System.out.println(stringFromBytes);
}
```

В консоль выведется строка `!!!`, так как значение 33 соответствует символу восклицательного знака в таблице символов
ASCII.

## 1.6 Изменяемые строки (StringBuilder)

> [[_оглавление_]](#оглавление)

# Перечень использованных определений

> [[_оглавление_]](#оглавление)

1. [**Scope (Область видимости)**](#112-типизация-переменных)
1. [**Вещественные типы (типы с плавающей точкой)**](#112-типизация-переменных)
1. [**Вложенный условный оператор**](#1212-оператор-if-else)
1. [**Индекс**](#14-массивы)
1. [**Инициализация переменной**](#11-переменные)
1. [**Инкремент**](#131-цикл-for)
1. [**Итерация**](#13-циклы)
1. [**Конкатенация**](#152-конкатенация-сложение-строк)
1. [**Логический тип переменных**](#112-типизация-переменных)
1. [**Массив**](#14-массивы)
1. [**Область видимости (Scope)**](#112-типизация-переменных)
1. [**Объявление переменной**](#11-переменные)
1. [**Переменная**](#11-переменные)
1. [**Пул строк**](#151-пул-строк-string-pool)
1. [**Символьные переменные**](#112-типизация-переменных)
1. [**Синтаксис языка программирования**](#11-переменные)
1. [**Строка (тип данных String)**](#15-строки-класс-string)
1. [**Тернарный оператор**](#1214-тернарный-оператор)
1. [**Цикл**](#13-циклы)
1. [**Целочисленные типы**](#112-типизация-переменных)
1. [**Элемент (ячейка) массива**](#14-массивы)
1. [**Ячейка (элемент) массива**](#14-массивы)