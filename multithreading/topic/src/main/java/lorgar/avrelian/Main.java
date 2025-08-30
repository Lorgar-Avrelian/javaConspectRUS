package lorgar.avrelian;

import lorgar.avrelian.consumer.ReadTask;
import lorgar.avrelian.factory.Factory;
import lorgar.avrelian.handler.ExceptionHandler;
import lorgar.avrelian.producer.WriteTask;
import lorgar.avrelian.topic.Topic;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

/**
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        final int topicSize = 3;
        final Topic<String> topic = new Topic<>(topicSize);
        final Thread.UncaughtExceptionHandler exceptionHandler = new ExceptionHandler();
        final ThreadFactory factory = new Factory(exceptionHandler);
        final int writeCount = 3;
        final int writeTimeout = 1;
        final Runnable writeTask0 = new WriteTask(topic, writeCount, writeTimeout);
        final Runnable writeTask1 = new WriteTask(topic, writeCount, writeTimeout);
        final Runnable writeTask2 = new WriteTask(topic, writeCount, writeTimeout);
        final int readCount = 2;
        final int readTimeout = 3;
        final Runnable readTask0 = new ReadTask(topic, readCount, readTimeout);
        final Runnable readTask1 = new ReadTask(topic, readCount, readTimeout);
        final Runnable readTask2 = new ReadTask(topic, readCount, readTimeout);
        final Thread producer0 = factory.newThread(writeTask0);
        producer0.setName("Producer 1");
        final Thread producer1 = factory.newThread(writeTask1);
        producer1.setName("Producer 2");
        final Thread producer2 = factory.newThread(writeTask2);
        producer2.setName("Producer 3");
        final Thread consumer0 = factory.newThread(readTask0);
        consumer0.setName("Consumer 1");
        final Thread consumer1 = factory.newThread(readTask1);
        consumer1.setName("Consumer 2");
        final Thread consumer2 = factory.newThread(readTask2);
        consumer2.setName("Consumer 3");
        startThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
        joinThreads(producer0, producer1, producer2, consumer0, consumer1, consumer2);
        System.out.println(Thread.currentThread().getName() + " finished work.");
        topic.messages();
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }

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