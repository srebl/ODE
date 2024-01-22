package at.fhtechnikumwien.ode.common;

public class MyLogger {
    private final String path;
    public MyLogger(String filePath){
        path = filePath;
    }

    public void logg(String msg){
        System.out.println(msg);
    }
}
