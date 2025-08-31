package lorgar.avrelian.shared_resource;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Общий ресурс
 *
 * @author Victor Tokovenko
 */
public final class Share {
    private static final ReentrantLock lock = new ReentrantLock(true);
    private static final Condition read = lock.newCondition();
    private static final Condition write = lock.newCondition();
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
        lock.lock();
        try {
            while (!changed) {
                read.await();
            }
            changed = false;
            System.out.println(Thread.currentThread().getName() + " read value: " + value);
            write.signal();
            return value;
        } finally {
            lock.unlock();
        }
    }

    public void setValue() throws InterruptedException {
        lock.lock();
        try {
            while (changed) {
                write.await();
            }
            changed = true;
            this.value += step;
            System.out.println(Thread.currentThread().getName() + " changed value to: " + this.value);
            read.signal();
        } finally {
            lock.unlock();
        }
    }
}
