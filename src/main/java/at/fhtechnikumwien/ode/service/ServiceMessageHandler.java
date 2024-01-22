package at.fhtechnikumwien.ode.service;

import at.fhtechnikumwien.ode.common.*;
import at.fhtechnikumwien.ode.common.messages.*;

import java.time.LocalDateTime;
import java.util.Objects;

public class ServiceMessageHandler implements MessageHandler {
    private final ClientHandler clientHandler;

    public ServiceMessageHandler(ClientHandler clientHandler){
        this.clientHandler = clientHandler;
    }
    @Override
    public void handleMessage(Message<?> msg, SocketHandler socketHandler) {
        if (msg instanceof ResponseMessage response) {
            responseMsg(response, socketHandler);
        } else if (msg instanceof LogInMessage logInMessage){
            handleLogin(logInMessage, socketHandler);
        } else if(msg instanceof ChatMessage<?> chatMessage){
            handleChat(chatMessage, socketHandler);
        }
        return;
    }

    private void responseMsg(ResponseMessage msg, SocketHandler socketHandler){
        var callBacks = socketHandler.getCallBacks();
        var callBack = callBacks.remove(msg.responeOfUuid);
        if (callBack == null) {
            //ignore msg
            return;
        }
        callBack.complete(Result.ok(msg));
    }

    private synchronized void handleLogin(LogInMessage msg, SocketHandler socketHandler){
        ResponseMessage resp = new ResponseMessage();
        resp.responeOfUuid = msg.uuid;

        if (clientHandler.isloggedin){
            resp.type = ResponseType.ACK;
            resp.message = "OK";
        }else if(msg.number == null || msg.number.isBlank()){
            resp.type = ResponseType.NACK;
            resp.message = "login: invalid number.";
            Enviroment.logg(resp.message);
        }else if (Server.loggedClients.putIfAbsent(msg.number, clientHandler) == null){
            Server.clients.remove(clientHandler.id);
            clientHandler.isloggedin = true;
            resp.type = ResponseType.ACK;
            resp.message = "login: user successfully logged in.";
            Enviroment.logg(resp.message);
        } else {
            resp.type = ResponseType.NACK;
            resp.message = "login: another client with the same number is allready logged in.";
            Enviroment.logg(resp.message);
        }

        socketHandler.sendMsg(resp);
    }

    private void handleChat(ChatMessage<?> msg, SocketHandler socketHandler){
        ResponseMessage rsp = new ResponseMessage();
        if(!clientHandler.isloggedin){
            rsp.type = ResponseType.NACK;
            rsp.message = "chat: user is not logged in.";
            Enviroment.logg(rsp.message);
            socketHandler.sendMsg(rsp);
        }

        //store msg in db
        //NOT IMPLEMENTED

        //send msg to recipient
        sendNewMessageNotification(msg);
    }

    private Result<Boolean, String> sendNewMessageNotification(ChatMessage<?> msg){
        ClientHandler client = Server.loggedClients.get(msg.to);
        if(client == null){
            return Result.err("client is not logged in");
        }

        NotifyNewMessage newMsg = new NotifyNewMessage();
        newMsg.chatMessage = msg;
        var r_res = client.getSocketHandler().sendMsgWithAck(newMsg);
        if(r_res.isErr()){
            return Result.err(r_res.getErr());
        }

        if(r_res.unwrap() instanceof ResponseMessage response){
            if(response.type == ResponseType.ACK){
                return Result.ok(true);
            } else {
                return Result.err("recipient refused message.");
            }
        }

        return Result.err("unknown response message");
    }
}
