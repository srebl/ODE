package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.MyLogger;
import at.fhtechnikumwien.ode.common.ResponseType;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.LogInMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.MessageParser;
import at.fhtechnikumwien.ode.common.messages.ResponseMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
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
        MyLogger logger = Enviroment.instance().getLogger();
        String number = numberTf.getText();
        if(number == null || number.isBlank()){
            messageLb.setText("number is empty.");
        }

        try{
            Socket socket = ClientEnviroment.instance().getSocket();
            if(socket == null){
                String serverAddr = Enviroment.instance().getServerAddress();
                int port = Enviroment.instance().getServerPort();
                socket = new Socket(serverAddr, port);
                ClientEnviroment.instance().setSocket(socket);
            }
            LogInMessage msg = new LogInMessage(number);
            msg.number = number;
            MessageParser.send(null, msg);
            Message<?> rsp_msg = MessageParser.receive(ClientEnviroment.instance().getDis());
            if(rsp_msg instanceof ResponseMessage rsp){
                if(rsp.type == ResponseType.ACK){
                    ClientEnviroment.instance().setLoggedin(true);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void onLoginBtnPressed(KeyEvent event) {

    }
}
