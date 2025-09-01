package lorgar.avrelian.pool;

import lorgar.avrelian.object.Connection;

import java.util.function.Supplier;

/**
 * Класс-имитация пула соединений с базой данных.
 *
 * @author Victor Tokovenko
 */
public class ConnectionPool extends AbstractPool<Connection> {
    public ConnectionPool(int poolSize) {
        super(new ConnectionSupplier(), poolSize);
    }

    @Override
    public void clearObject(Connection connection) {
        connection.setAutoCommit(true);
    }

    private static final class ConnectionSupplier implements Supplier<Connection> {
        private long id;

        @Override
        public Connection get() {
            return new Connection(this.id++, true);
        }
    }
}
