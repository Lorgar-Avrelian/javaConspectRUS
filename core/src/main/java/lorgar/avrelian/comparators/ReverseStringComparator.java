package lorgar.avrelian.comparators;

import java.util.Comparator;

public class ReverseStringComparator implements Comparator<String> {

    // Метод compare работает так же, как compareTo в интерфейсе Comparable
    // Различие в том, что он сравнивает 2 объекта из аргументов,
    // а не this и other
    // Контракт у этого метода такой же, как у метода compareTo
    @Override
    public int compare(String s1, String s2) {
        // Чтобы обратить порядок, можно сравнить в обратном порядке
        // То есть, если s1 больше s2, то возвращаем не 1, а -1
        return s2.compareToIgnoreCase(s1);
    }
}
