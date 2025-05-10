package lorgar.avrelian;

import lorgar.avrelian.models.Human;
import lorgar.avrelian.models.HumanUtility;
import lorgar.avrelian.models.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        //        switchMethod(2);
//        dayOfWeek(4);
//        ternarOperator(1, 2);
//        forExample();
//        whileExample();
//        doWhileExample();
//        forEachExample();
//        arraysExample();
//        textFromBytes();
//        scannerExample();
//        scannerUseDelimiterExample();
//        returnInVoidMethodExample(8);
//        utilityClassUsingExample();
        enumExample();
    }

    private static void enumExample() {
        Role[] values = Role.values();
        System.out.println(Arrays.toString(values));
        Role role = Role.valueOf("USER");
        System.out.println(role);
        int ordinal = Role.USER.ordinal();
        System.out.println(ordinal);
        int i = Role.GUEST.compareTo(Role.ADMIN);
        System.out.println(i);
        String name = Role.GUEST.name();
        System.out.println(name);
    }

    private static void utilityClassUsingExample() {
//        HumanUtility humanUtility = new HumanUtility(); // при попытке создать экземпляр класса HumanUtility возникнет ошибка
        Human ivan = new Human();
        ivan.setName("Ivan");
        ivan.setSurname("Ivanov");
        ivan.setAge(17);
        System.out.println(ivan);
        System.out.println(HumanUtility.isHumanAdult(ivan));
    }

    private static void returnInVoidMethodExample(int day) {
        if (day < 1 || day > 7) {
            System.out.println("Такого дня не существует!");
            return;
        } else {
            dayOfWeek(day);
        }
    }

    private static void scannerUseDelimiterExample() {
        Scanner scanner = new Scanner("У лукоморья дуб зелёный;`" +
                "Златая цепь на дубе том:`" +
                "И днём и ночью кот учёный`" +
                "Всё ходит по цепи кругом;`");
        scanner.useDelimiter("`");
        while (scanner.hasNext()) {
            System.out.println(scanner.next());
        }
        scanner.close();
    }

    private static void scannerExample() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите число: ");
            if (scanner.hasNextInt()) {
                int number = scanner.nextInt();
                System.out.println("Спасибо! Вы ввели число: " + number);
                break;
            } else {
                System.out.println("Извините! Введённое значение не является числом. Пожалуйста, попробуйте снова.");
            }
        }
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