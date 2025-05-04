import Backend.Word_Object;

public class action {

    private Word_Object word;
    private boolean handled;
    private int buttonType;

    public action(Word_Object word, int buttonType) {
        this.word = word;
        this.buttonType = buttonType;
        this.handled = false;
    }

    public Word_Object getWord() {
        return word;
    }

    public boolean isHandled() {
        return handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }

    public int getButtonType() {
        return buttonType;
    }

    //this needs to be developed still
    public void handle() {
        Handle_Click.handle(this);
    }
}
