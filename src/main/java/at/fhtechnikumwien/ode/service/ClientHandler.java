package at.fhtechnikumwien.ode.service;

import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.MyLogger;
import at.fhtechnikumwien.ode.common.ResponseType;
import at.fhtechnikumwien.ode.common.messages.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable{
    final UUID id;
    final DataInputStream dis;
    final DataOutputStream dos;
    Socket s;
    boolean isloggedin;
    String num;

    private MyLogger logger(){
        return Enviroment.instance().getLogger();
    }


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
                Message<?> msg = MessageParser.receive(dis);
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
            //remove client from server
            Server.clients.remove(this.id);
            if(num != null){
                Server.loggedClients.remove(this.num);
            }
            // closing resources
            this.dis.close();
            this.dos.close();
            this.s.close();
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private ResponseMessage handleLogin(LogInMessage msg){
        ResponseMessage resp = new ResponseMessage();
        if (isloggedin){
            resp.type = ResponseType.NACK;
            resp.message = "login: user is already logged in.";
            logger().logg(resp.message);
            return resp;
        }

        String num = msg.number;
        if(num == null || num.isBlank()){
            resp.type = ResponseType.NACK;
            resp.message = "login: invalid number.";
            logger().logg(resp.message);
            return resp;
        }

        if(Server.loggedClients.putIfAbsent(num, this) == null){
            Server.clients.remove(this.id);
            isloggedin = true;
            resp.type = ResponseType.ACK;
            resp.message = "login: user successfully logged in.";
            logger().logg(resp.message);
            return  resp;
        } else {
            resp.type = ResponseType.NACK;
            resp.message = "login: another client with the same number is allready logged in.";
            logger().logg(resp.message);
            return  resp;
        }
    }

    private ResponseMessage handleChat(ChatMessage<?> msg){
        // search for the recipient in the connected devices list.
        // ar is the vector storing client of active users
        ResponseMessage rsp = new ResponseMessage();
        if(!this.isloggedin){
            rsp.type = ResponseType.NACK;
            rsp.message = "chat: user is not logged in.";
            logger().logg("chat: user not logged in.");
            return rsp;
        }

        //store msg in db
        //NOT IMPLEMENTED

        //send msg to recipient
        ClientHandler client =  Server.loggedClients.get(msg.to);
        if(client != null){
            MessageParser.send(client.dos, msg);
            rsp.type = ResponseType.ACK;
            rsp.message = "chat: sent msg to chlient";
            logger().logg(rsp.message);
            return rsp;
        }
        else{
            rsp.type = ResponseType.NACK;
            rsp.message = "chat: persistent messages not implemented.";
            logger().logg(rsp.message);
            return rsp;
        }
    }
}
