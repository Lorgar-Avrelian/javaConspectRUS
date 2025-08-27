package lorgar.avrelian.producer;

import lorgar.avrelian.broker.Broker;
import lorgar.avrelian.model.Message;

import java.util.concurrent.TimeUnit;

/**
 * @author Victor Tokovenko
 */
public final class Produce implements Runnable {
    private static final String TERMINAL_MESSAGE_TEMPLATE = "%s has been sent!\n\r";
    private final Broker broker;
    private final int sleep;
    private final MessageFactory messageFactory;
    private final int maximumMessages;
    private static int counter = 1;
    private final String name;
    private static final String NAME_TEMPLATE = "Producer %s";

    public Produce(Broker broker, int sleep, MessageFactory messageFactory, int maximumMessages) {
        this.broker = broker;
        this.sleep = sleep;
        this.messageFactory = messageFactory;
        this.maximumMessages = maximumMessages;
        this.name = String.format(NAME_TEMPLATE, counter++);
    }

    public int getMaximumMessages() {
        return maximumMessages;
    }

    public String getName() {
        return name;
    }

    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            final Message message = this.messageFactory.create(this);
            this.broker.produce(message, this);
            System.out.printf(TERMINAL_MESSAGE_TEMPLATE, message);
            try {
                TimeUnit.SECONDS.sleep(sleep);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
