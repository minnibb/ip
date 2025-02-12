import java.util.Scanner;
import java.io.*;

public class Luke {
    private static String[] tasks = new String[100];
    private static boolean[] taskStatus = new boolean[100];
    private static String[] taskType = new String[100];
    private static String[] taskTime = new String[100];
    private static int numberOfTasks = 0;
    private static final String FILE_PATH = "./data/luke.txt";

    static {
        File directory = new File("./data");
        if (!directory.exists()) {
            directory.mkdir();
        }
        loadTasks();
    }

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
                if (input.length() <= 5) {
                    System.out.println("Please tell me what to do! Try again.");
                } else {
                    addTodo(input);
                }
            } else if (input.startsWith("deadline ")) {
                if (!input.contains("/by")) {
                    System.out.println("Please tell me the deadline using /by after a task! Try again.");
                } else {
                    addDeadline(input);
                }
            } else if (input.startsWith("event ")) {
                if (!input.contains("/from") || !input.contains("/to")) {
                    System.out.println("Please tell me the time using /from and /to! Try again.");
                } else {
                    addEvent(input);
                }
            } else if (input.startsWith("delete ")) {
                if (input.length() <= 7) {
                    System.out.println("Please tell me which task to delete! Try again.");
                } else {
                    try {
                        int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
                        if (taskNum < 0 || taskNum >= numberOfTasks) {
                            System.out.println("That task number does not exist! Try again.");
                        } else {
                            deleteTask(taskNum);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please give me a valid task number! Try again.");
                    }
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
        saveTasks();
    }

    private static void addDeadline(String input) {
        String[] parts = input.substring(9).split(" /by ");
        String task = parts[0];
        String dateTime = parts[1];

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            System.out.println("Please use format: day/month/year time (e.g., 2/12/2019 1800)");
            return;
        }

        String[] date = dateParts[0].split("/");
        String time = dateParts[1];

        String monthName = getMonthName(date[1]);
        String formattedTime = formatTime(time);

        String formattedDate = monthName + " " + date[0] + " " + date[2] + ", " + formattedTime;

        tasks[numberOfTasks] = task;
        taskType[numberOfTasks] = "D";
        taskTime[numberOfTasks] = formattedDate;

        System.out.println("Noted. I've added this task:");
        System.out.println("[D][ ] " + task + " (by: " + formattedDate + ")");
        numberOfTasks++;
        System.out.println("You have " + numberOfTasks + " tasks in the list.");
        saveTasks();
    }

    private static void addEvent(String input) {
        String[] parts = input.substring(6).split(" /from ");
        String[] timeParts = parts[1].split(" /to ");
        String task = parts[0];
        String dateTime = timeParts[0];

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            System.out.println("Please use format: day/month/year time (e.g., 2/12/2019 1400)");
            return;
        }

        String[] date = dateParts[0].split("/");
        String time = dateParts[1];

        String monthName = getMonthName(date[1]);
        String formattedTime = formatTime(time);

        String formattedDate = monthName + " " + date[0] + " " + date[2] + ", " + formattedTime;

        tasks[numberOfTasks] = task;
        taskType[numberOfTasks] = "E";
        taskTime[numberOfTasks] = formattedDate;

        System.out.println("Noted. I've added this task:");
        System.out.println("[E][ ] " + task + " (from: " + formattedDate + ")");
        numberOfTasks++;
        System.out.println("You have " + numberOfTasks + " tasks in the list.");
        saveTasks();
    }

    private static String getMonthName(String monthNumber) {
        switch(monthNumber) {
            case "1": return "Jan";
            case "2": return "Feb";
            case "3": return "Mar";
            case "4": return "Apr";
            case "5": return "May";
            case "6": return "Jun";
            case "7": return "Jul";
            case "8": return "Aug";
            case "9": return "Sep";
            case "10": return "Oct";
            case "11": return "Nov";
            case "12": return "Dec";
            default: return monthNumber;
        }
    }

    private static String formatTime(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        if (hour < 0 || hour > 23) {
            return time;
        }
        return (hour <= 12 ? hour : hour - 12) + (hour < 12 ? "AM" : "PM");
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
            saveTasks();
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
            saveTasks();
        }
    }

    private static void deleteTask(int taskNum) {
        String deletedTask = "";
        if (taskType[taskNum].equals("T")) {
            deletedTask = "[T]" + (taskStatus[taskNum] ? "[X] " : "[ ] ") + tasks[taskNum];
        } else if (taskType[taskNum].equals("D")) {
            deletedTask = "[D]" + (taskStatus[taskNum] ? "[X] " : "[ ] ") + tasks[taskNum] + " (by: " + taskTime[taskNum] + ")";
        } else if (taskType[taskNum].equals("E")) {
            deletedTask = "[E]" + (taskStatus[taskNum] ? "[X] " : "[ ] ") + tasks[taskNum] + " (from: " + taskTime[taskNum] + ")";
        }

        for (int i = taskNum; i < numberOfTasks - 1; i++) {
            tasks[i] = tasks[i + 1];
            taskStatus[i] = taskStatus[i + 1];
            taskType[i] = taskType[i + 1];
            taskTime[i] = taskTime[i + 1];
        }

        numberOfTasks--;

        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + numberOfTasks + " tasks in the list.");
        saveTasks();
    }

    private static void saveTasks() {
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            for (int i = 0; i < numberOfTasks; i++) {
                writer.write(taskType[i] + " | " +
                        (taskStatus[i] ? "1" : "0") + " | " +
                        tasks[i] +
                        (taskTime[i].isEmpty() ? "" : " | " + taskTime[i]) +
                        "\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Something went wrong saving the file!");
        }
    }

    private static void loadTasks() {
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                return;
            }

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                taskType[numberOfTasks] = parts[0];
                taskStatus[numberOfTasks] = parts[1].equals("1");
                tasks[numberOfTasks] = parts[2];
                taskTime[numberOfTasks] = parts.length > 3 ? parts[3] : "";

                numberOfTasks++;
            }
            fileScanner.close();
        } catch (IOException e) {
            System.out.println("Something went wrong loading the file!");
        }
    }

    private static void printLine() {
        System.out.println("____________________________________________________________");
    }
}