package lorgar.avrelian.consumer;

import lorgar.avrelian.broker.Broker;
import lorgar.avrelian.model.Message;

import java.util.concurrent.TimeUnit;

/**
 * Задача для получателей сообщений
 *
 * @author Victor Tokovenko
 */
public final class Consume implements Runnable {
    private static final String TERMINAL_MESSAGE_TEMPLATE = "%s has been consumed by %s\n\r";
    private final Broker broker;
    private final int sleep;
    private final int minimalMessages;
    private static int counter = 1;
    private final String name;
    private static final String NAME_TEMPLATE = "Consumer %s";

    public Consume(final Broker broker, int sleep, int minimalMessages) {
        this.broker = broker;
        this.sleep = sleep;
        this.minimalMessages = minimalMessages;
        this.name = String.format(NAME_TEMPLATE, counter++);
    }

    public int getMinimalMessages() {
        return minimalMessages;
    }

    public String getName() {
        return name;
    }

    public int getSleep() {
        return sleep;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final Message message = this.broker.consume(this).get();
            System.out.printf(TERMINAL_MESSAGE_TEMPLATE, message, this.getName());
            try {
                TimeUnit.SECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
