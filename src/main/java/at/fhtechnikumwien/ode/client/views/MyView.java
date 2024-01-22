package at.fhtechnikumwien.ode.client.views;

import at.fhtechnikumwien.ode.MainView;
import at.fhtechnikumwien.ode.common.Result;
import javafx.scene.Parent;

public interface MyView {
    Result<Parent, String> getAsNode();
}
