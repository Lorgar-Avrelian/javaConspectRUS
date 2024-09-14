package javaCore;

import javaCore.comparators.ReverseStringComparator;
import javaCore.models.Person;
import javaCore.models.VariableContainer;
import javaCore.models.Worker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

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
        treeMapExample();
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
