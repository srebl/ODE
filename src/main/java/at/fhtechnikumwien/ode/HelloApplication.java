package at.fhtechnikumwien.ode;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.client.views.LoginViewController;
import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.MyLogger;
import at.fhtechnikumwien.ode.common.Result;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application implements MainView {

    private Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        Enviroment.instance().setLogger(new MyLogger("nope"));
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
        launch();
    }

    public Result<Boolean, String> changeScene(MyView view) {
        //Parent pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxml)));
        Result<Parent, String> r_node= view.getAsNode();
        if(r_node.isErr()){
            return Result.err(r_node.getErr());
        }
        primaryStage.getScene().setRoot(r_node.unwrap());
        return Result.ok(true);
    }
}