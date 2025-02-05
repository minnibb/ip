import java.util.Scanner;

public class Luke {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printLine();
        System.out.println("Hello! I'm Luke");
        System.out.println("What can I do for you?");
        printLine();

        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine();
            printLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                isRunning = false;
            } else {
                System.out.println(input);
            }
            printLine();
        }

        scanner.close();
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}
