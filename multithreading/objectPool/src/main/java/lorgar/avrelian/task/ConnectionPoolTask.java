package lorgar.avrelian.task;

import lorgar.avrelian.object.Connection;
import lorgar.avrelian.pool.AbstractPool;

import java.util.concurrent.TimeUnit;

/**
 * Класс-имитация задачи для работы с пулом соединений.
 *
 * @author Victor Tokovenko
 */
public class ConnectionPoolTask extends AbstractPoolTask<Connection> {
    private final int timeout;

    public ConnectionPoolTask(AbstractPool<Connection> pool, int timeout) {
        super(pool);
        this.timeout = timeout;
    }

    @Override
    protected void work(final Connection connection) {
        try {
            connection.setAutoCommit(false);
            TimeUnit.SECONDS.sleep(timeout);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
