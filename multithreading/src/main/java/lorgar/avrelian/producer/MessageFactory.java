package lorgar.avrelian.producer;

import lorgar.avrelian.model.Message;

/**
 * @author Victor Tokovenko
 */
public final class MessageFactory {
    private static final int INITIAL_NEXT_MESSAGE_ID = 1;
    private static final String MESSAGE_TEMPLATE = "Message#%d from %s";
    private int nextMessageId;

    public MessageFactory() {
        this.nextMessageId = INITIAL_NEXT_MESSAGE_ID;
    }

    public Message create(Produce produce) {
        return new Message(String.format(MESSAGE_TEMPLATE, this.incrementNextMessageId(), produce.getName()));
    }

    private synchronized int incrementNextMessageId() {
        return this.nextMessageId++;
    }
}
