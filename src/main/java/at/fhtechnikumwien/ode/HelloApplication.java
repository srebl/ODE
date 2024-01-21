package at.fhtechnikumwien.ode;

import at.fhtechnikumwien.ode.client.views.ClientEnviroment;
import at.fhtechnikumwien.ode.client.views.LoginViewController;
import at.fhtechnikumwien.ode.client.views.MainViewController;
import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.Result;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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