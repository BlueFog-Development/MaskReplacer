package eu.bluefogdev.maskreplacer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.prefs.Preferences;

public class EditorController {

    private static final String LAST_DIRECTORY_KEY = "lastDirectory";
    private final Preferences preferences = Preferences.userNodeForPackage(this.getClass());

    @FXML
    public TextField searchMaskField;
    @FXML
    private TextField filePathField;
    @FXML
    private TextField startNumberField;
    @FXML
    private Button selectFileButton;
    @FXML
    private Button processFileButton;
    @FXML
    private Button cancelButton;

    private Stage stage;

    @FXML
    private void initialize() {
        selectFileButton.setOnAction(event -> selectFile());
        processFileButton.setOnAction(event -> processFile());
        cancelButton.setOnAction(event -> stage.close());
    }

    private void selectFile() {
        FileChooser fileChooser = new FileChooser();
        String lastDirectory = preferences.get(LAST_DIRECTORY_KEY, "");
        if (!lastDirectory.isEmpty()) {
            fileChooser.setInitialDirectory(new File(lastDirectory));
        }
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Textdateien", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
            preferences.put(LAST_DIRECTORY_KEY, selectedFile.getParent());
        }
    }

    private void processFile() {
        try {
            String filePath = filePathField.getText();
            if (filePath.isEmpty()) {
                showAlert("Fehler", "Bitte wählen Sie eine Datei aus.");
                return;
            }

            String startNumberStr = startNumberField.getText();
            int startNumber;
            try {
                startNumber = Integer.parseInt(startNumberStr);
            } catch (NumberFormatException e) {
                showAlert("Fehler", "Bitte geben Sie eine gültige Startnummer ein.");
                return;
            }

            String searchMask = searchMaskField.getText();
            if (searchMask.isEmpty()) {
                showAlert("Fehler", "Die Suchmaske darf nicht leer sein.");
                return;
            }

            Path path = Paths.get(filePath);
            if (!Files.exists(path)) {
                showAlert("Fehler", "Die ausgewählte Datei existiert nicht.");
                return;
            }

            List<String> lines = Files.readAllLines(path);
            int currentNumber = startNumber;
            boolean isNoMatch = true;

            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).startsWith(searchMask)) {
                    lines.set(i, ":28:" + currentNumber);
                    currentNumber++;
                    isNoMatch = false;
                }
            }

            if (isNoMatch) {
                showAlert("Fehler", "Die ausgewählte Datei enthält nicht das Suchmuster \":28:?\".");
                clearFields();
                return;
            }

            String newFilePath = filePath.substring(0, filePath.lastIndexOf('.')) + "_neu" + filePath.substring(filePath.lastIndexOf('.'));
            Files.write(Paths.get(newFilePath), lines);

            showAlertTimed("Erfolg", "\nDie Datei wurde erfolgreich bearbeitet\n\nAls " + newFilePath + " gespeichert.", 3);
            clearFields();
        } catch (IOException ex) {
            showAlert("Fehler", "Fehler beim Verarbeiten der Datei.");
            clearFields();
        }
    }

    private void showAlert(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showAlertTimed(String title, String content, int timerSeconds) {
        Stage timerAlertStage = new Stage();
        timerAlertStage.initModality(Modality.APPLICATION_MODAL);
        timerAlertStage.setTitle(title);

        VBox vbox = new VBox(new Label(content));
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20, 20, 20, 20));

        Scene scene = new Scene(vbox);
        timerAlertStage.setScene(scene);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(timerSeconds),
                event -> timerAlertStage.close()
        ));
        timeline.setCycleCount(1);
        timeline.play();

        timerAlertStage.show();
    }


    private void clearFields() {
        filePathField.setText("");
        startNumberField.setText("");
        searchMaskField.setText(":28:0");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
