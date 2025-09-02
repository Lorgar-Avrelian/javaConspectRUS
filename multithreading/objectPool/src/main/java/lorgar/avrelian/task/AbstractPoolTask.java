package lorgar.avrelian.task;

import lorgar.avrelian.pool.AbstractPool;

/**
 * Абстрактный класс (шаблон) для работы с пулом объектов.
 *
 * @author Victor Tokovenko
 */
public abstract class AbstractPoolTask<T> implements Runnable {
    private final AbstractPool<T> pool;

    public AbstractPoolTask(final AbstractPool<T> pool) {
        this.pool = pool;
    }

    @Override
    public void run() {
        final T object = pool.acquire();
        try {
            System.out.println(object + " was acquired.");
            this.work(object);
        } finally {
            System.out.println(object + " is being released.");
            pool.release(object);
        }
    }

    /**
     * Метод для реализации операций с объектом, которые должны быть выполнены потоком в процессе работы с объектом.
     *
     * @param object объект из пула объектов {@link AbstractPool}.
     */
    protected abstract void work(final T object);
}
