package at.fhtechnikumwien.ode.common.messages;

import java.util.Date;

public abstract class ChatMessage<T extends ChatMessage<T>> extends Message<T> {
    public String from;
    public String to;
    public Date date;
}
