package lorgar.avrelian.producer;

import lorgar.avrelian.topic.Topic;

import java.util.concurrent.TimeUnit;

/**
 * @author Victor Tokovenko
 */
public final class WriteTask implements Runnable {
    private final Topic topic;
    private final int messages;
    private final int timeout;
    private static int counter = 1;
    private static final String MESSAGE_TEMPLATE = "%s - Message № %s";

    public WriteTask(Topic topic, int messages, int timeout) {
        this.topic = topic;
        this.messages = messages;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        for (int i = 0; i < messages; i++) {
            String message = String.format(MESSAGE_TEMPLATE, Thread.currentThread().getName(), counter++);
            try {
                topic.write(message);
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
