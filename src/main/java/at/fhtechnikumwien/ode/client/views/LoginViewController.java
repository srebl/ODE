package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.LogInMessage;
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
        String number = numberTf.getText();
        if(number == null || number.isBlank()){
            messageLb.setText("number is empty.");
        }

        try{
            //Socket socket = new Socket("localhost", 4711);
            //ClientEnviroment.instance().setSocket(socket);
            LogInMessage msg = new LogInMessage(number);
            ObjectMapper mapper = msg.getMapper();
            /*PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().build();
            mapper.activateDefaultTyping(ptv); // default to using DefaultTyping.OBJECT_AND_NON_CONCRETE
            mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);*/

            String data_str = mapper.writeValueAsString(msg);
            System.out.println(data_str);
            byte[] data_byte = mapper.writeValueAsBytes(msg);
            //var dos = socket.getOutputStream();
            //dos.write(data_byte);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void onLoginBtnPressed(KeyEvent event) {

    }
}
