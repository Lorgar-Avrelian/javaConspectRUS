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
 * <b>Пример реализации брокера сообщений.</b><br><br>
 * В брокере сообщений находится очередь сообщений, максимальная длина которой задаётся при инициализации.<br>
 * Продюсеры и получатели сообщений публикуют и получают сообщения из очереди брокера сообщений, соответственно.<br><br>
 * <ld>И для продюсеров, и для получателей сообщений характерны следующие свойства:</ld>
 * <li>создаются при помощи фабрики потоков и запускаются в параллельных потоках-демонах;</li>
 * <li>имеют задержку времени выполнения, задаваемую в секундах при инициализации;</li>
 * <li>обрабатывают исключения, которые могут возникнуть в процессе работы при помощи обработчика непроверяемых исключений;</li>
 * <li>имеют имена, генерируемые автоматически при инициализации;</li>
 * <li>имеют ограничение по максимальному (для продюсеров) и минимальному (для получателей) количеству сообщений в очереди.</li>
 * Отправляемые сообщения генерируются при помощи фабрики сообщений.<br><br>
 * <ld>Продюсеры публикуют сообщения в брокере в зависимости от длины очереди сообщений:</ld>
 * <li>от 0 до 5 - все продюсеры;</li>
 * <li>от 6 до 10 - только продюсеры 1 и 2;</li>
 * <li>от 11 до 15 - только продюсер 1.</li>
 * <br>
 * <ld>Получатели читают сообщения из брокера в зависимости от длины очереди сообщений:</ld>
 * <li>от 1 до 5 - только получатель 1;</li>
 * <li>от 6 до 10 - получатели 1 и 2;</li>
 * <li>от 11 до 15 - все получатели.</li>
 *
 * @author Victor Tokovenko
 */
public class Main {
    public static void main(String[] args) {
        // время работы приложения
        final int workTime = 15;
        // длина очереди сообщений в брокере
        final int brokerLength = 15;
        // брокер сообщений
        final Broker broker = new Broker(brokerLength);
        // фабрика сообщений
        final MessageFactory messageFactory = new MessageFactory();
        // время задержки работы продюсеров
        final int producersTimeout = 2;
        // время задержки работы получателей
        final int consumersTimeout = 1;
        // задачи продюсеров
        final Runnable produce0 = new Produce(broker, producersTimeout, messageFactory, brokerLength);
        final Runnable produce1 = new Produce(broker, producersTimeout, messageFactory, brokerLength / 3 * 2);
        final Runnable produce2 = new Produce(broker, producersTimeout, messageFactory, brokerLength / 3);
        // задачи получателей
        final Runnable consume0 = new Consume(broker, consumersTimeout, 1);
        final Runnable consume1 = new Consume(broker, consumersTimeout, 1 + brokerLength / 3);
        final Runnable consume2 = new Consume(broker, consumersTimeout, 1 + brokerLength / 3 * 2);
        // фабрика потоков
        final ThreadFactory factory = new Factory();
        // потоки продюсеров
        final Thread produceThread0 = factory.newThread(produce0);
        final Thread produceThread1 = factory.newThread(produce1);
        final Thread produceThread2 = factory.newThread(produce2);
        // потоки получателей
        final Thread consumeThread0 = factory.newThread(consume0);
        final Thread consumeThread1 = factory.newThread(consume1);
        final Thread consumeThread2 = factory.newThread(consume2);
        // запуск потоков
        startThreads(produceThread0, produceThread1, produceThread2, consumeThread0, consumeThread1, consumeThread2);
        // задержка времени выполнения основного потока main
        try {
            TimeUnit.SECONDS.sleep(workTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для запуска нескольких потоков.
     *
     * @param threads потоки для запуска
     */
    private static void startThreads(Thread... threads) {
        Arrays.stream(threads).forEach(Thread::start);
    }
}