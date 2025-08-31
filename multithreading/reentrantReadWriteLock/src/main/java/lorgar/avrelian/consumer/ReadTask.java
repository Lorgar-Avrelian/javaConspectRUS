package lorgar.avrelian.consumer;

import lorgar.avrelian.shared_resource.Share;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Задача для потребителей
 *
 * @author Victor Tokovenko
 */
public final class ReadTask implements Runnable {
    private final Share share;
    private final int timeout;
    private final int values;

    public ReadTask(Share share, int timeout, int values) {
        this.share = share;
        this.timeout = timeout;
        this.values = values;
    }

    @Override
    public void run() {
        IntStream.range(0, values).forEach(i -> {
            try {
                TimeUnit.SECONDS.sleep(timeout);
                share.getValue();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
