package lorgar.avrelian;

import lorgar.avrelian.comparators.ReverseStringComparator;
import lorgar.avrelian.models.Person;
import lorgar.avrelian.models.VariableContainer;
import lorgar.avrelian.models.Worker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
//        inheritanceExample();
//        tryCatchFinallyExample();
//        genericExample();
//        listExample();
//        iteratorExample();
//        comparatorExample();
//        dequeExample();
//        hashMapExample();
//        treeMapExample();
//        mapEntryExample();
//        streamExample();
//        streamMethodsExample();
        optionalExample();
    }

    private static void optionalExample() {
        List<String> companyNames = Arrays.asList(
                "paypal", "oracle", "", "microsoft", "", "apple");
        Optional<List<String>> listOptional = Optional.of(companyNames);
        int size = listOptional
                .map(List::size)
                .orElse(0);
        System.out.println(size);
    }

    private static void streamMethodsExample() {
        List<Integer> list = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 8, 7, 6, 5, 4, 3, 2, 1));
        System.out.println(list);
        Map<String, Integer> map = list.stream()
                .distinct()
                .collect(Collectors.toMap(String::valueOf, x -> x));
        List<String> stringList = list.stream()
                .distinct()
                .collect(Collectors.mapping(String::valueOf, Collectors.toList()));
        String str = list.stream()
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.joining(""));
        System.out.println(map);
        System.out.println(str);
        System.out.println(stringList);
    }

    private static void streamExample() {
        // Создадим список и заполним его буквами не по алфавиту
        List<String> list = new ArrayList<>();
        list.add("B");
        list.add("D");
        list.add("C");
        list.add("A");
        list.add("E");
        List<String> list2 = new ArrayList<>(list);
        // Отсортируем список с помощью метода пузырьковой сортировки.
        // Она использует два вложенных цикла и одно сравнение.
        for (int i = 0; i < list.size(); i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).compareTo(list.get(j)) > 0) {
                    String temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        System.out.println("Мы отсортировали это для тебя: " + list);
        // Создадим стрим для элементов
        ArrayList<String> sortedElements = list2.stream()
                .sorted()
                // Далее выведем данные в новую коллекцию с помощью коллектора
                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("Мы отсортировали это для тебя: " + sortedElements + " Исходный список: " + list2);
    }

    private static void mapEntryExample() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key 1", 1);
        map.put("key 2", 2);
        map.put("key 3", 3);
        map.put("key 4", 4);
        Set<Map.Entry<String, Integer>> entrySet = map.entrySet();
        System.out.println(entrySet);
        System.out.println(map);
        for (Map.Entry<String, Integer> entry : entrySet) {
            int hashCode = entry.hashCode();
            System.out.println(hashCode);
            if (entry.getValue() % 2 == 0) {
                entry.setValue(entry.getValue() + 1);
            }
        }
        System.out.println();
        System.out.println(entrySet);
        System.out.println(map);
    }

    private static void treeMapExample() {
        Person ivan = new Person("Ivan", "Ivanov", 25);
        Person petr = new Person("Petr", "Petrov", 25);
        Person sidor = new Person("Sidor", "Sidorov", 31);
        Map<Person, Integer> persons = new TreeMap<>();
        persons.put(sidor, sidor.getAge());
        persons.put(ivan, ivan.getAge());
        persons.put(petr, petr.getAge());
        System.out.println(persons);
    }

    private static void hashMapExample() {
        Map<String, Integer> map = new HashMap<>();
        map.put("key", 1);
        System.out.println(map);
        int i = map.get("key");
        System.out.println(i);
        boolean b = map.containsValue(2);
        System.out.println(b);
        b = map.containsKey("key");
        System.out.println(b);
        map.put("test", 0);
        System.out.println(map);
        b = map.remove("test", 1);
        System.out.println(b);
        i = map.remove("test");
        System.out.println(i);
        System.out.println(map);
        System.out.println(map.get("test"));
    }

    private static void dequeExample() {
        Deque<String> queue = new ArrayDeque<>(List.of("B", "A", "C", "D", "d", "b", "a", "c"));
        System.out.println(queue);
        queue.add("E");
        System.out.println(queue);
        queue.addFirst("e");
        System.out.println(queue);
        String str = queue.getFirst();
        System.out.println(str);
        System.out.println(queue);
    }

    private static void comparatorExample() {
        Set<String> tree = new TreeSet<>(new ReverseStringComparator());
        tree.add("B");
        tree.add("A");
        tree.add("C");
        tree.add("D");
        tree.add("d");
        tree.add("b");
        tree.add("a");
        tree.add("c");
        System.out.println(tree);
    }

    private static void iteratorExample() {
        List<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        Iterator<Integer> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() % 2 == 0) {
                iterator.remove();
            }
        }
        arrayList.sort(Comparator.naturalOrder());
        System.out.println(arrayList);
    }

    private static void listExample() {
        List<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        arrayList.add(11);
        System.out.println(arrayList);
        arrayList.add(5, 0);
        System.out.println(arrayList);
        int value = arrayList.get(5);
        System.out.println(value);
        boolean b = arrayList.contains(0);
        System.out.println(b);
        arrayList.remove(5);
        arrayList.remove(arrayList.size() - 1);
        System.out.println(arrayList);
        arrayList.clear();
        System.out.println(arrayList);
        List<Integer> linkedList = new LinkedList(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        b = false;
        b = arrayList.addAll(linkedList);
        System.out.println(arrayList);
        System.out.println(b);
        int i = arrayList.indexOf(10);
        System.out.println(i);
        i = arrayList.indexOf(11);
        System.out.println(i);
        arrayList.sort(Comparator.reverseOrder());
        System.out.println(arrayList);
        System.out.println(linkedList);
        Integer[] intArray = arrayList.toArray(new Integer[arrayList.size()]);
        System.out.println(Arrays.toString(intArray));
    }

    private static void genericExample() {
        List<VariableContainer> values = new ArrayList<>();
        VariableContainer<Byte> var1 = new VariableContainer((byte) 1);
        VariableContainer<Short> var2 = new VariableContainer((short) 1_000);
        VariableContainer<Integer> var3 = new VariableContainer(1_000_000);
        VariableContainer<Long> var4 = new VariableContainer(1_000_000_000_000L);
        VariableContainer<Float> var5 = new VariableContainer(2_000_000.0f);
        VariableContainer<Double> var6 = new VariableContainer(2_000_000_000_000.0);
        VariableContainer<Character> var7 = new VariableContainer('A');
        VariableContainer<Boolean> var8 = new VariableContainer(true);
        VariableContainer<String> var9 = new VariableContainer("String");
        values.add(var1);
        values.add(var2);
        values.add(var3);
        values.add(var4);
        values.add(var5);
        values.add(var6);
        values.add(var7);
        values.add(var8);
        values.add(var9);
        for (int i = 0; i < values.size(); i++) {
            switch (values.get(i).getValue().getClass().getSimpleName()) {
                case ("Byte"):
                    System.out.println("Byte value is " + values.get(i).getValue());
                    break;
                case ("Short"):
                    System.out.println("Short value is " + values.get(i).getValue());
                    break;
                case ("Integer"):
                    System.out.println("Integer value is " + values.get(i).getValue());
                    break;
                case ("Long"):
                    System.out.println("Long value is " + values.get(i).getValue());
                    break;
                case ("Float"):
                    System.out.println("Float value is " + values.get(i).getValue());
                    break;
                case ("Double"):
                    System.out.println("Double value is " + values.get(i).getValue());
                    break;
                case ("Character"):
                    System.out.println("Character value is " + values.get(i).getValue());
                    break;
                case ("Boolean"):
                    System.out.println("Boolean value is " + values.get(i).getValue());
                    break;
                case ("String"):
                    System.out.println("String value is " + values.get(i).getValue());
                    break;
                default:
                    System.out.println("Unknown value type " + values.get(i).getValue().getClass());
            }
        }
    }

    private static void tryCatchFinallyExample() {
        try {
            File file = new File("pictures/16.png");
            System.out.println(Files.size(file.toPath()));
        } catch (NoSuchElementException e) {
            System.out.println("File not found!");
        } catch (IOException | ClassCastException e) {
            System.out.println("Internal error!");
        } finally {
            System.out.println("The end");
        }
    }

    private static void inheritanceExample() {
        Person ivan = new Person("Ivan", "Ivanov", 21);
        Worker petr = new Worker("Petr", "Petrov", 22, "programmer");
        Worker fedor = new Worker("Fedor", "Fedorov", 23, "manager");
        Worker sidor = new Worker("Sidor", "Sidorov", 25, "manager");
        System.out.println(ivan);
        System.out.println(petr);
        System.out.println(fedor);
        System.out.println(sidor);
        sidor.setPosition("commercial director");
        System.out.println(sidor);
    }
}