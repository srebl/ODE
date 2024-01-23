package at.fhtechnikumwien.ode.client;

import at.fhtechnikumwien.ode.MainView;
import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.messages.ChatMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.database.Finder;
import javafx.collections.ObservableList;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;

public class ClientEnviroment {

    private static ClientEnviroment instance;
    private MainView mainView;
    private Finder finder;
    private final HashMap<String, ObservableList<ChatMessage<?>>> chatHashMap = new HashMap<>();

    private String number;

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

    public Finder getFinder() {
        return finder;
    }

    public void setFinder(Finder finder) {
        this.finder = finder;
    }

    public HashMap<String, ObservableList<ChatMessage<?>>> getChatHashMap() {
        return chatHashMap;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
