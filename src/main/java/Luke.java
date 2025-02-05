import java.util.Scanner;

public class Luke {
    private static String[] tasks = new String[100];
    private static boolean[] taskStatus = new boolean[100];
    private static String[] taskType = new String[100];
    private static String[] taskTime = new String[100];
    private static int numberOfTasks = 0;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        printLine();
        System.out.println("Hello! I'm Luke");
        System.out.println("What can I do for you today?");
        printLine();

        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine();
            printLine();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again!!");
                isRunning = false;
            } else if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                if (input.length() <= 5) {
                    System.out.println("Please indicate which task to mark! Try again.");
                } else {
                    try {
                        int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
                        if (taskNum < 0 || taskNum >= numberOfTasks) {
                            System.out.println("That task number does not exist! Try again.");
                        } else if (input.startsWith("mark ")) {
                            markAsDone(taskNum);
                        } else {
                            markAsNotDone(taskNum);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please give me a valid task number! Try again.");
                    }
                }
            } else if (input.startsWith("todo ")) {
                // Handle todo errors
                if (input.length() <= 5) { // just "todo "
                    System.out.println("Please tell me what to do! Try again.");
                } else {
                    addTodo(input);
                }
            } else if (input.startsWith("deadline ")) {
                // Handle deadline errors
                if (!input.contains("/by")) {
                    System.out.println("Please tell me the deadline using /by after a task! Try again.");
                } else {
                    addDeadline(input);
                }
            } else if (input.startsWith("event ")) {
                // Handle event errors
                if (!input.contains("/from") || !input.contains("/to")) {
                    System.out.println("Please tell me the time using /from and /to! Try again.");
                } else {
                    addEvent(input);
                }
            } else {
                System.out.println("I don't know what that means! Try again.");
            }
            printLine();
        }
        scanner.close();
    }

    private static void addTodo(String input) {
        String task = input.substring(5);
        tasks[numberOfTasks] = task;
        taskType[numberOfTasks] = "T";
        taskTime[numberOfTasks] = "";
        System.out.println("Noted. I've added this task:");
        System.out.println("[T][ ] " + task);
        numberOfTasks++;
        System.out.println("You have " + numberOfTasks + " tasks in the list.");
    }

    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split(" /by ");
        tasks[numberOfTasks] = parts[0];
        taskType[numberOfTasks] = "D";
        taskTime[numberOfTasks] = parts[1];
        System.out.println("Noted. I've added this task:");
        System.out.println("[D][ ] " + parts[0] + " (by: " + parts[1] + ")");
        numberOfTasks++;
        System.out.println("You have " + numberOfTasks + " tasks in the list.");
    }

    private static void addEvent(String input) {
        String[] parts = input.substring(6).split(" /from ");
        String[] timeParts = parts[1].split(" /to ");
        tasks[numberOfTasks] = parts[0];
        taskType[numberOfTasks] = "E";
        taskTime[numberOfTasks] = timeParts[0] + " to: " + timeParts[1];
        System.out.println("Noted. I've added this task:");
        System.out.println("[E][ ] " + parts[0] + " (from: " + timeParts[0] + " to: " + timeParts[1] + ")");
        numberOfTasks++;
        System.out.println("You have " + numberOfTasks + " tasks in the list.");
    }

    private static void listTasks() {
        if (numberOfTasks == 0) {
            System.out.println("No tasks in your list!");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < numberOfTasks; i++) {
            String status = taskStatus[i] ? "[X]" : "[ ]";
            if (taskType[i].equals("T")) {
                System.out.println((i + 1) + ".[T]" + status + " " + tasks[i]);
            } else if (taskType[i].equals("D")) {
                System.out.println((i + 1) + ".[D]" + status + " " + tasks[i] + " (by: " + taskTime[i] + ")");
            } else if (taskType[i].equals("E")) {
                System.out.println((i + 1) + ".[E]" + status + " " + tasks[i] + " (from: " + taskTime[i] + ")");
            }
        }
    }

    private static void markAsDone(int taskNum) {
        if (taskNum >= 0 && taskNum < numberOfTasks) {
            taskStatus[taskNum] = true;
            System.out.println("Nice! I've marked this task as done:");
            if (taskType[taskNum].equals("T")) {
                System.out.println("[T][X] " + tasks[taskNum]);
            } else if (taskType[taskNum].equals("D")) {
                System.out.println("[D][X] " + tasks[taskNum] + " (by: " + taskTime[taskNum] + ")");
            } else if (taskType[taskNum].equals("E")) {
                System.out.println("[E][X] " + tasks[taskNum] + " (from: " + taskTime[taskNum] + ")");
            }
        }
    }

    private static void markAsNotDone(int taskNum) {
        if (taskNum >= 0 && taskNum < numberOfTasks) {
            taskStatus[taskNum] = false;
            System.out.println("OK, I've marked this task as not done yet:");
            if (taskType[taskNum].equals("T")) {
                System.out.println("[T][ ] " + tasks[taskNum]);
            } else if (taskType[taskNum].equals("D")) {
                System.out.println("[D][ ] " + tasks[taskNum] + " (by: " + taskTime[taskNum] + ")");
            } else if (taskType[taskNum].equals("E")) {
                System.out.println("[E][ ] " + tasks[taskNum] + " (from: " + taskTime[taskNum] + ")");
            }
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}