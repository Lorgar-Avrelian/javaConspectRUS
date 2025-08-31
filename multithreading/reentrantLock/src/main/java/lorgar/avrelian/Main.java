package lorgar.avrelian;

import lorgar.avrelian.consumer.ReadTask;
import lorgar.avrelian.factory.Factory;
import lorgar.avrelian.handler.ExceptionHandler;
import lorgar.avrelian.producer.WriteTask;
import lorgar.avrelian.shared_resource.Share;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <b>Пример реализации общего ресурса</b><br><br>
 * Потоки-продюсеры и потоки-потребители обращаются к общему ресурсу, изменяя его данные или читая их, соответственно.<br>
 * Синхронизация работы потоков реализована в классе общего ресурса с использованием класса {@link ReentrantLock}.
 *
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        // общий ресурс
        final Share share = new Share();
        // обработчик непроверяемых исключений
        final Thread.UncaughtExceptionHandler handler = new ExceptionHandler();
        // фабрика потоков
        final ThreadFactory factory = new Factory(handler);
        // таймаут между операциями потоков-продюсеров
        final int writeTimeout = 1;
        // количество операций потоков-продюсеров
        final int writeCount = 5;
        // задачи для потоков-продюсеров
        final Runnable write0 = new WriteTask(share, writeTimeout, writeCount);
        final Runnable write1 = new WriteTask(share, writeTimeout, writeCount);
        final Runnable write2 = new WriteTask(share, writeTimeout, writeCount);
        // потоки-продюсеры
        final Thread producer0 = factory.newThread(write0);
        producer0.setName("Producer 1");
        final Thread producer1 = factory.newThread(write1);
        producer1.setName("Producer 2");
        final Thread producer2 = factory.newThread(write2);
        producer2.setName("Producer 3");
        // таймаут между операциями потоков-потребителей
        final int readTimeout = 1;
        // количество операций потоков-потребителей
        final int readCount = 5;
        // задачи для потоков-потребителей
        final Runnable read0 = new ReadTask(share, readTimeout, readCount);
        final Runnable read1 = new ReadTask(share, readTimeout, readCount);
        final Runnable read2 = new ReadTask(share, readTimeout, readCount);
        // потоки-потребители
        final Thread consumer0 = factory.newThread(read0);
        consumer0.setName("Consumer 1");
        final Thread consumer1 = factory.newThread(read1);
        consumer1.setName("Consumer 2");
        final Thread consumer2 = factory.newThread(read2);
        consumer2.setName("Consumer 3");
        // запуск потоков
        startThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
        joinThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

    private static void joinThreads(Thread... threads) {
        Arrays.stream(threads).forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
    }
}