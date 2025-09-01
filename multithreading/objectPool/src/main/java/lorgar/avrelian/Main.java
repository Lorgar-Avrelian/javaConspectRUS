package lorgar.avrelian;

import lorgar.avrelian.factory.ThreadsFactory;
import lorgar.avrelian.pool.ConnectionPool;
import lorgar.avrelian.task.ConnectionPoolTask;

import java.util.Arrays;

/**
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