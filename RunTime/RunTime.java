import java.util.LinkedList;

import Backend.Document;

public class RunTime {
    private String save_path;
    private boolean running;

    // Associations with other classes
    private Handle_Click handle_click;
    private manageFile manage_file;
    private UI ui;

    // Constructor
    public RunTime() {
        // Initialization of attributes and associations
        this.save_path = "";
        this.running = false;
        
        //call the start method
    }

    // Methods
    public void start() {
        
        //initialize the UI object
        ui = new UI(this);

        //get words from the UI
        String[] words = ui.getWords();

        //initialize the document class
        Document document = new Document(words);
    }

    public void watch() {
        // Implementation of watch method
    }

    public void updateLibrary(String libraryPath) {
        // Implementation of updateLibrary
    }

    public LinkedList<String> runSpellCheck(String text) {
        // Implementation of runSpellCheck
        return new LinkedList<>();
    }

    public boolean saveFile(String filePath) {
        // Implementation of saveFile
        return true;
    }

    public String[] loadFile(String filePath) {
        // Implementation of loadFile
        return new String[0];
    }

    public void handleAction(Action action) {
        // Implementation of handleAction
    }

    public void populateUI(LinkedList<String> data) {
        // Implementation of populateUI
    }

    // Other necessary methods and functionalities
}
