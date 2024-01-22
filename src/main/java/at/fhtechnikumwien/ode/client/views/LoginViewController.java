package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.HelloApplication;
import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.common.*;
import at.fhtechnikumwien.ode.common.messages.LogInMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.MessageParser;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;

public class LoginViewController implements MyView {

    @FXML
    private Label instructionLb;

    @FXML
    private Button loginBtn;

    @FXML
    private Label messageLb;

    @FXML
    private TextField numberTf;

    @Override
    public Result<Parent, String> getAsNode() {
        try {
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("login-view.fxml")));
            return node != null ? Result.ok(node) : Result.err("Created node was null");
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }

    @FXML
    void onLoginBtnClicked(MouseEvent event) {
        login();
    }

    @FXML
    void onLoginBtnPressed(KeyEvent event) {
        login();
    }

    private void login(){
        Runnable runnable = () -> {
            String number = numberTf.getText();
            String labelText = "";


            if(number == null || number.isBlank()){
                labelText = "labelTextnumber is empty.";
            }

            var res = HelloApplication.initConnection();
            if (res.isErr()){
                labelText = res.getErr();
            }

            SocketHandler socketHandler = Enviroment.instance().getSocketHandler();

            LogInMessage msg = new LogInMessage(number);
            msg.number = number;
            Result<Message<?>, String> r_sock = socketHandler.sendMsgWithAck(msg);
            if(r_sock.isErr()){
                labelText = r_sock.getErr();
            }

            if(r_sock.unwrap() instanceof ResponseMessage rsp){
                if(rsp.type == ResponseType.NACK){
                    labelText = "server refused login";
                }

                ClientEnviroment.instance().setLoggedin(true);
                var search = new SearchChatController();
                var mainView = ClientEnviroment.instance().getMainView();
                mainView.changeScene(search);
            }

            final String tmp = labelText;
            Platform.runLater(() -> {
                messageLb.setText(tmp);
            });
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }
}
