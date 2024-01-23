package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.common.Result;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.Objects;

public class SelectChatRoomController implements MyView{
    @FXML
    private Label notificationLb;

    @FXML
    private TextField numberTf;

    @FXML
    private Button selectBtn;

    @FXML
    void onSelectBtnClicked(MouseEvent event) {
        String num = numberTf.getText();
        if(num == null || num.isBlank()){
            notificationLb.setText("invalid number");
        }

        Result<Boolean, String> res = ClientEnviroment.instance().getMainView().changeScene(new SearchChatController(num));
        if(res.isErr()){
            notificationLb.setText("invalid number");
        }
    }

    @Override
    public Result<Parent, String> getAsNode() {
        try {
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("select-chat-room.fxml")));
            return node != null ? Result.ok(node) : Result.err("Created node was null");
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }
}
