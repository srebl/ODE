package at.fhtechnikumwien.ode;

import at.fhtechnikumwien.ode.client.views.MyView;
import at.fhtechnikumwien.ode.common.Result;
import javafx.scene.Parent;

public interface MainView {
    Result<Boolean, String> changeScene(MyView view);
}
