package at.fhtechnikumwien.ode.client.controls;

import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.Result;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ChatTextControl implements MyView {
    public ChatTextControl(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-text-control.fxml"));
        //fxmlLoader.setRoot(this);
        //fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    private Label textLb;
    @FXML
    private GridPane wrapperGrid;

    private boolean isOwnMsg = false;

    public boolean isOwnMsg() {
        return isOwnMsg;
    }

    public void setOwnMsg(boolean ownMsg) {
        isOwnMsg = ownMsg;
        if(isOwnMsg){
            GridPane.setRowIndex(textLb, 1);
            textLb.setAlignment(Pos.CENTER_RIGHT);
        } else {
            GridPane.setRowIndex(textLb, 0);
            textLb.setAlignment(Pos.CENTER_LEFT);
        }
    }

    public void setText(String text){
        textLb.setText(text);
    }

    @Override
    public Result<Parent, String> getAsNode() {
        return null;
    }
}
