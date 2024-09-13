package javaCore;

import javaCore.models.Person;
import javaCore.models.VariableContainer;
import javaCore.models.Worker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
        inheritanceExample();
        tryCatchFinallyExample();
        genericExample();
        listExample();
    }

    private static void listExample() {
        List<Integer> arrayList = new ArrayList<>(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        List<Integer> linkedList = new LinkedList(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
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
