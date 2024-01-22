package at.fhtechnikumwien.ode.client.controls;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ChatTextControl {
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
}
