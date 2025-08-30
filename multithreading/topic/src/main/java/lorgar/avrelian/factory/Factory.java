package lorgar.avrelian.factory;

import java.util.concurrent.ThreadFactory;

/**
 * @author Victor Tokovenko
 */
public class Factory implements ThreadFactory {
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    public Factory(Thread.UncaughtExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }


    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setUncaughtExceptionHandler(exceptionHandler);
        return thread;
    }
}
