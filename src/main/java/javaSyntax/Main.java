package javaSyntax;

public class Main {
    public static void main(String[] args) {
        switchMethod(2);
        dayOfWeek(4);
        ternarOperator(1, 2);
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
