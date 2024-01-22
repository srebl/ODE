package at.fhtechnikumwien.ode.client;

import at.fhtechnikumwien.ode.common.MessageHandler;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.SocketHandler;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;

import java.util.concurrent.CompletableFuture;

public class ClientMessageHandler implements MessageHandler {
    @Override
    public void handleMessage(Message<?> msg, SocketHandler socketHandler) {
        if(msg instanceof ResponseMessage response) {
            var callBacks = socketHandler.getCallBacks();
            var callBack = callBacks.remove(response.responeOfUuid);
            if (callBack == null) {
                //ignore msg
                return;
            }
            callBack.complete(Result.ok(msg));
        }
    }
}
