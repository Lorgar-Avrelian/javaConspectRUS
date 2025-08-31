package lorgar.avrelian.producer;

import lorgar.avrelian.shared_resource.Share;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Задача для продюсеров
 *
 * @author Victor Tokovenko
 */
public final class WriteTask implements Runnable {
    private final Share share;
    private final int timeout;
    private final int values;

    public WriteTask(Share share, int timeout, int values) {
        this.share = share;
        this.timeout = timeout;
        this.values = values;
    }

    public void run() {
        IntStream.range(0, values).forEach(i -> {
            try {
                share.setValue();
                TimeUnit.SECONDS.sleep(timeout);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}
