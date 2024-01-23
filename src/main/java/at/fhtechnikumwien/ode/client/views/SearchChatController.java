package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.client.ClientEnviroment;
import at.fhtechnikumwien.ode.common.Enviroment;
import at.fhtechnikumwien.ode.common.Result;
import at.fhtechnikumwien.ode.common.messages.ChatMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.TextMessage;
import at.fhtechnikumwien.ode.database.Finder;
import at.fhtechnikumwien.ode.service.Client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

public class SearchChatController implements MyView{

    @FXML
    private ListView<ChatMessage<?>> chatLv;

    @FXML
    private Button searchBtn;

    @FXML
    private Label searchLb;

    @FXML
    private TextField searchTf;

    @FXML
    private Label statusLb;

    @FXML
    private Button sendBtn;

    private String to;

    private FilteredList<ChatMessage<?>> list;

    public void initialize(){
        ObservableList<ChatMessage<?>> messages = ClientEnviroment.instance().getChatHashMap().get(to);
        if(messages == null){
            messages = FXCollections.observableArrayList();
            ClientEnviroment.instance().getChatHashMap().put(to, messages);
        }

        list = new FilteredList<>(messages, x -> true);

        chatLv.setCellFactory(msg -> {
            TextFieldListCell<ChatMessage<?>> cell = new TextFieldListCell<ChatMessage<?>>();
            cell.setConverter(new StringConverter<ChatMessage<?>>() {
                @Override
                public String toString(ChatMessage<?> object) {
                    return msgToString(object);
                }

                @Override
                public ChatMessage<?> fromString(String string) {
                    return null;
                }
            });

            return cell;
        });


    }

    public SearchChatController(){}

    public SearchChatController(String to){
        this.to = to;
    }

    @FXML
    void onSearchBtnClick(MouseEvent event) {
        chatLv.setItems(list);
        String str = searchTf.getText();
        if(str == null || str.isBlank())
        {
            list.setPredicate(msg -> true);
            return;
        }

        Runnable runnable = () -> {
            /*final ArrayList<String> msgList = new ArrayList<>();
            Finder finder = ClientEnviroment.instance().getFinder();
            List<TextMessage> lst = finder.findByText(ClientEnviroment.instance().getNumber(), to, str.trim());
            for (TextMessage item : list){
                String tmp = "from: " + item.from + " msg: " + item.msg;
                msgList.add(tmp);
            }*/

            Platform.runLater(() -> {
                list.setPredicate(a -> compareMsg(a, str));
            });
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

    private boolean compareMsg(ChatMessage<?> a, String search){
        if(a instanceof TextMessage aa){
            return aa.msg.toLowerCase().contains(search);
        }
        return false;
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

    @FXML
    void onSendBtnClick(MouseEvent event) {
        chatLv.setItems(list);
        String str = searchTf.getText();
        if(str == null || str.isBlank())
        {
            statusLb.setText("no valid text provided");
            return;
        }

        Runnable runnable = () -> {
            TextMessage msg = new TextMessage(ClientEnviroment.instance().getNumber(), to, str);
            final Result<Message<?>, String> r_send = Enviroment.instance().getSocketHandler().sendMsgWithAck(msg);

            Platform.runLater(() -> {
                if(r_send.isOk()){
                    var messages = FXCollections.observableArrayList();
                    messages.add(msg);
                } else {
                    statusLb.setText(r_send.getErr());
                }
            });
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }
    public void setTo(String to) {
        this.to = to;
    }

    private String msgToString(ChatMessage msg){
        if(msg instanceof TextMessage textMsg){
            return "from: " + textMsg.from + " msg: " + textMsg.msg;
        }

        return "from: " + msg.from + " unsupported message.";
    }
}
