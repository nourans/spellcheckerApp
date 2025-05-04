package spellapp.spellcheck;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Popup;
import javafx.scene.control.Button;
import java.io.IOException;

public class UserDictionaryController {
    private Scene preScene;         // To remember scene of Main window, to go back

    public void setPreScene(Scene preScene) {
        this.preScene = preScene;
    }

    @FXML
    private Button homeButton;

    @FXML
    private TextArea textArea;

    @FXML
    private Label mainLabel;

    private Stage primaryStage;


    /**
     * Handles the action when the home button is clicked.
     *
     * @param event The ActionEvent triggered by the home button.
     */
    @FXML
    private void handleHome(ActionEvent event) {
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(preScene);
            stage.show();
    }

    /**
     * Opens the help popup when the help button is clicked.
     *
     * @param event The ActionEvent triggered by the help button.
     */
    @FXML
    void openHelp(ActionEvent event) {
        try {
            // Load the Help.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Help.fxml"));
            Parent root = loader.load();

            // Create a new popup
            javafx.stage.Popup popup = new Popup();
            popup.getContent().add(root);

            // Set the owner stage
            Stage ownerStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            popup.show(ownerStage);

            // Show some text


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
