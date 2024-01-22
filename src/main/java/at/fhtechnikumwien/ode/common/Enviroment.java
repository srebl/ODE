package at.fhtechnikumwien.ode.common;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Enviroment {

    private static Enviroment instance;

    private Enviroment(){}

    private MyLogger myLogger;
    private final String serverAddress = "localhost";
    private final int serverPort = 4711;

    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;

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

    public String getServerAddress() {
        return serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setSocket(Socket s){
        this.socket = s;
        try {
            this.dis = new DataInputStream(s.getInputStream());
            this.dos = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            Enviroment.instance().getLogger().logg(e.toString());
        }
    }

    public Socket getSocket(){
        return socket;
    }

    public DataInputStream getDis() {
        return dis;
    }

    public DataOutputStream getDos() {
        return dos;
    }
}
