package at.fhtechnikumwien.ode.common.messages;

import java.nio.charset.StandardCharsets;

public class TextMessage extends ChatMessage<TextMessage>{
    public String msg;

    public TextMessage(String msg){
        this.msg = msg;
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
