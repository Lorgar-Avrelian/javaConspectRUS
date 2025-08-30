package lorgar.avrelian.consumer;

import lorgar.avrelian.topic.Topic;

import java.util.concurrent.TimeUnit;

/**
 * @author Victor Tokovenko
 */
public final class ReadTask implements Runnable {
    private final Topic topic;
    private final int messages;
    private final int timeout;

    public ReadTask(Topic topic, int messages, int timeout) {
        this.topic = topic;
        this.messages = messages;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        for (int i = 0; i < messages; i++) {
            try {
                TimeUnit.SECONDS.sleep(timeout);
                topic.read();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
