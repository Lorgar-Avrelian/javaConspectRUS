package javaSyntax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        switchMethod(2);
        dayOfWeek(4);
        ternarOperator(1, 2);
        forExample();
        whileExample();
        doWhileExample();
        forEachExample();
        arraysExample();
        textFromBytes();
    }

    private static void textFromBytes() {
        byte[] textInBytes = {33, 33, 33};
        String stringFromBytes = new String(textInBytes);
        System.out.println(stringFromBytes);
    }

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

    private static void forEachExample() {
        List<Integer> testList = new ArrayList<>(List.of(1, 2, 3));
        for (Integer i : testList) {
            System.out.println("i = " + i);
        }
    }

    private static void doWhileExample() {
        int i = 0;
        do {
            action1();
            i++;
        } while (i < 10);
    }

    private static void whileExample() {
        int i = 10;
        while (i > 0) {
            action1();
            i--;
        }
    }

    private static void forExample() {
        for (int i = 0; i < 10; i++) {
            action1();
        }
    }

    private static void ternarOperator(int a, int b) {
        int result = a > b ? 1 : 2;
        switchMethod(result);
    }

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

    public static void action1() {
        System.out.println("Выполнить действие 1");
    }

    public static void action2() {
        System.out.println("Выполнить действие 2");
    }

    public static void action3() {
        System.out.println("Выполнить действие 3");
    }

    public static void actionByDefault() {
        System.out.println("Выполнить действие по умолчанию");
    }
}
