module at.fhtechnikumwien.ode {
    requires javafx.controls;
    requires javafx.fxml;


    opens at.fhtechnikumwien.ode to javafx.fxml;
    exports at.fhtechnikumwien.ode;
}