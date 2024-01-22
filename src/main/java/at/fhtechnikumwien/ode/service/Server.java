package at.fhtechnikumwien.ode.service;

import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.MyLogger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static ConcurrentHashMap<UUID, ClientHandler> clients = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, ClientHandler> loggedClients = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        Enviroment.instance().setLogger(new MyLogger("nope"));
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(4711);
            Socket socket;

            while(true){
                socket = serverSocket.accept();

                // obtain input and output streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                // Create a new handler object for handling this request.
                UUID id = UUID.randomUUID();
                ClientHandler mtch = new ClientHandler(socket, id, dis, dos);
                System.out.println("created new ClientHandler with id: " + id);

                // Create a new Thread with this object.
                Thread t = new Thread(mtch);

                System.out.println("Adding this client to active client list");

                // add this client to active clients list
                clients.put(id, mtch);

                // start the thread.
                t.start();
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private void close(Socket s){
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
