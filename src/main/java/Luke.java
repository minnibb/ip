import java.util.Scanner;

public class Luke {
    private static String[] tasks = new String[100];
    private static int taskCount = 0;

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
            } else if (input.equals("list")) {
                listTasks();
            } else {
                tasks[taskCount] = input;
                System.out.println("added: " + input);
                taskCount++;
            }
            printLine();
        }
        scanner.close();
    }

    private static void listTasks() {
        if (taskCount == 0) {
            System.out.println("No tasks in your list!");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks[i]);
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}