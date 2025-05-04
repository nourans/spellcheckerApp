package spellapp.spellcheck;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExitController {
    private Stage primaryStage;
    public String content = "";

    // Injected via FXML if defined in your FXML file
    @FXML
    private Label label;
    
    /**
     * Handles the "Exit" button click event to exit the application.
     * @param event is the event of the user clicking on the exit button
     */
    @FXML
    void exitExit(ActionEvent event) {
        Platform.exit();
    }

    /**
     * Handles the "Save" button click event to save data and exit the application.
     * This method performs the following steps:
     *     Brings up the Save popup.
     *     Allows the user to set the filename and path.
     *     Performs the save operation.
     *     Exits the application.
     * @param event The ActionEvent triggered by the "Save" button.
     */

    @FXML
    void exitSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Assuming primaryStage is the primary stage of your application
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file != null) {
            saveToFile(file);
            Platform.exit();
        }
    }
        /**
         * Saves the content to the specified file.
         * @param file The File to which the content is saved.
         */
    private void saveToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error dialog to the user
        }
    }
    /**
     * Sets the primary stage for the controller.
     * @param primaryStage The primary stage of the JavaFX application.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }
}
