package Backend;
public class LinkedList {
    private Word_Object head; // Head of the list
    private Word_Object tail; // Tail of the list
    
    /**
     * Constructs an empty linked list.
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * Adds a new node to the end of the list.
     * @param newNode The Word_Object to be added.
     */
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

    /**
     * Adds a new node to the beginning of the list.
     * @param newNode The Word_Object to be added.
     */
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

    /**
     * Gets the head of the list.
     * @return The head Word_Object of the list.
     */
    public Word_Object getHead() {
        return head;
    }

    /**
     * Gets the tail of the list.
     * @return The tail Word_Object of the list.
     */
    public Word_Object getTail() {
        return tail;
    }

    /**
     * Gets the length of the linked list.
     * @return The length of the linked list.
     */
    public int get_length() {
        int length = 0;
        Word_Object curr = head;
        while (curr != null) {
            length++;
            curr = curr.getNext_node();
        }
        return length;
    }

    /**
     * Calculates and sets the start and end indices for each Word_Object in the linked list.
     */
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

    /**
     * Removes a Word_Object from the linked list.
     * @param word The Word_Object to be removed.
     */
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
    
    /**
     * Gets the Word_Object at a specified index in the text.
     * @param index The index in the text.
     * @return The Word_Object at the specified index.
     */
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
    /**
     * Gets the Word_Object at the caret position in the text.
     * @param caretPosition The caret position in the text.
     * @return The Word_Object at the caret position.
     */
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
    
    /**
     * Updates the Word_Object at a specified caret position with a new Word_Object.
     * @param caretPosition The caret position in the text.
     * @param newWord       The new Word_Object to replace the existing one.
     */
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

    /**
     * Replaces an old Word_Object with a new Word_Object in the linked list.
     * @param oldWord The old Word_Object to be replaced.
     * @param newWord The new Word_Object to replace the old one.
     */
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

}
