package at.fhtechnikumwien.ode.common.messages;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

public class TextMessage extends ChatMessage<TextMessage>{
    public String msg;

    public TextMessage(String from, String to, String msg){
        this.from = from;
        this.to = to;
        this.msg = msg;
    }

    public TextMessage(String from, String to, String msg, LocalDateTime date){
        this(from, to, msg);
        this.date = date;
    }

    @Override
    public byte[] getPayload() {

        return msg.getBytes();
    }

    @Override
    public TextMessage deserialize(byte[] msg) {
        this.msg = new String(msg, StandardCharsets.UTF_8);
        return this;
    }
}
