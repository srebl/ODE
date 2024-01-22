package at.fhtechnikumwien.ode.service;

import at.fhtechnikumwien.ode.common.*;
import at.fhtechnikumwien.ode.common.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable{
    final UUID id;
    boolean isloggedin = false;
    String num;

    private final SocketHandler socketHandler;


    // constructor
    public ClientHandler(Socket s, UUID id, DataInputStream dis, DataOutputStream dos) throws IOException {
        this.id = id;
        MessageHandler msgHandler = new ServiceMessageHandler(this);
        socketHandler = new SocketHandler(s, msgHandler);
    }

    @Override
    public void run() {
        while (true) {}
    }

    public void close(){
        socketHandler.closeSocket();
    }

    public SocketHandler getSocketHandler(){
        return socketHandler;
    }
}
