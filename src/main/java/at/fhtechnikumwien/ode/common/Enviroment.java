package at.fhtechnikumwien.ode.common;

public class Enviroment {

    private static Enviroment instance;

    private Enviroment(){}

    private MyLogger myLogger;
    private String serverAddress = "localhost";
    private int serverPort = 4711;

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

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }
}
