package at.fhtechnikumwien.ode.common;

import at.fhtechnikumwien.ode.common.messages.Message;

public interface MessageHandler {
    void handleMessage(Message<?> msg, SocketHandler socketHandler);
}
