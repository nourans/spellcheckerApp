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
    private int punctuation_index;
    private boolean is_first_word;
    
    /**
     * Constructor for the Word_Object class.
     * @param word
     */
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
        this.punctuation_index = 0;
        this.is_capital_at = new int[word.length()];


    }

    private boolean modified = true;
    
    /**
     * Constructor for the Word_Object class.
     * @return boolean to show needs a capital
     */
    public boolean isNeeds_first_capital() {
        return needs_first_capital;
    }
    /**
     * Constructor for the Word_Object class.
     * @param needs_first_capital   boolean to show needs a capital
     * @return void
     */
    public void setNeeds_first_capital(boolean needs_first_capital) {
        this.needs_first_capital = needs_first_capital;
    }
    /**
     * Constructor for the Word_Object class.
     * @return boolean to show needs a capital but first
     */
    public boolean isNeeds_lower_but_first() {
        return needs_lower_but_first;
    }
    /**
     * Constructor for the Word_Object class.
     * @param needs_lower_but_first   boolean to show needs a capital but first
     * @return void
     */
    public void setNeeds_lower_but_first(boolean needs_lower_but_first) {
        this.needs_lower_but_first = needs_lower_but_first;
    }
    /**
     * Constructor for the Word_Object class.
     * @return boolean to show needs a lower
     */
    public boolean isNeeds_lower() {
        return needs_lower;
    }
    /**
     * Constructor for the Word_Object class.
     * @param needs_lower   boolean to show needs a lower
     * @return void
     */
    public void setNeeds_lower(boolean needs_lower) {
        this.needs_lower = needs_lower;
    }

    /**
     * Constructor for the Word_Object class.
     * @return boolean to show if modified
     */
    public boolean isModified() {
        return modified;
    }
    
    /**
     * Constructor for the Word_Object class.
     * @param modified   boolean to show if modified
     * @return void
     */
    public void setModified(boolean modified) {
        this.modified = modified;
    }
    
    /**
     * Constructor for the Word_Object class.
     * @param word   String of the word
     * @param start_with_capital   boolean to show if starts with a capital
     * @param end_with_period   boolean to show if ends with a period
     * @param is_real_word   boolean to show if is a real word
     * @param needs_capital   boolean to show if needs a capital
     * @param needs_period   boolean to show if needs a period
     * @param is_double_word_after   boolean to show if is a double word after
     * @param is_double_word_before   boolean to show if is a double word before
     * @param suggestion_1   String of the first suggestion
     * @param suggestion_2   String of the second suggestion
     * @param suggestion_3   String of the third suggestion
     * @return void
     */
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
    /**
     * Constructor for the Word_Object class.
     * @param start_index  int of the start index
     */
    public void setStart_index(int start_index) {
        this.start_index = start_index;
    }
    /**
     * Constructor for the Word_Object class.
     * @param end_index  int of the end index
     */
    public void setEnd_index(int end_index) {
        this.end_index = end_index;
    }
    /**
     * Constructor for the Word_Object class.
     * @return int of the start index
     */
    public int getStart_index() {
        return start_index;
    }
    /**
     * Constructor for the Word_Object class.
     * @return int of the end index
     */
    public int getEnd_index() {
        return end_index;
    }
    
    /**
     * Constructor for the Word_Object class.
     * @return Word_Object of the previous node
     */
    public Word_Object getPrev_node() {
        return prev_node;
    }
    /**
     * Constructor for the Word_Object class.
     * @param prev_node  Word_Object of the previous node
     */
    public void setPrev_node(Word_Object prev_node) {
        this.prev_node = prev_node;
    }
    /**
     * Constructor for the Word_Object class.
     * @return Word_Object of the next node
     */
    public Word_Object getNext_node() {
        return next_node;
    }
     /**
     * Sets the next node in the linked list.
     * @param next_node The next Word_Object in the linked list.
     */
    public void setNext_node(Word_Object next_node) {
        this.next_node = next_node;
    }
    /**
     * Gets the word represented by this Word_Object.
     * @return The word.
     */
    public String getWord() {
        return word;
    }
    /**
     * Checks if the last character in the word is a period, exclamation mark, or question mark.
     * @return true if the last character is a period, exclamation mark, or question mark; false otherwise.
     */
    public boolean check_end_punctuation(){
        //check if the last char is a period
        if(this.word.charAt(this.word.length() - 1) == '.' || this.word.charAt(this.word.length() - 1) == '!' || this.word.charAt(this.word.length() - 1) == '?'){
            return true;
        }
        else{
            return false;
        }
    }
    /**
     * Sets whether the word ends with punctuation.
     * @param endsWithPunctuation True if the word ends with punctuation, false otherwise.
     */
    public void setEndsWithPunctuation(boolean endsWithPunctuation) {
        this.ends_with_punctuation = endsWithPunctuation;
    }
    /**
     * Gets whether the word ends with punctuation.
     * @return True if the word ends with punctuation, false otherwise.
     */
    public boolean getEndsWithPunctuation() {
        return ends_with_punctuation;
    }
    /**
     * Sets whether the word is the first word in the document.
     * @param is_first_word True if the word is the first word, false otherwise.
     */
    public void setIs_first_word(boolean is_first_word) {
        this.is_first_word = is_first_word;
    }
    /**
     * Gets whether the word is the first word in the document.
     * @return True if the word is the first word, false otherwise.
     */
    public boolean isIs_first_word() {
        return is_first_word;
    }
    /**
     * Sets the word represented by this Word_Object and updates related properties.
     * @param word The new word.
     */
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
    /**
     * Replaces the word represented by this Word_Object with a new word and adjusts indices accordingly.
     * @param word The new word to replace with.
     */
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
    /**
     * Sets the array indicating capitalization at each character position in the word.
     * @param capital_at The capitalization array.
     */
    public void setCapital_at(int[] capital_at) {
        this.is_capital_at = capital_at;
    }
    /**
     * Sets capital-related markers based on the capitalization array.
     */
    public void set_capital_markets(){
        //check that if the previous word ends with a period, then this word needs a capital
        if(this.prev_node != null){
            if(this.prev_node.isEnd_with_period()){
                this.setIs_first_word(true);
                //check if the fist char is a capital
                if(Character.isUpperCase(this.word.charAt(0))){
                    //do nothing
                    this.needs_first_capital = false;
                }
                else{
                    //set the needs capital to true
                    this.needs_first_capital = true;
                }
                //check to see if any other characters are capitals
                for(int i = 1; i < this.word.length(); i++){
                    if(Character.isUpperCase(this.word.charAt(i))){
                        //set the needs lower but first to true
                        if(i == 0){
                        //this.needs_lower_but_first = true;
                        this.needs_first_capital = false;
                        }else{
                            this.needs_lower = true;
                        }
                        
                    }
                }
            
            }else{
            //check to see if any letters are capitals
            for(int i = 0; i < this.word.length(); i++){
                if(Character.isUpperCase(this.word.charAt(i))){
                    //set the needs lower to true
                    if(i == 0){
                        this.needs_lower_but_first = true;
                        this.needs_first_capital = false;
                    }else{
                    this.needs_lower = true;
                    }
                    break;
                }
        }
    }
        }else{
            this.setIs_first_word(true);
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
                        if(i == 0){
                        //this.needs_lower_but_first = true;  //think this is wrong. word gaLlery
                        this.needs_first_capital = false;
                        }else{
                            this.needs_lower = true;
                        }
                    }
                }
            }
            
            //check to see if any letters are capitals
            //THIS LOOPS THE SAME LOOP AS BEFORE
           // for(int i = 0; i < this.word.length(); i++){
           //     if(Character.isUpperCase(this.word.charAt(i))){
                    //set the needs lower to true
            //        this.needs_lower = true;
            //        break;
         //       }

      //  }
    }

    

    /**
     * Gets whether the word ends with a period.
     * @return True if the word ends with a period, false otherwise.
     */
    public boolean isEnd_with_period() {
        return end_with_period;
    }
    /**
     * Sets whether the word ends with a period.
     * @param end_with_period True if the word ends with a period, false otherwise.
     */
    public void setEnd_with_period(boolean end_with_period) {
        this.end_with_period = end_with_period;
    }

    /**
     * Gets whether the word is a real word.
     * @return True if the word is a real word, false otherwise.
     */
    public boolean isIs_real_word() {
        return is_real_word;
    }
    /**
     * Sets whether the word needs a capital letter at the beginning.
     * @param needs_capital True if the word needs a capital letter, false otherwise.
     */
    public void setIs_real_word(boolean is_real_word) {
        this.is_real_word = is_real_word;
    }
    /**
     * Gets whether the word needs a period at the end.
     * @return True if the word needs a period, false otherwise.
     */
    public boolean isNeeds_capital() {
        
        return needs_capital;
    }
    /**
     * Sets whether the word needs a capital letter.
     * @param needs_capital True if the word needs a capital letter, false otherwise.
     */
    public void setNeeds_capital(boolean needs_capital) {
        this.needs_capital = needs_capital;
    }
    /**
     * Gets whether the word needs a period at the end.
     * @return True if the word needs a period, false otherwise.
     */
    public boolean isNeeds_period() {
        return needs_period;
    }
    /**
     * Sets whether the word needs a period at the end.
     * @param needs_period True if the word needs a period, false otherwise.
     */
    public void setNeeds_period(boolean needs_period) {
        this.needs_period = needs_period;
    }
    /**
     * Sets the number of spaces after the word.
     * @param spaces_after The number of spaces after the word.
     */
    public void setSpaces_after(int spaces_after) {
        this.spaces_after = spaces_after;
    }
    
    /**
     * Sets the number of spaces before the word.
     * @param spaces_before The number of spaces before the word.
     */
    public void setSpaces_before(int spaces_before) {
        this.spaces_before = spaces_before;
    }
    /**
     * Gets the number of spaces after the word.
     * @return The number of spaces after the word.
     */
    public int getSpaces_after() {
        return spaces_after;
    }
    /**
     * Gets the number of spaces before the word.
     * @return The number of spaces before the word.
     */
    public int getSpaces_before() {
        return spaces_before;
    }
    /**
     * Checks whether the word is part of a double word after.
     * @return True if the word is part of a double word after, false otherwise.
     */
    public boolean isIs_double_word_after() {
        return is_double_word_after;
    }
    /**
     * Sets whether the word is part of a double word after.
     * @param is_double_word_after True if the word is part of a double word after, false otherwise.
     */
    public void setIs_double_word_after(boolean is_double_word_after) {
        this.is_double_word_after = is_double_word_after;
    }
    /**
     * Checks whether the word is part of a double word before.
     * @return True if the word is part of a double word before, false otherwise.
     */
    public boolean isIs_double_word_before() {
        return is_double_word_before;
    }
    /**
     * Sets whether the word is part of a double word before.
     * @param is_double_word_before True if the word is part of a double word before, false otherwise.
     */
    public void setIs_double_word_before(boolean is_double_word_before) {
        this.is_double_word_before = is_double_word_before;
    }

    /**
     * Sets the suggestions for the word.
     * @param suggestions An array of suggestions for the word.
     */
    public void setSuggestions(String[] suggestions) {
        this.suggestion_1 = suggestions.length > 0 ? suggestions[0] : "";
        this.suggestion_2 = suggestions.length > 1 ? suggestions[1] : "";
        this.suggestion_3 = suggestions.length > 2 ? suggestions[2] : "";
    }
    /**
     * Gets the suggestions for the word.
     * @return An array of suggestions for the word.
     */
    public String[] getSuggestions(){
        String[] items = new String[3];
        items[0] = this.suggestion_1;
        items[1] = this.suggestion_2;
        items[2] = this.suggestion_3;

        return items;

    }
    /**
     * Gets the array indicating capitalization at each character position in the word.
     * @return The capitalization array.
     */
    public int[] getIs_capital_at() {
        return is_capital_at;
    }
   
    /**
     * Checks whether the word has a next node in the linked list.
     * @return True if the word has a next node, false otherwise.
     */
    public boolean hasNext(){
        if(this.next_node == null){
            return false;
        }
        else{
            return true;
        }
    }
    /**
     * Sets the punctuation index to the specified value.
     * @param i The index to set for punctuation.
     * @return The updated punctuation index.
     */
    public int set_punctuation_index(int i){
        this.punctuation_index = i;
        return punctuation_index;
    }
    /**
     * Retrieves the current punctuation index.
     * @return The current punctuation index.
     */
    public int get_punctuation_index(){
        return this.punctuation_index;
    }
}
