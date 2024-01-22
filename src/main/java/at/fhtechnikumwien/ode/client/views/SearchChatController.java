package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.client.controls.ChatTextControl;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.TextMessage;
import at.fhtechnikumwien.ode.database.Finder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ModifiableObservableListBase;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.util.ArrayList;
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
    private ListView<String> chatLv;

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

    private ObservableList<String> list = FXCollections.observableArrayList();

    @FXML
    void onSearchBtnClick(MouseEvent event) {
        chatLv.setItems(list);
        list.clear();
        final String str = searchTf.getText();

        Runnable runnable = () -> {
            String blub = "";
            final ArrayList<String> msgList = new ArrayList<>();
            if(str == null || str.isBlank())
            {
                blub = "error: search text empty";
                return;
            } else {
                Finder finder = ClientEnviroment.instance().getFinder();
                List<TextMessage> lst = finder.findByText(from, to, str.trim());
                for (TextMessage item : lst){
                    String tmp = "from: " + item.from + " msg: " + item.msg;
                    msgList.add(tmp);
                }
            }

            final String tmp = blub;
            Platform.runLater(() -> {
                list.clear();
                list.addAll(msgList);
                searchLb.setText(tmp);
            });
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }


    @Override
    public Result<Parent, String> getAsNode() {
        try {
            Parent node = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("search-chat.fxml")));
            return node != null ? Result.ok(node) : Result.err("Created node was null");
        } catch (IOException e) {
            return Result.err(e.getMessage());
        }
    }
}
