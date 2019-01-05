package work.haupt.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MessageContainer {

    public static ConcurrentLinkedQueue<ChatMessage> chatMessageQueue = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<String> botMessageQueue = new ConcurrentLinkedQueue<>();
}
