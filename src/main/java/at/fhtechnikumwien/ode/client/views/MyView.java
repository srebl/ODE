/*package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.MainView;
import at.fhtechnikumwien.ode.common.Result;
import javafx.scene.Parent;

public interface MyView {
    Result<Parent, String> getAsNode();
}
*/
package at.fhtechnikumwien.ode.client.views;

import javafx.scene.Parent;

import java.util.List;

public interface MyView {
    void setChatMessages(List<String> chatMessages);
    void addMessage(String message);
}

// Note: Your existing MainViewController class should now implement the MyView interface
/*public class MainViewController implements MyView {

    private final String fxmlPath;
    private final List<String> chatMessages; // In-memory list for message persistence

    public MainViewController(String fxmlPath, List<String> chatMessages) {
        this.fxmlPath = fxmlPath;
        this.chatMessages = chatMessages;
    }
*/
    public Result<Parent, String> loadView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent parent = fxmlLoader.load();
            MyView chatController = fxmlLoader.getController();

            // Pass the chat messages to the controller
            chatController.setChatMessages(chatMessages);

            return Result.ok(parent);
        } catch (IOException e) {
            return Result.err("Error loading FXML: " + e.getMessage());
        }
    }

    // Implementation of methods from the MyView interface
    @Override
    public void setChatMessages(List<String> chatMessages) {
        this.chatMessages.clear();
        this.chatMessages.addAll(chatMessages);
    }

    @Override
    public void addMessage(String message) {
        chatMessages.add(message);
    }
}
