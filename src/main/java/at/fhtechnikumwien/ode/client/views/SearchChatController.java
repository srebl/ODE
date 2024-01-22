package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.client.controls.ChatTextControl;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.TextMessage;
import at.fhtechnikumwien.ode.database.Finder;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class SearchChatController implements MyView{

    @FXML
    private ListView<?> chatLv;

    @FXML
    private Button searchBtn;

    @FXML
    private Label searchLb;

    @FXML
    private TextField searchTf;

    @FXML
    private Label statusLb;

    //ObservableList<Text>

    private String from = "1234";
    private String to = "4321";

    private ObservableList<ChatTextControl> list;

    @FXML
    void onSearchBtnClick(MouseEvent event) {
        String str = searchTf.getText();
        if(str == null || str.isBlank())
        {
            statusLb.setText("error: search text empty");
            return;
        }

        Finder finder = ClientEnviroment.instance().getFinder();
        List<TextMessage> lst = finder.findByText(from, to, str.trim());
        list.clear();
        for (var item : lst){
            ChatTextControl control = new ChatTextControl();
            control.setText(item.msg);
            control.setOwnMsg(item.from.equals(from));
            list.add(control);
        }
    }


    @Override
    public Result<Parent, String> getAsNode() {
        try {
            var one = getClass().getResource("search-chat.fxml");
            var two = Objects.requireNonNull(one);
            var three = FXMLLoader.load(two);
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-chat.fxml")));
            return node != null ? Result.ok(node) : Result.err("Created node was null");
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }
}
