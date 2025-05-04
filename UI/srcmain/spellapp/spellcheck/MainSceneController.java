package spellapp.spellcheck;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.scene.Scene;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import Backend.Document;
import Backend.Word_Object;
import javafx.stage.FileChooser;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.fxmisc.richtext.StyleClassedTextArea;

import Backend.Doc_Error;

public class MainSceneController {

    @FXML
    private Pane textAreaContainer;
    private StyleClassedTextArea textArea = new StyleClassedTextArea();
    
    @FXML
    private Label mainLabel;

    //Create a document object
    Document document;
    private ContextMenu contextMenu = new ContextMenu();
    private ContextMenu spellingContextMenu = new ContextMenu();
    private ContextMenu undoContextMenu = new ContextMenu();
    private int currentCaretPosition = 0;

    
    /**
     * Sets the content of the text area.
     * @param content The content to be set in the text area.
     */
    public void setTextAreaContent(String content) {
        textArea.replaceText(content); // Replaces the entire text content

        textArea.setOnMouseClicked(event -> {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            }
            // Additional logic for handling text click, if any
        });
        

        
        
        init_document(textArea); 
    }
    
    /**
     * This function starts at the first click and automatically updates the environment every 10 seconds.
     */
    public void startRepeatedTask() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            Platform.runLater(() -> {
                try {
                    //get text area content
                    String textContent = textArea.getText();
                    //populate the linked list
                    document.populateLinkedList(textContent);
                    //run the document spell check on the newly updated linked list
                    document.run_spell_check();
                    
                    int[] docAnalysisVals = get_doc_analysis();
                    
                    update_doc_analysis_values(docAnalysisVals);
                    
                    //get the doc error values
                    int[] docErrorVals = get_doc_error();
                    
                    //populate the doc error values on the front end
                    update_doc_error_values(docErrorVals);
                    
                    
                    //run the highlight misspelled words function
                    highlightErrors();
                    

                } catch (Exception e) {
                    System.err.println("Error during repeated task: " + e.getMessage());
                    // Handle or log the exception appropriately
                }
            });
        }));
    
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    

    /**
     * init the document object from the text area
     * @param textArea_in which is the text area
     */
    public void init_document(StyleClassedTextArea textArea_in){      
        // Create a new document object
        this.document = new Document(textArea_in.getText());
    }
    
    /**
     * populates the document object and sets the document object 
     * @param document is the document 
     */
    public void setDocument(Document document) {
        // Populate the document object
        document.populateLinkedList(textArea.getText());
    
        // Set the document object
        this.document = document;
    }
    
    /**
     * handleSaveButton handles the action when the user clicks the save button to save the file
     * @param event is the button click event
     */
    @FXML
    private void handleSaveButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            saveSystem(file, textArea.getText());
        }
    }

    /**
     * saveSystem saves the file
     * @param file is the file to be saved
     * @param text is the string to be saved in the file
     */
    private void saveSystem(File file, String text) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * openHelp opens the help button
     * @param event is the button click event
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

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * gets the text from the document
     */
    public String[] parse_text(){
        //get the text from the text area
        String text = textArea.getText();
        //split the text into an array of words
        String[] words = text.split("\\s+");
        
        //delete double spaces
        
        //return the array of words
        return words;
    }

    /**
     * exit handles the exit button which exits the application
     * @param event is the button click event
     */
    @FXML
    public void exit(ActionEvent event) {
        try {
            System.out.println("Exiting...");
            // Load the Help.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Exit.fxml"));
            Parent root = loader.load();

            // Create a new popup
            javafx.stage.Popup popup = new Popup();
            popup.getContent().add(root);

            // Set the owner stage
            Stage ownerStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            //pass the text area content to the exit controller
            ExitController exitController = loader.getController();
            exitController.content = textArea.getText();
            popup.show(ownerStage);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @FXML
    private Label userWords;

    /**
     * handleUserDictionary handles the user dictionary
     * @param event is the button click event
     */
    @FXML
    private void clear_user_dict(ActionEvent event) {
        //clear the user dictionary
        document.clear_user_dict();
        
        userWords.setText("");;
    }
    

    /**
     * gets the doc analysis values
     */
    public int[] get_doc_analysis(){
        //gets the values from the doc analysis object
        //int num_lines = textArea.getParagraphs().size();
        String content = textArea.getText();
        int lines = (int) content.chars().filter(ch -> ch == '\n').count() + 1;
        int[] doc_err_vals = document.get_doc_analysis(lines);

        return doc_err_vals;

    };
    
    /**
     * define fxml values for the doc analysis counts
     */
    @FXML
    private Label characterCountLabel;
    @FXML
    private Label lineCountLabel;
    @FXML
    private Label wordCountLabel;
    @FXML
    private Label Misspellings;
    @FXML
    private Label MissCaps;
    @FXML
    private Label DoubleWords;
    @FXML
    private Label Corrections;
    @FXML
    private Label DoubleWordCorrections;
    @FXML
    private Label corrected_capital;

    /**
     * updates the document error values from an array of values
     * @param values is the int values
     */
    public void update_doc_error_values(int[] values){
        //update the doc error values
        //get the valuesclear_user_dict
        int misspelt_words = values[0];
        int corrected_misspelt_words = values[1];
        int double_words = values[2];
        int corrected_double_words = values[3];
        int capital_errors = values[4];
        int corrected_capital_errors = document.getCorrected_capital_errors();

        //set the values
        Misspellings.setText("Misspellings: " + misspelt_words);
        MissCaps.setText("Capital Errors: " + capital_errors);
        DoubleWords.setText("Double Words: " + double_words);
        Corrections.setText("Corrected Misspellings: " + corrected_misspelt_words);
        DoubleWordCorrections.setText("Corrected Double Words: " + corrected_double_words);
        corrected_capital.setText("Corrected Capital Errors: " + corrected_capital_errors);
        if(document.user_dict_is_null()){
            userWords.setText("");
        }else{
        
        userWords.setText(document.get_user_dict_formatted());
    }
}

    /**
     * updates the doc analysis values and gets value
     * @param counts is the int values
     */
    public void update_doc_analysis_values(int[] counts){
        //update the doc analysis values
        //get the values
        int char_count = counts[0];
        int word_count = counts[1];
        int line_count = counts[2];

        //set the values
        characterCountLabel.setText("Character count: " + char_count);
        lineCountLabel.setText("Line count: " + line_count);
        wordCountLabel.setText("Word count: " + word_count);
    }

    /**
     * get the values from the doc error class via the document class
     */ 
    public int[] get_doc_error(){

        Doc_Error error = document.get_doc_error();

        //get the values
        int misspelt_words = error.getCurrent_misspelt_words();
        int corrected_misspelt_words = error.getCorrected_misspelt_words();
        int double_words = error.getCurrent_double_words();
        int corrected_double_words = error.getCorrected_double_words();
        int capital_errors = error.getCurrent_capital_errors();
        int corrected_capital_errors = error.getCorrected_capital_errors();

        //create an array to store the values
        int[] values = {misspelt_words, corrected_misspelt_words, double_words, corrected_double_words, capital_errors, corrected_capital_errors};

        //return the array
        return values;

            
    };
    

     /**
     * inits the text area and starts the repeated task function.
     */
    @FXML
    public void initialize() {
        // Initialize StyleClassedTextArea
        textArea = new StyleClassedTextArea();
        textAreaContainer.getChildren().add(textArea);
        textArea.prefWidthProperty().bind(textAreaContainer.widthProperty());
        textArea.prefHeightProperty().bind(textAreaContainer.heightProperty());
        startRepeatedTask();
        // Configure the StyleClassedTextArea
        textAreaContainer.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleTextClick(event);
            } else {
                contextMenu.hide();
            }

            // Additional mouse click logic, if needed
        });
    }

    /**
     * When we click on the screen this function is called.
     * @param event is the event when the user clicks with the mouse
     */
    private void handleTextClick(MouseEvent event) {
    
        // Check if the document is initialized
        if (this.document == null) {
            init_document(textArea);
            startRepeatedTask();
        }

        //determine the position of the click from the event value 
        int position = getClickPosition(event);

        //update the wordbuffer indicies
        document.wordBuffer.calculate_indicies();

        // ! maybe update index here (how long does that take!!! )
        //get the word using the document class using the position (index value (carret position))
        Word_Object this_word = document.get_word_in_linked_list(position);

        // Check if the word is misspelled and show suggestions
        if (!this_word.isIs_real_word()) {
            //hide all other context menus
            contextMenu.hide();
            
            showPopupAtTextPosition_spelling(this_word, event.getScreenX(), event.getScreenY());
        }else if(this_word.isIs_double_word_after() || this_word.isIs_double_word_before()){
            //hide all other context menus
            contextMenu.hide();
            
            showPopupAtTextPosition_double_word(this_word, event.getScreenX(), event.getScreenY());
        }else if(this_word.isNeeds_first_capital()){

            String word_base = this_word.getWord();

            String word_capital = word_base.substring(0, 1).toUpperCase() + word_base.substring(1);

            popup_caps(word_capital, this_word, event.getScreenX(), event.getScreenY());


        }else if(this_word.isNeeds_lower_but_first()){

            String word_base = this_word.getWord();
            String word_lower = word_base.toLowerCase();
            //String word_capital = word_lower.substring(0, 1).toUpperCase() + word_lower.substring(1);
            String word_capital = this_word.getWord().toLowerCase();

            popup_caps(word_capital, this_word, event.getScreenX(), event.getScreenY());


        }else if(this_word.isNeeds_lower()){
            
            //provide a reccomendation that shows just the first letter capatalized
            String word_base = this_word.getWord();
            //first set it all to lower case
            String word_lower = word_base.toLowerCase();
            //then set the first letter to upper case
            
            
            popup_caps(word_lower, this_word, event.getScreenX(), event.getScreenY());

        }else if(this_word.isNeeds_lower()){
            //provide a reccomendation that shows the word lowercase
            String word_base = this_word.getWord();
            String word_lower = word_base.toLowerCase();
            
            popup_caps(word_lower, this_word, event.getScreenX(), event.getScreenY());

        }else{
            //explode
        }
    }
    
    /**
     * Displays a context menu with a suggestion for capitalization adjustment.
     * @param adjusted_word The adjusted word with the desired capitalization.
     * @param word          The Word_Object representing the original word.
     * @param x             The x-coordinate of the mouse cursor.
     * @param y             The y-coordinate of the mouse cursor.
     */
    private void popup_caps(String adjusted_word, Word_Object word, double x, double y){
        // Clear existing items and prepare new ones
        contextMenu.getItems().clear();
        // Add new items to the context menu
        contextMenu.getItems().addAll(
            createCustomMenuItem_caps(word, adjusted_word)
        );

        // Show context menu at the mouse cursor's position
        contextMenu.show(textArea, x, y);
    }

    /**
     * Creates a custom menu item for adjusting capitalization in the context menu.
     *
     * @param word          The Word_Object representing the original word.
     * @param adjusted_word The adjusted word with the desired capitalization.
     * @return A CustomMenuItem with a label displaying the adjusted word and an action to implement the capitalization change.
     */
    private CustomMenuItem createCustomMenuItem_caps(Word_Object word, String adjusted_word) {
        
        Label label = new Label(adjusted_word);
        label.setStyle("-fx-text-fill: red;");
        label.setPrefWidth(150);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);


        label.setOnMouseClicked(event -> {
            //delete the text, and the word object in the linked list
            implement_caps(word, adjusted_word);
        });
        return new CustomMenuItem(label,true);
    }
    
    /**
     * Shows a context menu with options for handling double words at the specified text position.
     *
     * @param word The Word_Object representing the double word.
     * @param x    The x-coordinate of the mouse cursor.
     * @param y    The y-coordinate of the mouse cursor.
     */
    private void showPopupAtTextPosition_double_word(Word_Object word, double x, double y){

        contextMenu.getItems().clear();

        if (word.isIs_double_word_after()){
            contextMenu.getItems().addAll(
                createDoubleWordItem_replace(0, word)
                
            );

            contextMenu.show(textArea, x, y);
        }
    }
    /**
     * Creates a CustomMenuItem for replacing a double word with a specific action.
     *
     * @param index The index of the menu item.
     * @param word  The Word_Object representing the double word.
     * @return A CustomMenuItem for handling double word replacement.
     */
    private CustomMenuItem createDoubleWordItem_replace(int index, Word_Object word){
        String text = "Delete Double Word";
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: red;");
        label.setPrefWidth(150);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);


        label.setOnMouseClicked(event -> {
            //delete the text, and the word object in the linked list

            implement_drop_double_word(word);
            document.decrease_current_double_words();
        });

        return new CustomMenuItem(label,true);
    }
    
    /**
     * Creates a CustomMenuItem for replacing a double word with a specific action.
     * @param index The index of the menu item.
     * @param word  The Word_Object representing the double word.
     * @return A CustomMenuItem for handling double word replacement.
     */
    private CustomMenuItem addTouserDictItem(Word_Object word){
        //create a new menu item
        
        //create a new custom menu item
        
        //add a label
        Label label = new Label("Add to user dictionary");
        //add an event listener to the menu item
        label.setPrefWidth(150);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
        label.setOnMouseClicked(event -> {
            
            //get the word
            String word_text = word.getWord();
            //add the word to the user dictionary
            document.add_to_user_dict(word_text);
            
            //set the word object is real word flag
            word.setIs_real_word(true);
            //set the word object modified flag
            word.setModified(true);
            //set the word object suggestion to null
            word.setSuggestions(null);
           

            //hide the context menu
            contextMenu.hide();
        });
        //return the custom menu item
        return new CustomMenuItem(label, true);
    }

    /**
     * Shows a context menu with spelling suggestions and an option to add the word to the user dictionary.
     *
     * @param word The Word_Object representing the misspelled word.
     * @param x    The x-coordinate of the mouse cursor's position.
     * @param y    The y-coordinate of the mouse cursor's position.
     */
    private void showPopupAtTextPosition_spelling(Word_Object word, double x, double y) {
        // Clear existing items and prepare new ones
        contextMenu.getItems().clear();
        String[] suggestions = word.getSuggestions();
        //get the word start and stop indicies
        


        if (suggestions[0] != null) {
            // Add new items to the context menu
            contextMenu.getItems().addAll(
                createCustomMenuItem(0, word),
                createCustomMenuItem(1, word),
                createCustomMenuItem(2, word),
                addTouserDictItem(word) // Updated method name
            );

            // Show context menu at the mouse cursor's position
            contextMenu.show(textArea, x, y);
        }
    }

    
    /**
     * Creates a custom menu item for a spelling suggestion with an event handler to replace the misspelled word.
     * @param index      The index of the suggestion in the array.
     * @param word       The Word_Object representing the misspelled word.
     * @return A CustomMenuItem containing the suggestion as a label and an event handler to replace the word.
     */
    private CustomMenuItem createCustomMenuItem(int index, Word_Object word) {
        String suggestion = word.getSuggestions()[index];
        Label label = new Label(suggestion);
        label.setStyle("-fx-text-fill: red;");
        label.setPrefWidth(150);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setWrapText(true);
    
        label.setOnMouseClicked(event -> {
            // Replace the word in the text editor
            implement_suggestion(word, suggestion);

            //update fix and missplet couts
    
            // Hide the context menu
            contextMenu.hide();
    
            // Additional functionality like re-running spell check can be added here
        });
    
        return new CustomMenuItem(label, false);
    }
    
    /**
     * Implements the capitalization correction for a Word_Object in the text area.
     *
     * @param word          The Word_Object representing the word to be capitalized.
     * @param adjusted_word The adjusted word with proper capitalization.
     */
    private void implement_caps(Word_Object word, String adjusted_word){
        //get the word start and stop indicies
        int startIndex = word.getStart_index();
        int endIndex = word.getEnd_index();

        word.setWord(adjusted_word);

        //replace that text in that range with the word
        textArea.replaceText(startIndex, endIndex, adjusted_word);
        //update the word object
        //word.setIs_real_word(true);
        word.setNeeds_capital(false);
        word.setNeeds_lower_but_first(false);
        word.setNeeds_lower(false);
        word.setNeeds_first_capital(false);
        
        //count the fix
        document.increase_corrected_capital_errors();
    }

       
    
    /**
     * Implements the suggestion correction for a Word_Object in the text area.
     *
     * @param word       The Word_Object representing the misspelled word to be corrected.
     * @param suggestion The suggested correction for the misspelled word.
     */
    private void implement_suggestion(Word_Object word, String suggestion) {
        // Validate word and suggestion
        if (word == null || suggestion == null || suggestion.isEmpty()) {
            return;
        }
    
        // Calculate indices with the original word
        document.wordBuffer.calculate_indicies();
    
        // Get the original word length and start index
        int originalWordLength = word.getWord().length();
        int originalStartIndex = word.getStart_index();
    
        // Replace the word in the linked list
        word.setWord(suggestion);
        word.setIs_real_word(true);
        word.setModified(false);
    
        // Recalculate indices to reflect the new word length
        document.wordBuffer.calculate_indicies();;

        replaceAndInsertWord(textArea, word, suggestion);
    
        
    
        // Update document statistics
        document.decrease_current_misspelt_words();
    }

    /**
     * Replaces a Word_Object in the linked list and inserts the corrected word in the text area.
     * @param textArea    The text area where the replacement will occur.
     * @param word        The Word_Object to be replaced.
     * @param replacement The corrected word to be inserted.
     */
    private void replaceAndInsertWord(StyleClassedTextArea textArea, Word_Object word, String replacement) {
        
        //getting punctuation
        if(word.getEndsWithPunctuation()){
            if(word.get_punctuation_index() == 1){
                replacement = replacement + ".";
            }else if(word.get_punctuation_index() == 2){
                replacement = replacement + "?";
            }else if(word.get_punctuation_index() == 3){
                replacement = replacement + "!";
            }else if(word.get_punctuation_index() == 4){
                replacement = replacement + ",";
            }else if(word.get_punctuation_index() == 5){
                replacement = replacement + ";";
            }
            
        }

        //first let's match the word in the text area
        //get the word start and stop indicies
        int startIndex = word.getStart_index();
        int endIndex = word.getEnd_index();

        int middle = (startIndex + endIndex) / 2;
        int start = findWordStart(textArea.getText(), middle);
        int end = findWordEnd(textArea.getText(), middle);


        //get the text before and after the word
        String[] texts = {textArea.getText(0, start), textArea.getText(end, textArea.getLength())};
        String[] textold = {textArea.getText(0, textArea.getLength())};
        // now join the three strings together
        String text = "";
        if(texts[1].equals(textold[0])){
            //means this is the last word in the file.
            text = texts[0] + replacement;
        }else{  
            text = texts[0] + replacement + texts[1];
        }
        //now replace the text area
        textArea.replaceText(text);

        //
        
        
    }
    
    /**
     * Finds the start index of the word containing the specified middle index in the given text.
     * The word is delimited by spaces or newline characters.
     *
     * @param text   The text in which to find the word.
     * @param middle The middle index around which to find the word start.
     * @return The start index of the word containing the middle index.
     */
    private static int findWordStart(String text, int middle) {

        //get the char at the middle index
        char middle_char = text.charAt(middle);
        while(middle_char != ' ' && middle_char != '\n'){
            middle--;
            if(middle <= 0){
                return 0;
            }
            middle_char = text.charAt(middle);
        }
        return middle + 1;
    }
    
    /**
     * Finds the end index of the word containing the specified middle index in the given text.
     * The word is delimited by spaces or newline characters.
     *
     * @param text   The text in which to find the word.
     * @param middle The middle index around which to find the word end.
     * @return The end index of the word containing the middle index.
     */
    private static int findWordEnd(String text, int middle) {
        char middle_char = text.charAt(middle);
        while(middle_char != ' ' && middle_char != '\n'){
            middle++;
            if(middle >= text.length() - 1){
                return 0;
            }
            middle_char = text.charAt(middle);
        }
        return middle;
    }
    

    /**
     * Drops a double word by removing it from the linked list and updating the text area.
     *
     * @param word The Word_Object representing the double word to be dropped.
     */
    private void implement_drop_double_word(Word_Object word){
        //get the word start and stop indicies
        //update the words' indicies


        int startIndex = (word.getPrev_node() != null) ? word.getPrev_node().getEnd_index() + 1 : 0;
        int endIndex = (word.getNext_node() != null) ? word.getNext_node().getStart_index() : textArea.getLength();

        textArea.replaceText(startIndex, endIndex, "");

        //remove the word

        document.wordBuffer.removeWord(word);

    }
    
    /**
     * Gets the current caret position in the text area and invokes the click splash.
     *
     * @param event The MouseEvent associated with the click.
     * @return The caret position in the text area.
     */
    private int getClickPosition(MouseEvent event) {
        // Return the current caret position in the text area
        //update index
        document.wordBuffer.calculate_indicies();
        invoke_click_splash(event);
        return textArea.getCaretPosition();
    }
    /**
     * Invokes actions related to a mouse click event, specifically for word objects before and after
     * the clicked word at the current caret position in the text area.
     *
     * @param event The MouseEvent representing the mouse click event.
     */
    private void invoke_click_splash(MouseEvent event){
        
        //get the click position
        int position = textArea.getCaretPosition();

        //get the word object at this position
        Word_Object this_word = document.wordBuffer.getWordAtCaretPosition(position);

        
        if(this_word == null){
            return;
        }

        Word_Object current = this_word;
        Word_Object [] before = {null, null, null};
        Word_Object [] after = {null, null, null}; 
        int i = 0;
        current = this_word.getNext_node();
        while(current != null && i < 2){

            after[i] = current;
            i++;
            current = current.getNext_node();
        }
        i = 0;
        current = this_word;
        current = current.getPrev_node();
        while(current != null && i < 2){
            before[i] = current;
            i++;
            current = current.getPrev_node();

        }

        //run checks on the two arrays of words
        for(int j = 0; j<2; j++){
            //mark the word objects as modified
            if(before[j] != null){
                before[j].setModified(true);
                //check if it has any kind of capitalization error
                 
            }
            if(after[j] != null){
                after[j].setModified(true);
                
            }
        }

    }
    
    /**
     * Highlights errors in the text area based on the properties of words in the document.
     * It iterates through the words in the text, retrieves corresponding Word_Object instances,
     * determines the appropriate style class, and applies it to the corresponding text range.
     * Words with null or invalid Word_Object instances are skipped.
     */
    public void highlightErrors() {

        Word_Object current = document.wordBuffer.getHead();

        //update the word indicies
        document.wordBuffer.calculate_indicies();

        while(current != null){
            
                //get the word start and stop indicies
                int startIndex = current.getStart_index();
                int endIndex = current.getEnd_index();
                // Determine the style class based on the word properties
                String styleClass = determineStyleClass(current);
                // Apply the style class
                textArea.setStyleClass(startIndex, endIndex, styleClass);


                
                
            
            current = current.getNext_node();
        }


        
    }

    /**
     * Determines the style class for a Word_Object based on its properties.
     *
     * @param word The Word_Object for which the style class is determined.
     * @return The style class corresponding to the word's properties.
     */
    private String determineStyleClass(Word_Object word) {
        if (!word.isIs_real_word()) {
            return "misspelled-word";
        } else if (word.isIs_double_word_after() || word.isIs_double_word_before()) {
            return "double-word";
        } else if (word.isNeeds_first_capital() || word.isNeeds_lower() || word.isNeeds_lower_but_first()) {
            //upcount the current capital errors
            
            return "capital-word";
        } else {
            return "normal-word";
        }
    }
   
    

    
}
