package luke;

import java.util.Scanner;

/**
 * Handles user interaction
 */
public class Ui {
    private Scanner scanner;

    /**
     * Creates a new UI handler.
     */
    public Ui() {
        scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from the user.
     *
     * @return The command entered by the user
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Shows the welcome message.
     */
    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Luke");
        System.out.println("What can I do for you today?");
        showLine();
    }

    /**
     * Shows a separator line.
     */
    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Shows an error message for file loading issues.
     */
    public void showLoadingError() {
        System.out.println("Something went wrong loading the file!");
    }

    /**
     * Shows an error message for file saving issues.
     */
    public void showSavingError() {
        System.out.println("Something went wrong saving the file!");
    }

    /**
     * Shows an error message.
     *
     * @param message The error message to display
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Shows the goodbye message.
     */
    public void showBye() {
        System.out.println("Bye. Hope to see you again!!");
    }
}