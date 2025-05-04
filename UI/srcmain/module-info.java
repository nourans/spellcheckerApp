module spellapp.spellcheck {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires org.fxmisc.richtext;

    opens spellapp.spellcheck to javafx.fxml;
    exports spellapp.spellcheck;
}
