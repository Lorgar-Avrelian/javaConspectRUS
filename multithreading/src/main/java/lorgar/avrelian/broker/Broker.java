package lorgar.avrelian.broker;

import lorgar.avrelian.consumer.Consume;
import lorgar.avrelian.model.Message;
import lorgar.avrelian.producer.Produce;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;
import java.util.Optional;

/**
 * Брокер сообщений
 *
 * @author Victor Tokovenko
 */
public final class Broker {
    private final Deque<Message> messages;
    private final int maxMessages;

    public Broker(final int maxMessages) {
        this.messages = new ArrayDeque<>();
        this.maxMessages = maxMessages;
    }

    public Deque<Message> getMessages() {
        return messages;
    }

    public int getMaxMessages() {
        return maxMessages;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Broker broker = (Broker) o;
        return maxMessages == broker.maxMessages && Objects.equals(messages, broker.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messages, maxMessages);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + " [messages = " + messages + "]";
    }

    /**
     * Метод для публикации сообщений в очереди брокера
     *
     * @param message сообщение
     * @param produce задача для продюсера
     * @return булев результат успешной/неудачной отправки
     */
    public synchronized boolean produce(final Message message, final Produce produce) {
        while (!this.shouldProduce(produce)) {
            try {
                super.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        boolean add = this.messages.add(message);
        super.notifyAll();
        return add;
    }

    /**
     * Метод для определения возможности публикации сообщения потоком-продюсером
     *
     * @param produce задача для продюсера
     * @return булев результат возможности публикации с учётом текущих настроек продюсера и брокера
     */
    private boolean shouldProduce(final Produce produce) {
        return this.messages.size() < this.maxMessages && produce.getMaximumMessages() >= this.messages.size();
    }

    /**
     * Метод для чтения сообщений из очереди брокера
     *
     * @param task задача для получателя
     * @return {@link Optional} с прочитанным сообщением
     */
    public synchronized Optional<Message> consume(Consume task) {
        while (!this.shouldConsume(task)) {
            try {
                super.wait(task.getSleep() * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return Optional.empty();
            }
        }
        Message message = this.messages.poll();
        super.notify();
        return Optional.ofNullable(message);
    }

    /**
     * Метод для определения возможности чтения сообщения потоком-получателем
     *
     * @param consume задача для получателя
     * @return булев результат возможности чтения с учётом текущих настроек получателя и брокера
     */
    private boolean shouldConsume(final Consume consume) {
        return !this.messages.isEmpty() && consume.getMinimalMessages() <= this.messages.size();
    }
}
