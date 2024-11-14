package spellapp.spellcheck;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @author      James Hutchins
 * @author      Michelle Bourdon
 * @author      Jessica Kerr
 * @author      Laila El attar
 * @author      Nouran Sakr
 * @version     1.0
 * @since       0.0
 * The Main class serves as the entry point for the Spellchecker application
 */
public class Main extends Application {
    /**
     * The main entry point for the application.
     * @param stage The primary stage for the application.
     * @throws IOException If an error occurs while loading the FXML file.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("FileChooser.fxml"));
        Parent root = loader.load();

        FileController fileController = loader.getController();
        fileController.setPrimaryStage(stage);
        
        Scene scene = new Scene(root, 800, 500);
        scene.getStylesheets().add(getClass().getResource("/spellapp/spellcheck/stylesheet.css").toExternalForm());
        stage.setTitle("Spellchecker");
        stage.setScene(scene);
        stage.show();
        //call a function in main scne controller to populate the text area
    }
    
    /**
     * The main method launches the JavaFX application.
     * @param args Command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
