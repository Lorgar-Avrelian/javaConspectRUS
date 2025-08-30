package lorgar.avrelian.factory;

import lorgar.avrelian.handler.ExceptionHandler;

import java.util.concurrent.ThreadFactory;

/**
 * Фабрика потоков
 *
 * @author Victor Tokovenko
 */
public class Factory implements ThreadFactory {
    private static final Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionHandler();

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        thread.setDaemon(true);
        return thread;
    }
}
