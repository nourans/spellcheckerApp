package Backend;
public class Doc_Analysis{
  //defining instance variables
  public char[] text;
  private int charCount = 0;
  private int wordCount = 0;
  private int lineCount = 0;
  
   /**
   * Constructor for Doc_Analysis class
   * @param wordBuffer A linked list containing words for analysis.
   */
  public Doc_Analysis(LinkedList wordBuffer){
    //get the text
    String[] text = getText(wordBuffer);
    //get the char
    char[] char_array = getChar(text);
    //get the char count
    //get the line count
    lineCount = getLineCount(char_array);

}

  /**
   * Private method to calculate the line count based on newline characters.
   * @param text The character array representing the document text.
   * @return The line count in the document.
   */
  private int getLineCount(char[] text) {
    int lineCount = 1; // Initialize lineCount
    int i = 0;
    while (i < text.length) {
        // If newline, count
        if (text[i] == '\n') {
            lineCount += 1;
        }
        i++; // Increment i to avoid infinite loop
    }
    return lineCount;
  }

  /**
   * Private method to extract text from a linked list of words.
   * @param wordBuffer A linked list containing words.
   * @return An array of strings representing the text.
   */
  private String[] getText(LinkedList wordBuffer) {
    int length = wordBuffer.get_length();
    String[] text = new String[length];

    Word_Object curr_word_object = wordBuffer.getHead();
    int i = 0;
    while (curr_word_object != null) {
        text[i] = curr_word_object.getWord();
        i++;
        curr_word_object = curr_word_object.getNext_node();
    }

    this.wordCount = i;
    return text;
  }
  
  /**
   * Private method to convert an array of strings into a single character array.
   * @param text An array of strings representing the document text.
   * @return A character array representing the document text.
   */
  private char[] getChar(String[] text) {
    // Calculate total length needed
    int totalLength = 0;
    for (String word : text) {
        totalLength += word.length();
    }

    // Initialize the array with the correct size
    char[] char_array = new char[totalLength];

    // Loop through the array and fill char_array
    int currentIndex = 0;
    for (String word : text) {
        for (char c : word.toCharArray()) {
            char_array[currentIndex++] = c;
        }
    }

    // Update the instance variable
    this.charCount = totalLength;

    return char_array;
}

/**
 * Update method to recalculate analysis based on a new linked list of words.
 * @param wordBuffer A linked list containing words for analysis.
 */  
public void update(LinkedList wordBuffer){
  //get the text
  String[] text = getText(wordBuffer);
  //get the char
  char[] char_array = getChar(text);
  //get the char count
  //get the line count
  lineCount = getLineCount(char_array);
}

  /**
   * Getter for character count.
   * @return The total number of characters in the document.
   */
  public int get_char_count(){
    return charCount;
  }
  /**
   * Getter for word count.
   * @return The total number of words in the document.
   */
  public int get_word_count(){
    return wordCount;
  }
  /**
   * Getter for line count.
   * @return The total number of lines in the document.
   */
  public int get_line_count(){
    return lineCount;
  }

  
}
