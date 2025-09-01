package lorgar.avrelian.pool;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Абстрактный класс (шаблон) для создания пула объектов.
 *
 * @author Victor Tokovenko
 */
public abstract class AbstractPool<T> {
    private final List<PoolObject<T>> poolObjects;
    private final Semaphore semaphore;

    public AbstractPool(final Supplier<T> supplier, final int poolSize) {
        this.poolObjects = getPoolObject(supplier, poolSize);
        this.semaphore = new Semaphore(poolSize, true);
    }

    /**
     * Метод для создания {@link List} объектов {@link PoolObject} (пула объектов) на основании переданной реализации
     * функционального интерфейса {@link Supplier} и заданного размера пула.<br>
     * Метод применяется в конструкторе для инициализации {@link AbstractPool}.
     *
     * @param supplier реализация функционального интерфейса {@link Supplier}
     * @param poolSize размер пула объектов
     * @param <T>      тип объектов в пуле
     * @return {@link List} объектов {@link PoolObject} (пул объектов)
     */
    private static <T> List<PoolObject<T>> getPoolObject(final Supplier<T> supplier, final int poolSize) {
        return IntStream.range(0, poolSize)
                .mapToObj(i -> supplier.get())
                .map(object -> new PoolObject<>(object, false))
                .toList();
    }

    /**
     * Метод по извлечению объекта из пула объектов.
     *
     * @return свободный объект из пула объектов.
     */
    public final T acquire() {
        this.semaphore.acquireUninterruptibly();
        return this.acquireObject();
    }

    /**
     * Синхронизированный метод по нахождению и возвращению свободного объекта из пула объектов.<br>
     * Метод превращает операции нахождения и возвращения объекта в транзакцию (одну атомарную операцию). Условие
     * атомарности необходимо во избежание получения одного и того же объекта для двух разных потоков, вызванного
     * квантованием времени выполнения потоков.
     *
     * @return свободный объект из пула объектов.
     */
    private synchronized T acquireObject() {
        return this.poolObjects.stream()
                .filter(poolObject -> !poolObject.isIssued())
                .findFirst()
                .map(AbstractPool::markAsIssued)
                .map(PoolObject::getObject)
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Метод для изменения значения индикатора выдачи объекта из пула объектов на выданный ({@code true}).
     *
     * @param poolObject объект-обёртка объекта
     * @param <T>        тип объектов в пуле объектов.
     * @return {@link PoolObject} со значением индикатора {@code issued} равным {@code true}
     */
    private static <T> PoolObject<T> markAsIssued(final PoolObject<T> poolObject) {
        poolObject.setIssued(true);
        return poolObject;
    }

    /**
     * Метод по возвращению объекта обратно в пул объектов.
     *
     * @param object объект пула объектов.
     */
    public final void release(final T object) {
        if (this.releaseObject(object)) {
            this.semaphore.release();
        }
    }

    /**
     * Метод для нахождения в пуле объектов объекта, соответствующего возвращаемому объекту, и для его очистки (при
     * наличии).
     *
     * @param object возвращаемый в пул объект
     * @return {@code true}, если объект был найден, иначе - {@code false}.
     */
    private synchronized boolean releaseObject(final T object) {
        return this.poolObjects.stream()
                .filter(PoolObject::isIssued)
                .filter(poolObject -> Objects.equals(poolObject.getObject(), object))
                .findFirst()
                .map(this::clearPoolObject)
                .isPresent();
    }

    /**
     * Метод для очистки объекта после его возвращения в пул объектов.
     *
     * @param object возвращаемый в пул объект.
     */
    public abstract void clearObject(final T object);

    /**
     * Метод для очистки объекта и его объекта-обёртки при возвращении объекта в пул объектов.
     *
     * @param poolObject соответствующий объекту объект-обёртка пула объектов
     * @return объект-обёртка объекта {@link PoolObject}.
     */
    private PoolObject<T> clearPoolObject(final PoolObject<T> poolObject) {
        poolObject.setIssued(false);
        this.clearObject(poolObject.getObject());
        return poolObject;
    }

    /**
     * Класс-обёртка для объектов, хранящихся в пуле объектов.<br>
     *
     * @param <T> тип объектов в пуле объектов.
     */
    private static final class PoolObject<T> {
        /**
         * Объект в пуле объектов.
         */
        private final T object;
        /**
         * Индикатор выдачи объекта из пула объектов.
         */
        private boolean issued;

        public PoolObject(final T object, final boolean issued) {
            this.object = object;
            this.issued = issued;
        }

        /**
         * Метод для получения объекта, содержащегося в обёртке.
         *
         * @return {@code object}.
         */
        public T getObject() {
            return object;
        }

        /**
         * Метод для получения значения индикатора выдачи объекта из пула.
         *
         * @return {@code false}, если объект не выдан из пула объектов, иначе - {@code true}.
         */
        public boolean isIssued() {
            return issued;
        }

        /**
         * Метод для изменения значения индикатора выдачи объекта из пула.
         *
         * @param issued значение индикатора выдачи объекта из пула объектов.
         */
        public void setIssued(boolean issued) {
            this.issued = issued;
        }
    }
}
