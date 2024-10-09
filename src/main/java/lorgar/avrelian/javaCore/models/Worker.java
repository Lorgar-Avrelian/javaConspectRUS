package lorgar.avrelian.javaCore.models;

import java.util.Objects;

public class Worker extends Person {
    private static int counter = 1;
    private final int id;
    private String position;

    public Worker(String name, String surname, int age, String position) {
        super(name, surname, age);
        this.position = position;
        this.id = counter++;
    }

    public int getId() {
        return id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Worker worker = (Worker) o;
        return id == worker.id && Objects.equals(position, worker.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, position);
    }

    @Override
    public String toString() {
        return "Worker[" +
                "id=" + id +
                ", person=" + super.toString() +
                ", position='" + position + '\'' +
                ']';
    }
}
