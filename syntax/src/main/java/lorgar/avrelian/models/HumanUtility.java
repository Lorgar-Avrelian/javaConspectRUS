package lorgar.avrelian.models;

public class HumanUtility {
    private HumanUtility() {
    }

    public static boolean isHumanAdult(Human human) {
        if (human.getAge() >= 18) {
            return true;
        } else if (human.getAge() >= 0) {
            return false;
        } else {
            throw new IllegalArgumentException("Human age must be greater than or equal to 0");
        }
    }
}
