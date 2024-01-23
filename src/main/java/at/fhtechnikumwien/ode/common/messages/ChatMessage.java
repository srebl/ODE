package at.fhtechnikumwien.ode.common.messages;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.Date;

@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(TextMessage.class),
})
public abstract class ChatMessage<T extends ChatMessage<T>> extends Message<T> {
    public String from;
    public String to;
    public LocalDateTime date = LocalDateTime.now();

    public boolean isOneTimeMessage = false;
    public boolean hasOneTimeMessageBeenSeen = false;
}
