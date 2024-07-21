package eu.bluefogdev.maskreplacer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
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

            Parent root = fxmlLoader.load();

            EditorController controller = fxmlLoader.getController();
            controller.setStage(mainStage);

            Image icon = new Image(Objects.requireNonNull(EditorApplication.class.getResourceAsStream("favicon.ico")));
            mainStage.getIcons().add(icon);

            mainStage.setResizable(false);
            mainStage.setTitle("Datei Bearbeiter");
            mainStage.setScene(new Scene(root, 440, 170));
            mainStage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load editor view\n", e);
            mainStage.close();
        }
    }
}
