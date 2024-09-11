package javaCore;

import javaCore.models.Person;
import javaCore.models.Worker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {
//        inheritanceExample();
        tryCatchFinallyExample();
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
