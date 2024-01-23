package at.fhtechnikumwien.ode.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MyLogger {
    private String path;
    private File file;
    public MyLogger(String filePath){
        this.path = filePath;
    }

    public void logg(String msg){
        System.out.println(msg);

        /*try(var stream = new BufferedWriter(new FileWriter(file))){
            stream.write(msg);
        }catch (Exception e){}*/
    }
}
