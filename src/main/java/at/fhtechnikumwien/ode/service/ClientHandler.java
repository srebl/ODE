package at.fhtechnikumwien.ode.service;

import at.fhtechnikumwien.ode.common.messages.ChatMessage;
import at.fhtechnikumwien.ode.common.messages.LogInMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.service.messages.MessageParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable{
    private UUID id;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;

    // constructor
    public ClientHandler(Socket s, UUID id, DataInputStream dis, DataOutputStream dos) {
        this.dis = dis;
        this.dos = dos;
        this.id = id;
        this.s = s;
        this.isloggedin=false;
    }

    @Override
    public void run() {
        boolean repeat = true;
        while (repeat)
        {
            try
            {
                Message<?> msg = MessageParser.deserialize(dis, dos);
                if(msg == null){
                    System.out.println("msg was null");
                    continue;
                }

                if(msg instanceof LogInMessage logInMessage){
                    handleLogin(logInMessage);
                }

                if(msg instanceof ChatMessage<?> chatMessage){
                    handleChat(chatMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                repeat = false;
            }
        }

        try {
            // closing resources
            this.dis.close();
        }catch(IOException e) {
            e.printStackTrace();
        }

        try {
            // closing resources
            this.dos.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void handleLogin(LogInMessage msg){
        if (isloggedin){
            System.out.println("user already logged in");
            return;
        }
        String num = msg.number;
        if(num == null || num.isBlank()){
            System.out.println("login: invalid number.");
            return;
        }

        if(Server.loggedClients.putIfAbsent(num, this) == null){
            Server.clients.remove(this.id);
            isloggedin = true;
        } else {
            System.out.println("login: another client is already logged in.");
        }
    }

    private void handleChat(ChatMessage<?> msg){
        // search for the recipient in the connected devices list.
        // ar is the vector storing client of active users
        System.out.println("sending msg to recipient");
        ClientHandler client =  Server.loggedClients.get(msg.to);

        if(client != null){
            System.out.println("recipient found");
            //client.dos.write(msg.serialize());
        }
    }
}
