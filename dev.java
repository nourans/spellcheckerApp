package Backend;

import java.util.HashMap;
import java.util.Map;

public class Document{
//this class is to work as the hub for the spell check system creating the linked list from the workspace text.
//It transforms the data types into our word object so that the words can be analyzed by the spell check.

// first creating instance variables
public String text;  // variable to hold last checked text instance in document
public LinkedList wordBuffer = new LinkedList();  //linked list to hold our word object list (for the entire document)
//create an instance of Doc error
Doc_Error doc_error = new Doc_Error();
Doc_Analysis doc_analysis;

//constructor
public Document(String text){
  this.text = text;
  populateLinkedList(text);
  //doc_analysis = new Doc_Analysis(wordBuffer);
  System.out.println("document created");
  //create doc analysis object
  this.doc_analysis = new Doc_Analysis(wordBuffer);
  this.doc_error = new Doc_Error();
}
public void populateLinkedList(String text) {
    
    //check to see if the string is empty
    if(text.equals("")){
      //do nothing
      return;
    }
    String[] wordsWithDelimiters = text.split("\\s+");
    //check to see if the linked list is empty
    int i = 0;
    
    

    Word_Object current = wordBuffer.getHead();
    
    while (current != null) {
      //check that i is in range of the wordsWithDelimiters
      if(i >= wordsWithDelimiters.length - 1){
        //remove the word
        wordBuffer.removeWord(current);
        //move to the next word
        current = current.getNext_node();
      }else if(current.getWord().equals(wordsWithDelimiters[i])){
        //do nothing
        i++;
      }else{
        String new_word = wordsWithDelimiters[i];
        //take off the punctuation if any
        if(new_word.endsWith(".") || new_word.endsWith(",") || new_word.endsWith(";") || new_word.endsWith(":") || new_word.endsWith("!") || new_word.endsWith("?") || new_word.endsWith("\"") || new_word.endsWith("\'")){
          new_word = new_word.substring(0, new_word.length() - 1);
        }
        //create a new word object
        Word_Object new_word_obj = new Word_Object(wordsWithDelimiters[i]);
        //replace the word object in the linked list
        wordBuffer.replaceWord(current, new_word_obj);
        //increment i
        i++;
        //call the capital function
        mark_capitals(new_word_obj);
      }
      if(current.getNext_node() != null){
        current = current.getNext_node();
      }else{
        break;
      }
    }
    //now we need to add the rest of the words to the linked list
    while(i < wordsWithDelimiters.length){
      //create a new word object
      Word_Object new_word = new Word_Object(wordsWithDelimiters[i]);
      //add the word object to the linked list
      wordBuffer.add(new_word);
      //increment i
      i++;
      //call the capital function
      mark_capitals(new_word);
    }
  }

public void mark_capitals(Word_Object word){
  
  //set the word_objects capital array
  String this_word = word.getWord();

  //get the length of the word
  int length = this_word.length();

  //create an array to hold the capitals
  int[] capitals = new int[length];

  //loop through the word
  for(int i = 0; i < length; i++){
    //check to see if the char is a capital
    if(Character.isUpperCase(this_word.charAt(i))){
      capitals[i] = 1;
    }else{
      capitals[i] = 0;
    }
  }

  //set the capital array
  word.setCapital_at(capitals);

  //FROM THE CAPITAL ARRAY SE THE MARKERS
  word.set_capital_markets();


}




public void run_spell_check() {
  Word_Object current = wordBuffer.getHead();
  while (current != null) {
      if (current.isModified()) {
          // Run spell check on this word
          doc_error.checkWords(current);
          doc_error.checkDoubleWord(current);
          doc_error.checkCapitals(current);
          current.setModified(false); // Reset the modified flag after checking
      }
      current = current.getNext_node();
  }

  //let us update the doc analysis
  update_doc_analysis();
}

public Word_Object check_single_word(Word_Object word){
  //create a word object
  

  //check the word
  doc_error.checkWords(word);

  //return the word object
  return word;
}

public Word_Object get_word_in_linked_list(int index){
  
  //call calculate indicies (linked list was just updated by calling function)
  wordBuffer.calculate_indicies();
  

  //get the word object at the index
  Word_Object word = (Word_Object) wordBuffer.get_word_at_index(index);

  //return the word object
  return word;
}


public void update_doc_analysis(){
  System.out.println("updating doc analysis");
  //check to see if doc_analysis is null
  if(doc_analysis == null){
    //create a new doc analysis object and assign it to instance variable
    doc_analysis = new Doc_Analysis(wordBuffer);
  }
  //update the document analysis
  doc_analysis.update(wordBuffer);
  //get the values from doc analysis and print them
  int char_count = doc_analysis.get_char_count();
  int word_count = doc_analysis.get_word_count();
  int line_count = doc_analysis.get_line_count();

  System.out.println("char count: " + char_count);
  System.out.println("word count: " + word_count);
  System.out.println("line count: " + line_count);
}

public int[] get_doc_analysis(int num_lines){
  

  //get the char count
  int char_count = doc_analysis.get_char_count();

  //get the word count
  int word_count = doc_analysis.get_word_count();
  
  //get the line count
  int line_count = num_lines;

  //add them to an array
  int[] analysis = {char_count, word_count, line_count};

  return analysis;



}

public int[] get_doc_error_values(){
  //get the misspelt words
  int misspelt_words = doc_error.getCurrent_misspelt_words();


  int Corrected_misspelt_words = doc_error.getCorrected_misspelt_words();

  int Current_double_words = doc_error.getCurrent_double_words();

  int Corrected_double_words = doc_error.getCorrected_double_words();

  int Current_capital_errors = doc_error.getCurrent_capital_errors();

  int Corrected_capital_errors = doc_error.getCorrected_capital_errors();

  //add them to an array
  int[] errors = {misspelt_words, Corrected_misspelt_words, Current_double_words, Corrected_double_words, Current_capital_errors, Corrected_capital_errors};

  

  //return the array
  return errors;

  }


  public Doc_Error get_doc_error(){
    return doc_error;
  }

  public void add_to_user_dict(String word){
    //call the add to user dict function in doc error
    doc_error.addToUserDict(word);
  }

  

  public void decrease_current_misspelt_words(){
    //call the down count misspelt function in doc error
    doc_error.downCountMisspelt();
  }

  public void decrease_current_double_words(){
    //call the down count double word function in doc error
    doc_error.downCountDoubleWord();
  }

  public void decrease_current_capital_errors(){
    //call the down count capital function in doc error
    doc_error.downCountCapital();
  
  }

  
}

package Backend;



public class Word_Object{
    private Word_Object prev_node;
    private Word_Object next_node;
    private String word;
    private boolean needs_first_capital;
    private boolean needs_lower_but_first;
    private boolean needs_lower;
    private boolean end_with_period;
    private boolean is_real_word;
    private boolean needs_capital;
    private boolean needs_period;
    private boolean is_double_word_after;
    private boolean is_double_word_before;
    private String suggestion_1;
    private String suggestion_2;
    private String suggestion_3;
    private int start_index;
    private int end_index;
    private int spaces_after;
    private int spaces_before;
    private int[] is_capital_at;
    private boolean ends_with_punctuation;
    private boolean is_first_word;

    public Word_Object(String word) {
        // Initialization logic
        //set is_real_word to false
        this.is_real_word = false;
        this.word = word;
        this.end_with_period = false;
        this.needs_capital = false;
        this.needs_period = false;
        this.is_double_word_after = false;
        this.is_double_word_before = false;
        this.suggestion_1 = "";
        this.suggestion_2 = "";
        this.suggestion_3 = "";
        this.start_index = 0;
        this.end_index = 0;
        this.spaces_after = 0;
        this.spaces_before = 0;
        this.is_capital_at = null;
        this.ends_with_punctuation = false;
        this.is_first_word = false;
        this.needs_first_capital = false;
        this.needs_lower_but_first = false;
        this.needs_lower = false;


    }

    private boolean modified = true;

    public boolean isNeeds_first_capital() {
        return needs_first_capital;
    }

    public void setNeeds_first_capital(boolean needs_first_capital) {
        this.needs_first_capital = needs_first_capital;
    }

    public boolean isNeeds_lower_but_first() {
        return needs_lower_but_first;
    }

    public void setNeeds_lower_but_first(boolean needs_lower_but_first) {
        this.needs_lower_but_first = needs_lower_but_first;
    }

    public boolean isNeeds_lower() {
        return needs_lower;
    }

    public void setNeeds_lower(boolean needs_lower) {
        this.needs_lower = needs_lower;
    }


    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    
    public void Word_Object_full(String word, boolean start_with_capital, boolean end_with_period, boolean is_real_word, boolean needs_capital, boolean needs_period, boolean is_double_word_after, boolean is_double_word_before, String suggestion_1, String suggestion_2, String suggestion_3) {
        this.word = word;
        this.end_with_period = end_with_period;
        this.is_real_word = is_real_word;
        this.needs_capital = needs_capital;
        this.needs_period = needs_period;
        this.is_double_word_after = is_double_word_after;
        this.is_double_word_before = is_double_word_before;
        this.suggestion_1 = suggestion_1;
        this.suggestion_2 = suggestion_2;
        this.suggestion_3 = suggestion_3;
    }

    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }

    public void setEnd_index(int end_index) {
        this.end_index = end_index;
    }

    public int getStart_index() {
        return start_index;
    }

    public int getEnd_index() {
        return end_index;
    }
    

    public Word_Object getPrev_node() {
        return prev_node;
    }

    public void setPrev_node(Word_Object prev_node) {
        this.prev_node = prev_node;
    }

    public Word_Object getNext_node() {
        return next_node;
    }

    public void setNext_node(Word_Object next_node) {
        this.next_node = next_node;
    }

    public String getWord() {
        return word;
    }
    public void setEndsWithPunctuation(boolean endsWithPunctuation) {
        this.ends_with_punctuation = endsWithPunctuation;
    }
    public boolean getEndsWithPunctuation() {
        return ends_with_punctuation;
    }

    public void setIs_first_word(boolean is_first_word) {
        this.is_first_word = is_first_word;
    }

    public boolean isIs_first_word() {
        return is_first_word;
    }

    public void setWord(String word) {
        this.word = word;
        this.is_capital_at = new int[word.length()];
        //loop through each char assign 1 if capital, 0 if not
        for(int i = 0; i < word.length(); i++){
            if(Character.isUpperCase(word.charAt(i))){
                this.is_capital_at[i] = 1;
            }
            else{
                this.is_capital_at[i] = 0;
            }
        }
    }

    public void replace_word(String word){
        //replace the word and adjust indicies after the word
        int old_length = this.word.length();
        int new_length = word.length();
        this.word = word;

        //adjust the indicies of the word
        this.end_index = this.start_index + new_length - 1;

        for(int i = 0; i < word.length(); i++){
            if(Character.isUpperCase(word.charAt(i))){
                this.is_capital_at[i] = 1;
            }
            else{
                this.is_capital_at[i] = 0;
            }
        }

        //adjust the indicies of the next word
        Word_Object current = this.next_node;
        while(current != null){
            current.setStart_index(current.getStart_index() - old_length + new_length);
            current.setEnd_index(current.getEnd_index() - old_length + new_length);
            current = current.getNext_node();
        }

        //mark the last and first words as modified
        if(this.next_node != null){
            this.next_node.setModified(true);
        }
        if(this.prev_node != null){
            this.prev_node.setModified(true);
        }
        

    }

    public void setCapital_at(int[] capital_at) {
        this.is_capital_at = capital_at;
    }

    public void set_capital_markets(){
        //check that if the previous word ends with a period, then this word needs a capital
        if(this.prev_node != null){
            if(this.prev_node.isEnd_with_period()){
                //check if the fist char is a capital
                if(Character.isUpperCase(this.word.charAt(0))){
                    //do nothing
                }
                else{
                    //set the needs capital to true
                    this.needs_first_capital = true;
                }
                //check to see if any other characters are capitals
                for(int i = 1; i < this.word.length(); i++){
                    if(Character.isUpperCase(this.word.charAt(i))){
                        //set the needs lower but first to true
                        this.needs_lower_but_first = true;
                        this.needs_first_capital = false;
                    }
                }
            }
            //check to see if any letters are capitals
            for(int i = 0; i < this.word.length(); i++){
                if(Character.isUpperCase(this.word.charAt(i))){
                    //set the needs lower to true
                    this.needs_lower = true;
                    break;
                }
            }
        }else{
            //first word so it needs a capital at the start
            if(Character.isUpperCase(this.word.charAt(0))){
                    //do nothing
                }
                else{
                    //set the needs capital to true
                    this.needs_first_capital = true;
                }
                //check to see if any other characters are capitals
                for(int i = 1; i < this.word.length(); i++){
                    if(Character.isUpperCase(this.word.charAt(i))){
                        //set the needs lower but first to true
                        this.needs_lower_but_first = true;
                        this.needs_first_capital = false;
                    }
                }
            }
            //check to see if any letters are capitals
            for(int i = 0; i < this.word.length(); i++){
                if(Character.isUpperCase(this.word.charAt(i))){
                    //set the needs lower to true
                    this.needs_lower = true;
                    break;
                }

        }
    }

    

    
    public boolean isEnd_with_period() {
        return end_with_period;
    }

    public void setEnd_with_period(boolean end_with_period) {
        this.end_with_period = end_with_period;
    }

    public boolean isIs_real_word() {
        return is_real_word;
    }

    public void setIs_real_word(boolean is_real_word) {
        this.is_real_word = is_real_word;
    }

    public boolean isNeeds_capital() {
        return needs_capital;
    }

    public void setNeeds_capital(boolean needs_capital) {
        this.needs_capital = needs_capital;
    }

    public boolean isNeeds_period() {
        return needs_period;
    }

    public void setNeeds_period(boolean needs_period) {
        this.needs_period = needs_period;
    }

    public void setSpaces_after(int spaces_after) {
        this.spaces_after = spaces_after;
    }
    public void setSpaces_before(int spaces_before) {
        this.spaces_before = spaces_before;
    }

    public int getSpaces_after() {
        return spaces_after;
    }

    public int getSpaces_before() {
        return spaces_before;
    }

    public boolean isIs_double_word_after() {
        return is_double_word_after;
    }

    public void setIs_double_word_after(boolean is_double_word_after) {
        this.is_double_word_after = is_double_word_after;
    }

    public boolean isIs_double_word_before() {
        return is_double_word_before;
    }

    public void setIs_double_word_before(boolean is_double_word_before) {
        this.is_double_word_before = is_double_word_before;
    }


    public void setSuggestions(String[] suggestions) {
        this.suggestion_1 = suggestions.length > 0 ? suggestions[0] : "";
        this.suggestion_2 = suggestions.length > 1 ? suggestions[1] : "";
        this.suggestion_3 = suggestions.length > 2 ? suggestions[2] : "";
    }
    public String[] getSuggestions(){
        String[] items = new String[3];
        items[0] = this.suggestion_1;
        items[1] = this.suggestion_2;
        items[2] = this.suggestion_3;

        return items;

    }

    public int[] getIs_capital_at() {
        return is_capital_at;
    }

   

    public boolean hasNext(){
        if(this.next_node == null){
            return false;
        }
        else{
            return true;
        }
    }

    
}



package Backend;
public class LinkedList {
    private Word_Object head; // Head of the list
    private Word_Object tail; // Tail of the list
    

    public LinkedList() {
        head = null;
        tail = null;
    }

    // Add a new node to the end of the list
    public void add(Word_Object newNode) {
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.setNext_node(null);
        } else {
            tail.setNext_node(newNode);
            newNode.setPrev_node(tail);
            tail = newNode;
        }
    }

    
    // Add a new node to the beginning of the list
    public void addFirst(Word_Object newNode) {
        if (head == null) {
            head = newNode;
            tail = newNode;
            newNode.setNext_node(null);
        } else {
            newNode.setNext_node(head);
            head.setPrev_node(newNode);
            head = newNode;
        }
    }

    // get the head of the list
    public Word_Object getHead() {
        return head;
    }

    // get the tail of the list
    public Word_Object getTail() {
        return tail;
    }

    public int get_length() {
        int length = 0;
        Word_Object curr = head;
        while (curr != null) {
            length++;
            curr = curr.getNext_node();
        }
        return length;
    }


    public void calculate_indicies() {
        // We will populate the index values for each word object.
        // Get the head of the linked list
        Word_Object curr = head;
    
        // Set the start index to 0
        int index = 0;
    
        // Loop through the linked list
        while (curr != null) {
            // Set the start index
            curr.setStart_index(index);
    
            // Get the word
            String word = curr.getWord();
    
            // Get the length of the word
            int word_length = word.length();
    
            // Add the word length to the index
            index += word_length;
    
            // Set the end index
            curr.setEnd_index(index);
    
            // Increment the index by 1 for space, by 2 for period and space
            if (curr.isEnd_with_period()) {
                index += 2;
            } else {
                index += 1;
            }
    
            // Move to the next node
            curr = curr.getNext_node();
        }
    }

    public void removeWord(Word_Object word) {
        if (word.getPrev_node() != null) {
            word.getPrev_node().setNext_node(word.getNext_node());
        } else {
            head = word.getNext_node();
        }
    
        if (word.getNext_node() != null) {
            word.getNext_node().setPrev_node(word.getPrev_node());
        } else {
            tail = word.getPrev_node();
        }
    }
    

    public Word_Object get_word_at_index(int index){
        System.out.println("get word at index: " + index);
        //get the head of the linked list
        Word_Object curr = head;
        System.out.println("index: " + index);
        System.out.println("start index: " + curr.getWord());

        //loop through the linked list
        while(curr != null){

            //check to see if the index is in the range of the word
            if(index >= curr.getStart_index() && index <= curr.getEnd_index()){
                return curr;
            }

            //increment the index
            curr = curr.getNext_node();

        }

        //if we get here, we did not find the word
        return null;

    }
    public Word_Object getWordAtCaretPosition(int caretPosition) {
        Word_Object current = head;
        
        while (current != null) {
            if (caretPosition >= current.getStart_index() && caretPosition <= current.getEnd_index()) {
                return current;
            }
            current = current.getNext_node();
        }
    
        return null; // No word found at this caret position
    }
    public void updateWordAtCaretPosition(int caretPosition, Word_Object newWord) {
        Word_Object wordToUpdate = getWordAtCaretPosition(caretPosition);
    
        if (wordToUpdate != null) {
            // Replace the existing word
            replaceWord(wordToUpdate, newWord);
        } else {
            // New word to add
            add(newWord); // This may need to be more sophisticated depending on how you handle new words
        }
    }
    public void replaceWord(Word_Object oldWord, Word_Object newWord) {
        newWord.setNext_node(oldWord.getNext_node());
        newWord.setPrev_node(oldWord.getPrev_node());
    
        if (oldWord.getPrev_node() != null) {
            oldWord.getPrev_node().setNext_node(newWord);
        } else {
            head = newWord;
        }
    
        if (oldWord.getNext_node() != null) {
            oldWord.getNext_node().setPrev_node(newWord);
        } else {
            tail = newWord;
        }
    }

    // Add other methods as needed, such as remove, find, etc.
}package spellapp.spellcheck;
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

    

    public void setTextAreaContent(String content) {
        textArea.replaceText(content); // Replaces the entire text content

        textArea.setOnMouseClicked(event -> {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            }
            // Additional logic for handling text click, if any
        });
        System.out.println("setting text area content" + content);
        System.out.println("text area content: " + textArea.getText());
        
        // Initialize and populate the document as before
        init_document(textArea); // Ensure init_document is compatible with InlineCssTextArea
        document.populateLinkedList(content);
        document.run_spell_check();

        
    }

    //this function starts at the first click, and automatically updates the encironment every 10 seconds
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
    

    
     //init the document object from the text area
    public void init_document(StyleClassedTextArea textArea_in){
        
        // Create a new document object
        this.document = new Document(textArea.getText());
        
    }
    

    public void setDocument(Document document) {
        // Populate the document object
        document.populateLinkedList(textArea.getText());
    
        // Set the document object
        this.document = document;
    }
    

    @FXML
    private void handleSaveButton(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");
        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            saveSystem(file, textArea.getText());
        }
    }

    private void saveSystem(File file, String text) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            printWriter.write(text);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


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

    //gets the text from the document (depricated (I think))
    public String[] parse_text(){
        //get the text from the text area
        String text = textArea.getText();
        //split the text into an array of words
        String[] words = text.split("\\s+");
        
        //delete double spaces
        
        //return the array of words
        return words;
    
    }

    @FXML
    public void exit(ActionEvent event) {
        try {
            // Load the Help.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Exit.fxml"));
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

    @FXML
    private void handleUserDictionary(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("UserDictionary.fxml"));
            Parent root = loader.load();
            UserDictionaryController userDictionaryController = loader.getController();
            userDictionaryController.setTextAreaContent(textArea.getText());
            if (getClass().getResourceAsStream("/stylesheet.css") == null) {
                System.out.println("Stylesheet not found");
            } else {
                System.out.println("Stylesheet found");
            }

            Scene mainScene = new Scene(root, 800, 500);
            //adds the stylesheet to the scene
            mainScene.getStylesheets().add(getClass().getResource("/spellapp/spellcheck/stylesheet.css").toExternalForm());


            Stage stage = new Stage();
            stage.setTitle("User Dictionary");
            stage.setScene(mainScene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading UserDictionary.fxml: " + e.getMessage());
        }
    }

    //I dont think we really need this
    @FXML
    void startSpell(ActionEvent event) {
        StringBuilder content = new StringBuilder();
        content.append( textArea.getText() );
        mainLabel.setText("Spell Check Mode");
        // call spell check mode
        
        
        //document.populateLinkedList(content.toString().split(" "));
        //document.run_spell_check();

        // Start Spell check mode
        // Pass the "content" String into Backend methods to load linked Lists, etc before starting spell check mode
        // Once done - Parse linked list of incorrect words and somehow show them differently on UI
        // THen we want to create a series of popup UIs that are able to popup when certain words are clicked on
    }
    // ---> james adding stuff.
    

    //gets the doc analysis values
    public int[] get_doc_analysis(){
        //gets the values from the doc analysis object
        //int num_lines = textArea.getParagraphs().size();
        String content = textArea.getText();
        int lines = (int) content.chars().filter(ch -> ch == '\n').count() + 1;
        int[] doc_err_vals = document.get_doc_analysis(lines);

        return doc_err_vals;

    };

    //define fxml values for the doc analysis counts
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

    //updates the document error values from an array of values

    public void update_doc_error_values(int[] values){
        //update the doc error values
        //get the values
        int misspelt_words = values[0];
        int corrected_misspelt_words = values[1];
        int double_words = values[2];
        int corrected_double_words = values[3];
        int capital_errors = values[4];
        int corrected_capital_errors = values[5];

        //set the values
        Misspellings.setText("Misspellings: " + misspelt_words);
        MissCaps.setText("Capital Errors: " + capital_errors);
        DoubleWords.setText("Double Words: " + double_words);
        Corrections.setText("Corrected Misspellings: " + corrected_misspelt_words);
        DoubleWordCorrections.setText("Corrected Double Words: " + corrected_double_words);
    }

    public void update_doc_analysis_values(int[] counts){
        //update the doc analysis values
        //get the values
        int char_count = counts[0];
        int word_count = counts[1];
        int line_count = counts[2];
        System.out.println("scene control doc vals");
        System.out.println("char count: " + char_count);
        System.out.println("word count: " + word_count);
        System.out.println("line count: " + line_count);

        //set the values
        characterCountLabel.setText("Character count: " + char_count);
        lineCountLabel.setText("Line count: " + line_count);
        wordCountLabel.setText("Word count: " + word_count);
    }

    //get the values from the doc error class via the document class/ 
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
    


    //inits the text area, as well as the repeated task function
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
            System.out.println("Mouse clicked in textArea"); // For debugging purposes
            if (event.getButton() == MouseButton.PRIMARY) {
                handleTextClick(event);
            } else {
                contextMenu.hide();
            }

            // Additional mouse click logic, if needed
        });
    }

    //when we click on the screen this function is called. 
    private void handleTextClick(MouseEvent event) {
    
        // Check if the document is initialized
        if (this.document == null) {
            System.out.println("document is null, initializing");
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
            System.out.println("word is misspelled: " + this_word.getWord());
            showPopupAtTextPosition_spelling(this_word, event.getScreenX(), event.getScreenY());
        }
        if(this_word.isIs_double_word_after() || this_word.isIs_double_word_before()){
            //hide all other context menus
            contextMenu.hide();
            System.out.println("word is double word: " + this_word.getWord());
            showPopupAtTextPosition_double_word(this_word, event.getScreenX(), event.getScreenY());
        }
        if(this_word.isNeeds_first_capital() || this_word.isNeeds_lower_but_first()){
            System.out.println("Is needs first capital");
            //provide a reccomendation that shows just the first letter capatalized
            String word_base = this_word.getWord();
            //first set it all to lower case
            String word_lower = word_base.toLowerCase();
            //then set the first letter to upper case
            String word_capital = word_lower.substring(0, 1).toUpperCase() + word_lower.substring(1);
            //create a new word_object
            
            popup_caps(word_capital, this_word, event.getScreenX(), event.getScreenY());

        }if(this_word.isNeeds_lower()){
            System.out.println("Is needs Lower");
            //provide a reccomendation that shows the word lowercase
            String word_base = this_word.getWord();
            String word_lower = word_base.toLowerCase();
            
            popup_caps(word_lower, this_word, event.getScreenX(), event.getScreenY());

        }
    }
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

    private void showPopupAtTextPosition_double_word(Word_Object word, double x, double y){

        contextMenu.getItems().clear();

        if (word.isIs_double_word_after()){
            contextMenu.getItems().addAll(
                createDoubleWordItem_replace(0, word)
                
            );

            contextMenu.show(textArea, x, y);
        }
    }
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
    

    //adding a function to display the popup
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
            System.out.println("adding to user dict");
            //get the word
            String word_text = word.getWord();
            //add the word to the user dictionary
            document.add_to_user_dict(word_text);
            System.out.println("added to user dict: " + word_text); 
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
    private void implement_caps(Word_Object word, String adjusted_word){
        //get the word start and stop indicies
        int startIndex = word.getStart_index();
        int endIndex = word.getEnd_index();

        word.setWord(adjusted_word);

        //replace that text in that range with the word
        textArea.replaceText(startIndex, endIndex, adjusted_word);
        //update the word object
        word.setIs_real_word(true);
        word.setNeeds_capital(false);
        word.setNeeds_lower_but_first(false);
        word.setNeeds_lower(false);

        //update the document statistics
        document.decrease_current_capital_errors();
    }

       
    

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
    
        // Determine the text replacement range in the text area
        int textAreaStartIndex = originalStartIndex;
        int textAreaEndIndex = (word.getNext_node() != null) ? word.getNext_node().getStart_index() - 1 : textArea.getLength();
    
        // Check the length difference between the original word and the suggestion
        int lengthDifference = suggestion.length() - originalWordLength;

        replaceAndInsertWord(textArea, word, suggestion);
    
        
    
        // Update document statistics
        document.decrease_current_misspelt_words();
    }
    private void replaceAndInsertWord(StyleClassedTextArea textArea, Word_Object word, String replacement) {
        // Validate the parameters
        if (word == null || replacement == null || replacement.isEmpty()) {
            return;
        }
    
        // Calculate the replacement area
        int startIndex = (word.getPrev_node() != null) ? word.getPrev_node().getEnd_index() : 0;
        int endIndex = (word.getNext_node() != null) ? word.getNext_node().getStart_index() : textArea.getLength() + 1;
    
        // Clear the area between the last and the next word
        textArea.replaceText(startIndex, endIndex, "");
    
        // Insert the new word
        textArea.insertText(startIndex, " " + replacement + " ");
    
        // Update the Word_Object and the document
        word.replace_word(replacement);
        word.setIs_real_word(true);
        word.setModified(false);
    
        // Recalculate the indices of all words in the document
        document.wordBuffer.calculate_indicies();

        // mark the previous and next word as modified
        
        // Update document statistics, if needed
        document.decrease_current_misspelt_words();
    }

    private void implement_drop_double_word(Word_Object word){
        //get the word start and stop indicies
        //update the words' indicies


        int startIndex = (word.getPrev_node() != null) ? word.getPrev_node().getEnd_index() + 1 : 0;
        int endIndex = (word.getNext_node() != null) ? word.getNext_node().getStart_index() : textArea.getLength();

        textArea.replaceText(startIndex, endIndex, " ");

        //remove the word

        document.wordBuffer.removeWord(word);

    }
    

    private int getClickPosition(MouseEvent event) {
        // Return the current caret position in the text area
        invoke_click_splash(event);
        return textArea.getCaretPosition();
    }

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
                before[j].isModified();
            }
            if(after[j] != null){
                after[j].isModified();
            }
        }

    }

    public void highlightErrors() {
        String text = textArea.getText();
        if (text == null || text.isEmpty()) {
            return; // Early return if the text is null or empty
        }
    
        String[] words = text.split("\\s+");
        int startIndex = 0;
    
        for (String word : words) {
            if (word.isEmpty()) {
                startIndex++; // For handling multiple consecutive spaces
                continue;
            }
    
            int endIndex = startIndex + word.length();
            Word_Object this_word = document.wordBuffer.get_word_at_index(startIndex);
    
            // Check for null or invalid word object
            if (this_word == null) {
                System.out.println("Word at index " + startIndex + " is null.");
                startIndex = endIndex + 1;
                continue; // Skip to next iteration if word object is null
            }
    
            // Determine the style class based on the word properties
            String styleClass = determineStyleClass(this_word);
    
            // Apply the style class
            textArea.setStyleClass(startIndex, endIndex, styleClass);
    
            startIndex = endIndex + 1;
        }
    }
    
    private String determineStyleClass(Word_Object word) {
        if (!word.isIs_real_word()) {
            return "misspelled-word";
        } else if (word.isIs_double_word_after() || word.isIs_double_word_before()) {
            return "double-word";
        } else if (word.isNeeds_first_capital() || word.isNeeds_lower() || word.isNeeds_lower_but_first()) {
            return "capital-word";
        } else {
            return "normal-word";
        }
    }
   
    

    
}package Backend;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Doc_Error {
    //create the document error variables
    private int current_misspelt_words;
    private int corrected_misspelt_words;
    private int current_double_words;
    private int corrected_double_words;
    private int current_capital_errors;
    private int corrected_capital_errors;
    Dictionary dictionary;

    //initialize the object
    public Doc_Error(){
        //set vars to zero
        this.current_misspelt_words = 0;
        this.corrected_misspelt_words = 0;
        this.current_double_words = 0;
        this.corrected_double_words = 0;
        this.current_capital_errors = 0;
        this.corrected_capital_errors = 0;

        //create an instance of the dictionary
        
        String path_test = "../UI/src/main/java/Backend/dict_resources/words.txt";
        String path_run = "../../Backend/dict_resources/words.txt";
        String path = "C:/Users/james/Desktop/2212_final/splash_fix/2212/UI/src/main/java/Backend/dict_resources/words.txt";
        File file = new File(path);
        
        dictionary = new Dictionary(path);

        //put int a try catch block
        
    }
    //checks words from the document for misspellings
    public void checkWords(Word_Object word) {
        //First check if the word is an actual word

        //get the word from the word object
        String word_text = word.getWord();
        // check to see if word in dict
        //take off any punctuation
        if(word_text.endsWith(".") || word_text.endsWith(",") || word_text.endsWith(";") || word_text.endsWith(":") || word_text.endsWith("!") || word_text.endsWith("?") || word_text.endsWith("\"") || word_text.endsWith("\'")){
            word_text = word_text.substring(0, word_text.length() - 1);
        }

        Boolean is_a_word = dictionary.isWord(word_text);
        //set the word object is real word flag
        word.setIs_real_word(is_a_word);
        
        //if it is not a word (False evaluation)
        if(!is_a_word){
            System.out.println("Misspelt word!!!: " + word_text);
            //itterate the current misspelt words
            this.current_misspelt_words++;
            //add a flag to the word object
            word.setIs_real_word(false);

            //now get suggestions for the word
            //create an array list to hold the suggestions
            String [] suggestions = dictionary.getSuggestions(word_text);
            //add the suggestions to the word object
            //print out the suggestions
            for(int i = 0; i < suggestions.length; i++){
                System.out.println(suggestions[i]);
            }
            word.setSuggestions(suggestions);
            
        }
    }

    //check double word function
    public void checkDoubleWord(Word_Object word_Object){

        //get the current word text
        String word_text = word_Object.getWord();

        //get the next word object if it is not null
        if(word_Object.getNext_node() == null){
            //if it is null then we do nothing
            return;
        }
        else{
            Word_Object next_word_object = word_Object.getNext_node();
            //extract the word string
            String next_word = next_word_object.getWord();
            System.out.println("next word: " + next_word);
            System.out.println("current word: " + word_text);

            //check to see if this word and next word are the same word
            if(word_text.equals(next_word)){
                //if they are the same word, then it is a double word
                this.current_double_words++;
                System.out.println("Double word!!!: " + word_text);
                
                //add a flag to the word object
                word_Object.setIs_double_word_after(true);
                word_Object.getNext_node().setIs_double_word_before(true);

            }
        }
    
    }

    public void set_is_first(Word_Object word){
        //check to see if there is a previous word
        if(word.getPrev_node() == null){
            //if there is no previous word, then this is the first word
            word.setIs_first_word(true);
        }
        else{
            //if the previous word ends with punctuation, then this is the first word
            if(word.getPrev_node().getEndsWithPunctuation()){
                word.setIs_first_word(true);
            }
            else{
                //if the previous word does not end with punctuation, then this is not the first word
                word.setIs_first_word(false);
            }
        }
    }

    // This is my new attempt at implemeting this I have added some new params to the word object for doing so and have added populating functions to do so. 
    public void checkCapitals(Word_Object head) {
        Word_Object current = head;
    
        while (current != null) {
            String word = current.getWord();
    
            // Check for the first word or words after punctuation (as marked by is_first_word)
            if (current.isIs_first_word()) {
                if (!Character.isUpperCase(word.charAt(0))) {
                    current.setNeeds_first_capital(true);
                    System.out.println("Needs first capital: " + word);
                }
    
                // Check if any other letters in the word are capitalized
                for (int i = 1; i < word.length(); i++) {
                    if (Character.isUpperCase(word.charAt(i))) {
                        current.setNeeds_lower_but_first(true);

                        break;
                    }
                    System.out.println("Needs Lower But first: " + word);
                }
            } else {
                // Check for capitals anywhere in the word
                for (int i = 0; i < word.length(); i++) {
                    if (Character.isUpperCase(word.charAt(i))) {
                        current.setNeeds_lower(true);
                        break;
                    }
                    System.out.println("Needs Lower: " + word);
                }
            }
    
            // Move to the next node in the list
            current = current.getNext_node();
        }
    }


    

    //add to user dict function
    public void addToUserDict(String word){
        //add the word to the user dict
        dictionary.add_user_word(word);
    }

    //functions to down count the errors as they are corrected
    public void downCountMisspelt(){
        //decrement the current misspelt words
        this.current_misspelt_words--;
        //increment the corrected misspelt words
        this.corrected_misspelt_words++;
    }

    public void downCountDoubleWord(){
        //decrement the current double words
        this.current_double_words--;
        //increment the corrected double words
        this.corrected_double_words++;
    }

    public void downCountCapital(){
        //decrement the current capital errors
        this.current_capital_errors--;
        //increment the corrected capital errors
        this.corrected_capital_errors++;
    }

    //getters and setters for the document class to use to popluate the front end values. 
    public int getCurrent_misspelt_words() {
        return current_misspelt_words;
    }

    public void setCurrent_misspelt_words(int current_misspelt_words) {
        this.current_misspelt_words = current_misspelt_words;
    }

    public int getCorrected_misspelt_words() {
        return corrected_misspelt_words;
    }

    public void setCorrected_misspelt_words(int corrected_misspelt_words) {
        this.corrected_misspelt_words = corrected_misspelt_words;
    }

    public int getCurrent_double_words() {
        return current_double_words;
    }

    public void setCurrent_double_words(int current_double_words) {
        this.current_double_words = current_double_words;
    }

    public int getCorrected_double_words() {
        return corrected_double_words;
    }

    public void setCorrected_double_words(int corrected_double_words) {
        this.corrected_double_words = corrected_double_words;
    }

    public int getCurrent_capital_errors() {
        return current_capital_errors;
    }

    public void setCurrent_capital_errors(int current_capital_errors) {
        this.current_capital_errors = current_capital_errors;
    }

    public int getCorrected_capital_errors() {
        return corrected_capital_errors;
    }

    public void setCorrected_capital_errors(int corrected_capital_errors) {
        this.corrected_capital_errors = corrected_capital_errors;
    }

    


}
