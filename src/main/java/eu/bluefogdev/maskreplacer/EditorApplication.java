package eu.bluefogdev.maskreplacer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditorApplication extends Application {

    private static final Logger logger = Logger.getLogger(EditorApplication.class.getName());

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage mainStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(EditorApplication.class.getResource("editor-view.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 320, 240);
            mainStage.setTitle("Hello!");
            mainStage.setScene(scene);
            mainStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load editor view\n", e);
            mainStage.close();
        }
    }
}
