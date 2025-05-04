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
