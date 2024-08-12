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
`for ([блок объявления переменной]; [блок условия, которое должно выполняться]; [действие, которое должно выполняться при каждой итерации]) {[тело цикла]}`  
Например, `for (int i; i < 10; i++) {}`

# Перечень использованных определений

> [[_оглавление_]](#оглавление)

1. [**Scope (Область видимости)**](#112-типизация-переменных)
1. [**Вещественные типы (типы с плавающей точкой)**](#112-типизация-переменных)
1. [**Вложенный условный оператор**](#1212-оператор-if-else)
1. [**Индекс**]()
1. [**Инициализация переменной**](#11-переменные)
1. [**Инкремент**]()
1. [**Итерация**](#13-циклы)
1. [**Конкатенация**]()
1. [**Логический тип переменных**](#112-типизация-переменных)
1. [**Массив**]()
1. [**Область видимости (Scope)**](#112-типизация-переменных)
1. [**Объявление переменной**](#11-переменные)
1. [**Переменная**](#11-переменные)
1. [**Пул строк**]()
1. [**Символьные переменные**](#112-типизация-переменных)
1. [**Синтаксис языка программирования**](#11-переменные)
1. [**Строка (тип данных String)**]()
1. [**Тернарный оператор**](#1214-тернарный-оператор)
1. [**Цикл**](#13-циклы)
1. [**Целочисленные типы**](#112-типизация-переменных)
1. [**Элемент (ячейка) массива**]()
1. [**Ячейка (элемент) массива**]()