package at.fhtechnikumwien.ode.client;

import at.fhtechnikumwien.ode.MainView;
import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.database.Finder;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientEnviroment {

    private static ClientEnviroment instance;
    private MainView mainView;
    private boolean isLoggedin;
    private Finder finder;


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

    public boolean isLoggedin() {
        return isLoggedin;
    }

    public void setLoggedin(boolean loggedin) {
        isLoggedin = loggedin;
    }

    public Finder getFinder() {
        return finder;
    }

    public void setFinder(Finder finder) {
        this.finder = finder;
    }
}
