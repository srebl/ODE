package at.fhtechnikumwien.ode.common.messages;

import java.time.LocalDateTime;
import java.util.Date;

public abstract class ChatMessage<T extends ChatMessage<T>> extends Message<T> {
    public String from;
    public String to;
    public LocalDateTime date = LocalDateTime.now();

    public boolean isOneTimeMessage = false;
    public boolean hasOneTimeMessageBeenSeen = false;
}
