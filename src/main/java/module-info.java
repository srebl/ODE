module at.fhtechnikumwien.ode {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;

    opens at.fhtechnikumwien.ode to javafx.fxml;
    opens at.fhtechnikumwien.ode.client.views to javafx.fxml;
    exports at.fhtechnikumwien.ode;

    exports at.fhtechnikumwien.ode.common;
    exports at.fhtechnikumwien.ode.common.messages;

    opens at.fhtechnikumwien.ode.client to javafx.fxml;
    opens at.fhtechnikumwien.ode.common to javafx.fxml;
}