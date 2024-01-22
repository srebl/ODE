package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.common.Result;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.Objects;

public class MainViewController implements MyView {

    public MainViewController(){
        /*FXMLLoader load = new FXMLLoader(getClass().getClassLoader().getResource("main-view.fxml"));
        load.setRoot(this);
        load.setController(this);
        try {
            //System.out.println("Hello from MainViewController");
            load.load();
        }catch(IOException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public Result<Parent, String> getAsNode() {
        try {
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("main-view.fxml")));
            return node != null ? Result.ok(node) : Result.err("Created node was null");
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }
}
