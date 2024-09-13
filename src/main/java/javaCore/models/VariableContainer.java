package javaCore.models;

import java.util.Objects;

public class VariableContainer<T extends Object> {
    private T value;

    public VariableContainer(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableContainer<?> that = (VariableContainer<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "VariableContainer{" +
                "value=" + value +
                '}';
    }
}
