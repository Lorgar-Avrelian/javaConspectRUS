package lorgar.avrelian.shared_resource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Общий ресурс
 *
 * @author Victor Tokovenko
 */
public final class Share {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final Lock readLock = lock.readLock();
    private static final Lock writeLock = lock.writeLock();
    private int value;
    private final int step;
    private static boolean changed = false;

    public Share() {
        this(0, 1);
    }

    public Share(int value, int step) {
        this.value = value;
        this.step = step;
    }

    public int getValue() throws InterruptedException {
        readLock.lock();
        try {
            changed = false;
            System.out.println(Thread.currentThread().getName() + " read value: " + value);
            return value;
        } finally {
            readLock.unlock();
        }
    }

    public void setValue() throws InterruptedException {
        writeLock.lock();
        try {
            changed = true;
            this.value += step;
            System.out.println(Thread.currentThread().getName() + " changed value to: " + this.value);
        } finally {
            writeLock.unlock();
        }
    }
}
