package at.fhtechnikumwien.ode;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.client.ClientMessageHandler;
import at.fhtechnikumwien.ode.client.views.LoginViewController;
import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.*;
import at.fhtechnikumwien.ode.common.messages.LogInMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.MessageParser;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class HelloApplication extends Application implements MainView {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        ClientEnviroment.instance().setMainView(this);

        this.primaryStage = stage;
        LoginViewController login = new LoginViewController();
        Parent node = login.getAsNode().unwrap();
        //FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        //Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene = new Scene(node, 400, 300);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        try {
            Enviroment.instance().setLogger(new MyLogger("nope"));

            launch();
        } catch (Exception e){
            SocketHandler handler = Enviroment.instance().getSocketHandler();
            if (handler != null){
                handler.closeSocket();
            }
        }
    }

    public Result<Boolean, String> changeScene(MyView view) {
        //Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Result<Parent, String> r_node= view.getAsNode();
        if(r_node.isErr()){
            Enviroment.logg("changeScene: could not get change scene.");
            return Result.err(r_node.getErr());
        }
        var blub = r_node.unwrap();
        primaryStage.getScene().setRoot(r_node.unwrap());
        return Result.ok(true);
    }

    public static Result<Boolean, String> initConnection(){
        try{
            SocketHandler socketHandler = Enviroment.instance().getSocketHandler();
            if(socketHandler == null){
                String serverAddr = "localhost";
                int port = 4711;
                Socket socket = new Socket(serverAddr, port);
                MessageHandler msgHandler = new ClientMessageHandler();
                socketHandler = new SocketHandler(socket, msgHandler);
                Enviroment.instance().setSocketHandler(socketHandler);
            }
            return Result.ok(true);
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }
}