package Backend;
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
    
    /**
     * Constructor initializes a new Doc_Error object and sets default values for error variables.
     * Also creates an instance of the dictionary using a specified words file.
     */
    public Doc_Error(){
        //set vars to zero
        this.current_misspelt_words = 0;
        this.corrected_misspelt_words = 0;
        this.current_double_words = 0;
        this.corrected_double_words = 0;
        this.current_capital_errors = 0;
        this.corrected_capital_errors = 0;

        //create an instance of the dictionary
        String path = "C:/Users/james/Downloads/2232/UI/src/main/java/Backend/dict_resources/words.txt";
        dictionary = new Dictionary(path);
        System.out.println("dictionary created");


        
    }
    /**
     * Checks words from the document for misspellings and provides suggestions for correction.
     * @param word The Word_Object representing the word to be checked.
     */
    public void checkWords(Word_Object word) {
        //First check if the word is an actual word
        //get the word from the word object
        String word_text = word.getWord();
        // check to see if word in dict
        //take off any punctuation
        if(word_text.endsWith(".") || word_text.endsWith(",") || word_text.endsWith(";") || word_text.endsWith(":") || word_text.endsWith("!") || word_text.endsWith("?") || word_text.endsWith("\"") || word_text.endsWith("\'")){
            if(word_text.endsWith(".")){
                word.set_punctuation_index(1);
            }else if(word_text.endsWith("?")){
                word.set_punctuation_index(2);
            }else if(word_text.endsWith("!")){
                word.set_punctuation_index(3);
            }else if(word_text.endsWith(",")){
                word.set_punctuation_index(4);
            }else if(word_text.endsWith(";")){
                word.set_punctuation_index(5);
            }

            word_text = word_text.substring(0, word_text.length() - 1);

        }

        Boolean is_a_word = dictionary.isWord(word_text);
        //set the word object is real word flag
        word.setIs_real_word(is_a_word);
        
        //if it is not a word (False evaluation)
        if(!is_a_word){
            
            //itterate the current misspelt words
            current_misspelt_words++;
            
            

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

    /**
     * Checks for consecutive occurrences of the same word in the document.
     * @param word_Object The Word_Object representing the current word to be checked.
     */  
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
    
    /**
     * Sets the "is_first_word" flag for the given word based on its position in the document.
     * @param word The Word_Object representing the word to be checked.
     */
    public void set_is_first(Word_Object word){
        //check to see if there is a previous word
        if(word.getPrev_node() == null){
            //if there is no previous word, then this is the first word
            word.setIs_first_word(true);
        }
        else{
            //if the previous word ends with punctuation, then this is the first word
            if(word.getPrev_node().getEndsWithPunctuation()){
                System.out.println("previous word ends with punctuation");
                word.setIs_first_word(true);
            }
            else{
                //if the previous word does not end with punctuation, then this is not the first word
                word.setIs_first_word(false);
            }
        }
    }
    /**
     * Clears the user dictionary
     */
    public void clearUserDict(){
        dictionary.clear_user_dict();
    }
    /**
     * Returns when the user dictionary is null
     */
    public boolean userDictIsNull(){
        return dictionary.userDictIsNull();
    }
    /**
     * Checks for capitalization errors in the document and sets flags in the Word_Objects accordingly.
     * @param head The head of the linked list of Word_Objects representing the document.
     */
    public void checkCapitals(Word_Object head) {
        Word_Object current = head;

        // set the ends with punctuation flag for each

        while (current != null) {
            //System.out.println("word: " + current.getWord());
            if(current.check_end_punctuation()){
                
                current.setEndsWithPunctuation(true);
            }
            else{
                current.setEndsWithPunctuation(false);
            }
            current = current.getNext_node();
        }

        // reset current to the head
        current = head;
        //run set_is_first on the head
    
        while (current != null) {
            String word = current.getWord();
            set_is_first(current);
    
            // Check for the first word or words after punctuation (as marked by is_first_word)
            if (current.isIs_first_word()) {
                if (!Character.isUpperCase(word.charAt(0))) {
                    current.setNeeds_first_capital(true);
                    
                }
            } else {
                // Check for capitals anywhere in the word
                for (int i = 0; i < word.length(); i++) {
                    if (Character.isUpperCase(word.charAt(i))) {
                        current.setNeeds_lower(true);
                        //check that word is_modified
                      
                        break;
                    }
                    
                }
            }
    
            // Move to the next node in the list
            current = current.getNext_node();
        }
        
        //loop to fix miscapitalizations
        current = head;
        
        while(current != null){
            String word = current.getWord();
            if(current.isIs_first_word()){
                if(Character.isUpperCase(word.charAt(0))){
                    current.setNeeds_first_capital(false);
                    current.setNeeds_lower_but_first(false);
                }
            }
            current = current.getNext_node();
        }
    }

    /**
     * Method upCountCapital_fix increases the count of fixed capitalization errors.
     * @param head is the first letter of the word to be capatalized 
     */
    public void upCountCapital_fix(Word_Object head){
        //loop through, check errors
        current_capital_errors = 0;
        Word_Object current = head;
        while(current != null){
            
            if(current.isNeeds_first_capital() || current.isNeeds_lower() || current.isNeeds_lower_but_first()){
                current_capital_errors = current_capital_errors + 1;
            }
            
            
            current = current.getNext_node();
        }
        
    }

    

    /**
     * Adds a word to the user dictionary for custom words.
     * @param word The word to be added to the user dictionary.
     */
    public void addToUserDict(String word){
        //add the word to the user dict
        dictionary.add_user_word(word);
    }

    public String [] get_user_dict(){
        

        String [] user_dict = dictionary.get_user_dict();

        return user_dict;

    }

    /**
     * Decrements the count of current misspelled words and increments the count of corrected misspelled words.
     */ 
    public void downCountMisspelt(){
        //decrement the current misspelt words
        this.current_misspelt_words--;
        //increment the corrected misspelt words
        this.corrected_misspelt_words++;
    }
    
    /**
     * Decrements the count of current double words and increments the count of corrected double words.
     */
    public void downCountDoubleWord(){
        //decrement the current double words
        this.current_double_words--;
        //increment the corrected double words
        this.corrected_double_words++;
    }
    
    /**
     * Decrements the count of current capitalization errors and increments the count of corrected capitalization errors.
     */
    public void downCountCapital(){
        //decrement the current capital errors
        this.current_capital_errors--;
        //increment the corrected capital errors
        this.corrected_capital_errors++;
    }

    /**
     * Gets the current count of misspelled words in the document.
     * @return The current count of misspelled words.
     */  
    public int getCurrent_misspelt_words() {
        return current_misspelt_words;
    }
    
    /**
     * Sets the current count of misspelled words in the document.
     * @param current_misspelt_words The new count of misspelled words to be set.
     */
    public void setCurrent_misspelt_words(int current_misspelt_words) {
        this.current_misspelt_words = current_misspelt_words;
    }

    /**
     * Gets the count of corrected misspelled words in the document.
     * @return The count of corrected misspelled words.
     */
    public int getCorrected_misspelt_words() {
        return corrected_misspelt_words;
    }
    /**
     * Sets the count of corrected misspelled words in the document.
     * @param corrected_misspelt_words The new count of corrected misspelled words to be set.
     */
    public void setCorrected_misspelt_words(int corrected_misspelt_words) {
        this.corrected_misspelt_words = corrected_misspelt_words;
    }
    /**
     * Gets the current count of double words in the document.
     * @return The current count of double words.
     */
    public int getCurrent_double_words() {
        return current_double_words;
    }
    /**
     * Sets the current count of double words in the document.
     * @param current_double_words The new count of double words to be set.
     */
    public void setCurrent_double_words(int current_double_words) {
        this.current_double_words = current_double_words;
    }
    /**
     * Gets the count of corrected double words in the document.
     * @return The count of corrected double words.
     */
    public int getCorrected_double_words() {
        return corrected_double_words;
    }
    /**
     * Sets the count of corrected double words in the document.
     * @param corrected_double_words The new count of corrected double words to be set.
     */
    public void setCorrected_double_words(int corrected_double_words) {
        this.corrected_double_words = corrected_double_words;
    }
    /**
     * Gets the current count of capitalization errors in the document.
     * @return The current count of capitalization errors.
     */
    public int getCurrent_capital_errors() {
        return current_capital_errors;
    }
   /**
     * Sets the current count of capitalization errors in the document.
     * @param current_capital_errors The new count of capitalization errors to be set.
     */
    public void setCurrent_capital_errors(int current_capital_errors) {
        this.current_capital_errors = current_capital_errors;
    }
    /**
     * Gets the count of corrected capitalization errors in the document.
     * @return The count of corrected capitalization errors.
     */
    public int getCorrected_capital_errors() {
        return corrected_capital_errors;
    }
    /**
     * Sets the count of corrected capitalization errors in the document.
     * @param corrected_capital_errors The new count of corrected capitalization errors to be set.
     */
    public void setCorrected_capital_errors(int corrected_capital_errors) {
        this.corrected_capital_errors = corrected_capital_errors;
    }

    /**
     * Adds one to the current capital errors count
     */
    public void upCountCapital(){
        this.current_capital_errors++;
    }

    public int increase_corrected_capital_errors(){
        this.corrected_capital_errors++;
        return this.corrected_capital_errors;
    }

    


}
