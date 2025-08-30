package lorgar.avrelian.topic;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Топик
 *
 * @author Victor Tokovenko
 */
public final class Topic<T> {
    private static final Lock lock = new ReentrantLock();
    private static final Condition empty = lock.newCondition();
    private static final Condition notEmpty = lock.newCondition();
    private final int length;
    private Deque<T> queu;
    private static final String READ_TEMPLATE = "%s read message %s\n\r";
    private static final String WRITE_TEMPLATE = "%s writes message %s\n\r";

    public Topic(int length) {
        this.length = length;
        this.queu = new ArrayDeque<T>(length);
    }

    /**
     * Метод для чтения сообщения из топика.
     *
     * @return сообщение из топика
     * @throws InterruptedException в случае прерывания потока
     */
    public T read() throws InterruptedException {
        lock.lock();
        try {
            while (queu.isEmpty()) {
                empty.await();
            }
            final T poll = queu.poll();
            System.out.printf(READ_TEMPLATE, Thread.currentThread().getName(), poll);
            messages();
            notEmpty.signalAll();
            return poll;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Метод для публикации сообщения в топик.
     *
     * @return булев результат публикации
     * @throws InterruptedException в случае прерывания потока
     */
    public boolean write(final T value) throws InterruptedException {
        lock.lock();
        try {
            while (queu.size() >= length) {
                notEmpty.await();
            }
            final boolean add = queu.add(value);
            System.out.printf(WRITE_TEMPLATE, Thread.currentThread().getName(), queu.peekLast());
            messages();
            empty.signalAll();
            return add;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Метод для вывода в терминал текущей статистики сообщений в топике.
     */
    public void messages() {
        System.out.println("Topic contains " + queu.size() + " messages: " + queu);
    }
}
