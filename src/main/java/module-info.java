module eu.bluefogdev.maskreplacer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens eu.bluefogdev.maskreplacer to javafx.fxml;
    exports eu.bluefogdev.maskreplacer;
}