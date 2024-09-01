package javaCore;

import javaCore.models.Person;
import javaCore.models.Worker;

public class Main {
    public static void main(String[] args) {
        inheritanceExample();
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
