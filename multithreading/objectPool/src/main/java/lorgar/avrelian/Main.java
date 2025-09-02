package lorgar.avrelian;

import lorgar.avrelian.factory.ThreadsFactory;
import lorgar.avrelian.pool.ConnectionPool;
import lorgar.avrelian.task.ConnectionPoolTask;

import java.util.Arrays;
import java.util.concurrent.Semaphore;

/**
 * <b>Пример реализации пула объектов с ограничением по количеству одновременно работающих с ним потоков.</b><br><br>
 *
 * Количество объектов в пуле задаётся при инициализации.<br>
 * Ограничение по количеству одновременно работающих с пулом объектов потоков реализовано с помощью синхронизатора
 * {@link Semaphore}.<br>
 * Операции поиска в пуле и возврата из пула свободного объекта, а также поиска соответствующего в пуле, очищения и
 * непосредственного возврата в пул объекта реализованы в отдельных {@code synchronized} методах для обеспечения
 * транзакционности.<br>
 * При возвращении объекта потом в пул происходит его автоматическое "очищение", в процессе которого он возвращается в
 * исходное состояние.<br><br>
 *
 * Для демонстрации работы создан пул объектов-имитаторов соединений к БД, для которого при инициализации дополнительно
 * задаётся таймаут ожидания потока (имитация работы).
 *
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        final int poolSize = 3;
        final int timeout = 3;
        final ConnectionPool pool = new ConnectionPool(poolSize);
        final ConnectionPoolTask task = new ConnectionPoolTask(pool, timeout);
        final int threadCount = 10;
        final ThreadsFactory threadsFactory = new ThreadsFactory(task, threadCount);
        Thread[] threads = threadsFactory.getThreads();
        startThreads(threads);
        joinThreads(threads);
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static void joinThreads(Thread... threads) {
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
            }
        });
    }
}