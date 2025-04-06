## Пример 1:

> [[_оглавление_]](../README.md/#28-stream-api)

> [[**2.8.1 Лямбда-выражения**]](/conspect/02_08.md/#281-лямбда-выражения)

```java
private static void runnableExample() {
    Runnable test = () -> System.out.println("Hello World");
    // Эквивалентный анонимный класс
    Runnable test2 = new Runnable {
        @Override
        public void run() {
            System.out.println("Hello World");
        }
    }
    test.run();
    test2.run();
}
```

## Пример 2:

```java
private static void binaryOperatorExample() {
    BinaryOperator<Integer> sum = (x, y) -> x + y;
    // Эквивалентный анонимный класс
    BinaryOperator<Integer> sum2 = new BinaryOperator<Integer>() {
        @Override
        public Integer apply(Integer x, Integer y) {
            return x + y;
        }
    };
    // Вариант применения, который распечатает на экран число 3
    System.out.println(sum.apply(1, 2));
    // Применение при реализации анонимным классом не отличается
    System.out.println(sum2.apply(1, 2));
}
```