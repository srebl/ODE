package at.fhtechnikumwien.ode.database;

import at.fhtechnikumwien.ode.common.messages.ChatMessage;
import at.fhtechnikumwien.ode.common.messages.Message;
import at.fhtechnikumwien.ode.common.messages.TextMessage;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Finder {
    public List<Message> findAll(String from, String to){
        return null;
    }

    public List<TextMessage> findByText(String from, String to, String searchStr){
        if(from == null || to == null || searchStr == null
                || from.isBlank() || to.isBlank() || searchStr.isBlank()){
            return new ArrayList<>();
        }

        //we dont hava a db
        List<TextMessage> lst =generateSearchList();
        return lst.stream()
                .filter(x -> matchSearchMessage(from, to, searchStr, x))
                .sorted(Comparator.comparingInt(a -> a.date.getSecond()))
                .toList();
    }

    private List<TextMessage> generateSearchList(){
        return Arrays.asList(new TextMessage[]{
                new TextMessage("1234", "4321", "Some text.", LocalDateTime.now().minusHours(10)),
                new TextMessage("6789", "4321", "wrong text", LocalDateTime.now().minusHours(9)),
                new TextMessage("1234", "4321", "why is this so difficult?.", LocalDateTime.now().minusHours(8)),
                new TextMessage("4321", "6789", "fxml is kinda outdated.", LocalDateTime.now().minusHours(6)),
                new TextMessage("4321", "4321", "fxml is kinda outdated.", LocalDateTime.now().minusHours(5)),
        });
    }

    private boolean matchSearchMessage(String from, String to, String searchStr, TextMessage msg){
        final Pattern pattern = Pattern.compile(Pattern.quote(to), Pattern.CASE_INSENSITIVE);
        return (msg.from.equals(from) && msg.to.equals(to) || msg.from.equals(to) && msg.to.equals(from))
                && pattern.matcher(msg.msg).find();
    }
}
