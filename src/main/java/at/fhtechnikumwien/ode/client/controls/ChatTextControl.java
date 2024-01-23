package at.fhtechnikumwien.ode.client.controls;

import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.ChatMessage;
import at.fhtechnikumwien.ode.common.messages.TextMessage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;

import java.io.IOException;

public class ChatTextControl extends ListCell<ChatMessage<?>> implements MyView {

    @FXML
    private Label textLb;
    @FXML
    private GridPane wrapperGrid;

    private boolean isOwnMsg = false;

    private final Node graphic ;

    public ChatTextControl() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("chat-text-control.fxml"));
        this.graphic = fxmlLoader.load();
        /*fxmlLoader.setLocation(getClass().getResource("chat-text-control.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }*/
    }

    @Override
    protected void updateItem(ChatMessage<?> item, boolean empty) {
        if(empty || item == null){
            setGraphic(null);
        } else if (item instanceof TextMessage textMsg){
            setText(textMsg.from + ": " +textMsg.msg);
        }
        setGraphic(graphic);
    }

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

    @Override
    public Result<Parent, String> getAsNode() {
        return null;
    }
}
