package lorgar.avrelian.handler;

/**
 * @author Victor Tokovenko
 */
public final class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Exception message: %s in thread %s\n\r";

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(String.format(EXCEPTION_MESSAGE_TEMPLATE, e.getMessage(), t.getName()));
    }
}
