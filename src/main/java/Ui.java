import java.util.Scanner;

public class Ui {
    private Scanner scanner;

    public Ui() {
        scanner = new Scanner(System.in);
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showWelcome() {
        showLine();
        System.out.println("Hello! I'm Luke");
        System.out.println("What can I do for you today?");
        showLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showLoadingError() {
        System.out.println("Something went wrong loading the file!");
    }

    public void showSavingError() {
        System.out.println("Something went wrong saving the file!");
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showBye() {
        System.out.println("Bye. Hope to see you again!!");
    }
}