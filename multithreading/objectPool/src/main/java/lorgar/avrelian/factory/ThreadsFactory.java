package lorgar.avrelian.factory;

import lorgar.avrelian.handler.ExceptionHandler;

import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

/**
 * Фабрика потоков.
 *
 * @author Victor Tokovenko
 */
public class ThreadsFactory {
    private final Runnable task;
    private final int count;
    private static final Thread.UncaughtExceptionHandler handler = new ExceptionHandler();
    private static final ThreadFactory factory = new Factory(handler);

    public ThreadsFactory(Runnable task, int count) {
        this.task = task;
        this.count = count;
    }

    public Thread[] getThreads() {
        return IntStream.range(0, count)
                .mapToObj(i -> factory.newThread(task))
                .toArray(Thread[]::new);
    }
}
