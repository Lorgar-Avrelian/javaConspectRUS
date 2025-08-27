package lorgar.avrelian;

import lorgar.avrelian.broker.Broker;
import lorgar.avrelian.consumer.Consume;
import lorgar.avrelian.factory.Factory;
import lorgar.avrelian.producer.MessageFactory;
import lorgar.avrelian.producer.Produce;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        final int workTime = 15;
        final int brokerLength = 15;
        final Broker broker = new Broker(brokerLength);
        final MessageFactory messageFactory = new MessageFactory();
        final int producersTimeout = 2;
        final int consumersTimeout = 1;
        final Runnable produce0 = new Produce(broker, producersTimeout, messageFactory, brokerLength);
        final Runnable produce1 = new Produce(broker, producersTimeout, messageFactory, brokerLength / 3 * 2);
        final Runnable produce2 = new Produce(broker, producersTimeout, messageFactory, brokerLength / 3);
        final Runnable consume0 = new Consume(broker, consumersTimeout, 1);
        final Runnable consume1 = new Consume(broker, consumersTimeout, 1 + brokerLength / 3);
        final Runnable consume2 = new Consume(broker, consumersTimeout, 1 + brokerLength / 3 * 2);
        final ThreadFactory factory = new Factory();
        final Thread produceThread0 = factory.newThread(produce0);
        final Thread produceThread1 = factory.newThread(produce1);
        final Thread produceThread2 = factory.newThread(produce2);
        final Thread consumeThread0 = factory.newThread(consume0);
        final Thread consumeThread1 = factory.newThread(consume1);
        final Thread consumeThread2 = factory.newThread(consume2);
        startThreads(produceThread0, produceThread1, produceThread2, consumeThread0, consumeThread1, consumeThread2);
        try {
            TimeUnit.SECONDS.sleep(workTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}