package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.MainView;

import java.net.Socket;

public class ClientEnviroment {

    private static ClientEnviroment instance;
    private MainView mainView;
    private Socket socket;

    private ClientEnviroment(){}

    public static synchronized ClientEnviroment instance(){
        if(instance == null){
            instance = new ClientEnviroment();
        }
        return instance;
    }
    public void setMainView(MainView view){
        mainView = view;
    }

    public MainView getMainView(){
        return mainView;
    }

    public void setSocket(Socket s){
        this.socket = s;
    }

    public Socket getSocket(){
        return socket;
    }
}
