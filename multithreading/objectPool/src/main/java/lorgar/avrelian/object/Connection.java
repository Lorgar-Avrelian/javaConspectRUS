package lorgar.avrelian.object;

import java.util.Objects;

/**
 * Класс-имитация соединения с базой данных.
 *
 * @author Victor Tokovenko
 */
public final class Connection {
    private final long id;
    private boolean autoCommit;

    public Connection(final long id, final boolean autoCommit) {
        this.id = id;
        this.autoCommit = autoCommit;
    }

    public long getId() {
        return id;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return id == that.id && autoCommit == that.autoCommit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, autoCommit);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                "[id=" + id +
                ", autoCommit=" + autoCommit +
                ']';
    }
}
