import java.util.Scanner;

public class Luke {
    private static String[] tasks = new String[100];
    private static boolean[] taskStatus = new boolean[100];
    private static int numberOfTasks = 0;

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
            } else if (input.startsWith("mark ")) {
                int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
                markAsDone(taskNum);
            } else if (input.startsWith("unmark ")) {
                int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
                markAsNotDone(taskNum);
            } else {
                tasks[numberOfTasks] = input;
                System.out.println("added: " + input);
                numberOfTasks++;
            }
            printLine();
        }
        scanner.close();
    }

    private static void listTasks() {
        if (numberOfTasks == 0) {
            System.out.println("No tasks in your list!");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numberOfTasks; i++) {
            System.out.println((i + 1) + "." + (taskStatus[i] ? "[X] " : "[ ] ") + tasks[i]);
        }
    }

    private static void markAsDone(int taskNum) {
        if (taskNum >= 0 && taskNum < numberOfTasks) {
            taskStatus[taskNum] = true;
            System.out.println("Nice! I've marked this task as done:");
            System.out.println("[X] " + tasks[taskNum]);
        }
    }

    private static void markAsNotDone(int taskNum) {
        if (taskNum >= 0 && taskNum < numberOfTasks) {
            taskStatus[taskNum] = false;
            System.out.println("OK, I've marked this task as not done yet:");
            System.out.println("[ ] " + tasks[taskNum]);
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}