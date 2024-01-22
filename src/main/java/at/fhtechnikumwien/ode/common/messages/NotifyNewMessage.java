package at.fhtechnikumwien.ode.common.messages;

public class NotifyNewMessage extends Message<NotifyNewMessage>{

    public ChatMessage<?> chatMessage;

    @Override
    public byte[] getPayload() {
        return new byte[0];
    }

    @Override
    public NotifyNewMessage deserialize(byte[] msg) {
        return null;
    }
}
