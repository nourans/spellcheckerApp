package spellapp.spellcheck;

import javafx.scene.layout.BorderPane;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author      James Hutchins
 * @author      Michelle Bourdon
 * @author      Jessica Kerr
 * @author      Laila El attar
 * @author      Nouran Sakr
 * @version     1.0
 * @since       0.0
 * The HelpController class handles actions related to the help functionality in the application.
 */
public class HelpController {
    @FXML
    private BorderPane help;

    /**
     * Handles the "Close Help" button click event to close the help window.
     * @param event The ActionEvent triggered by the "Close Help" button.
     */
    @FXML
    void CloseHelp(ActionEvent event) {
        help.getScene().getWindow().hide();
    }
}
