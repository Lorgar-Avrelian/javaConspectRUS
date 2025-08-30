package lorgar.avrelian;

import lorgar.avrelian.consumer.ReadTask;
import lorgar.avrelian.factory.Factory;
import lorgar.avrelian.handler.ExceptionHandler;
import lorgar.avrelian.producer.WriteTask;
import lorgar.avrelian.topic.Topic;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <b>Пример реализации топика для брокера сообщений.</b><br><br>
 * Топик является параметризуемым и может содержать ограниченное количество сообщений (имеет конечный размер).<br>
 * Продюсеры и потребители сообщений публикуют и получают сообщения из топика, соответственно, используя его методы.<br><br>
 * <ld>И для продюсеров, и для потребителей сообщений характерны следующие свойства:</ld>
 * <li>создаются при помощи фабрики потоков и запускаются в параллельных потоках;</li>
 * <li>имеют задержку времени выполнения, задаваемую в секундах при инициализации;</li>
 * <li>обрабатывают исключения, которые могут возникнуть в процессе работы при помощи обработчика непроверяемых исключений;</li>
 * <li>имеют ограничение по максимальным публикуемому (для продюсеров) и читаемым (для потребителей) количествам сообщений в/из топика.</li>
 *
 * <br>
 * Продюсеры могут публиковать сообщения только тогда, когда в топике имеется доступное место (не превышено ограничение на количество сообщений в топике).<br><br>
 * Потребители могут читать сообщения из топика только тогда, когда в топике имеются какие-либо сообщения (топик не пуст).<br><br>
 * Контроль количества сообщений в топике производится путём использования блокировки {@link Lock} и связанных с ней
 * реализаций интерфейса {@link Condition}, содержащихся непосредственно в классе топика {@link Topic}.
 *
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        // размер топика
        final int topicSize = 3;
        // топик
        final Topic<String> topic = new Topic<>(topicSize);
        // обработчик непроверяемых исключений
        final Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionHandler();
        // фабрика потоков
        final ThreadFactory factory = new Factory(exceptionHandler);
        // количество генерируемых сообщений
        final int writeCount = 3;
        // таймаут между отправками сообщений
        final int writeTimeout = 1;
        // задачи для продюсеров
        final Runnable writeTask0 = new WriteTask(topic, writeCount, writeTimeout);
        final Runnable writeTask1 = new WriteTask(topic, writeCount, writeTimeout);
        final Runnable writeTask2 = new WriteTask(topic, writeCount, writeTimeout);
        // количество читаемых сообщений
        final int readCount = 2;
        // таймаут между чтениями сообщений
        final int readTimeout = 3;
        // задачи для потребителей
        final Runnable readTask0 = new ReadTask(topic, readCount, readTimeout);
        final Runnable readTask1 = new ReadTask(topic, readCount, readTimeout);
        final Runnable readTask2 = new ReadTask(topic, readCount, readTimeout);
        // потоки-продюсеры
        final Thread producer0 = factory.newThread(writeTask0);
        producer0.setName("Producer 1");
        final Thread producer1 = factory.newThread(writeTask1);
        producer1.setName("Producer 2");
        final Thread producer2 = factory.newThread(writeTask2);
        producer2.setName("Producer 3");
        // потоки-потребители
        final Thread consumer0 = factory.newThread(readTask0);
        consumer0.setName("Consumer 1");
        final Thread consumer1 = factory.newThread(readTask1);
        consumer1.setName("Consumer 2");
        final Thread consumer2 = factory.newThread(readTask2);
        consumer2.setName("Consumer 3");
        // запуск всех потоков
        startThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
        joinThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
        // окончание выполнения и вывод результатов
        System.out.println(Thread.currentThread().getName() + " finished work.");
        topic.messages();
    }

    /**
     * Метод для запуска нескольких потоков.
     *
     * @param threads потоки для запуска
     */
    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    /**
     * Метод для приостановки выполнения потока main до завершения выполнения нескольких потоков.
     *
     * @param threads ожидаемые потоки
     */
    private static void joinThreads(Thread... threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                thread.interrupt();
            }
        }
    }
}