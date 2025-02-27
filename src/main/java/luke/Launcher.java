package luke;

import javafx.application.Application;

/**
 * A launcher class to workaround classpath issues.
 * needed as the entry point for our JavaFX application.
 */
public class Launcher {
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
}