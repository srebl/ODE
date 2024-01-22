package at.fhtechnikumwien.ode.service;

public class ServerEnviroment {
    private static ServerEnviroment instance;
    private ServerEnviroment(){}

    public static synchronized ServerEnviroment instance(){
        if(instance == null){
            instance = new ServerEnviroment();
        }
        return instance;
    }
}
