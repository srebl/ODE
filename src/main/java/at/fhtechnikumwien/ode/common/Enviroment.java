package at.fhtechnikumwien.ode.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Enviroment {

    private static Enviroment instance;

    private Enviroment(){}

    private MyLogger myLogger;

    private SocketHandler socketHandler;

    private MessageHandler messageHandler;

    public static synchronized Enviroment instance(){
        if(instance == null){
            instance = new Enviroment();
        }
        return instance;
    }

    public MyLogger getLogger() {
        return myLogger;
    }

    public void setLogger(MyLogger myLogger) {
        this.myLogger = myLogger;
    }


    public SocketHandler getSocketHandler() {
        return socketHandler;
    }

    public void setSocketHandler(SocketHandler socketHandler) {
        this.socketHandler = socketHandler;
    }

    public MessageHandler getMessageHandler() {
        return messageHandler;
    }

    public void setMessageHandler(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    public static void logg(String msg){
        instance().getLogger().logg(msg);
    }
}
