package luke;

import java.util.ArrayList;
/**
 * Main class for the Luke task manager application.
 * Handles the command loop and processing of user inputs.
 */
public class Luke {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a new Luke task manager instance.
     *
     * @param filePath Path to the save file
     */
    public Luke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        try {
            tasks = new TaskList(storage.load());
        } catch (LukeException e) {
            ui.showLoadingError();
            tasks = new TaskList();
        }
    }

    /**
     * Starts the command loop.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;

        while (!isExit) {
            String input = ui.readCommand();
            ui.showLine();

            try {
                if (input.equals("bye")) {
                    ui.showBye();
                    isExit = true;
                } else if (input.equals("list")) {
                    listTasks();
                } else if (input.startsWith("mark ") || input.startsWith("unmark ")) {
                    handleMarkCommand(input);
                } else if (input.startsWith("todo ")) {
                    handleTodoCommand(input);
                } else if (input.startsWith("deadline ")) {
                    handleDeadlineCommand(input);
                } else if (input.startsWith("event ")) {
                    handleEventCommand(input);
                } else if (input.startsWith("delete ")) {
                    handleDeleteCommand(input);
                } else if (input.startsWith("find ")) {
                    handleFindCommand(input);
                } else {
                    ui.showError("I don't know what that means! Try again.");
                }
            } catch (LukeException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

    /**
     * Lists all tasks.
     */
    private void listTasks() {
        if (tasks.size() == 0) {
            System.out.println("No tasks in your list!");
            return;
        }
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.getTask(i).toString());
        }
    }

    /**
     * Handles mark and unmark commands.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleMarkCommand(String input) throws LukeException {
        if (input.length() <= 5) {
            throw new LukeException("Please indicate which task to mark! Try again.");
        }
        try {
            int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new LukeException("That task number does not exist! Try again.");
            }
            if (input.startsWith("mark ")) {
                tasks.markTaskAsDone(taskNum);
                System.out.println("Nice! I've marked this task as done:");
            } else {
                tasks.markTaskAsNotDone(taskNum);
                System.out.println("OK, I've marked this task as not done yet:");
            }
            System.out.println(tasks.getTask(taskNum));
            storage.save(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new LukeException("Please give me a valid task number! Try again.");
        }
    }

    /**
     * Handles todo command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleTodoCommand(String input) throws LukeException {
        if (input.length() <= 5) {
            throw new LukeException("Please tell me what to do! Try again.");
        }
        String description = input.substring(5);
        Task newTask = new Task(description, "T");
        tasks.addTask(newTask);
        System.out.println("Noted. I've added this task:");
        System.out.println(newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * Handles deadline command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleDeadlineCommand(String input) throws LukeException {
        Task newTask = Parser.parseDeadline(input);
        tasks.addTask(newTask);
        System.out.println("Noted. I've added this task:");
        System.out.println(newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * Handles event command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleEventCommand(String input) throws LukeException {
        Task newTask = Parser.parseEvent(input);
        tasks.addTask(newTask);
        System.out.println("Noted. I've added this task:");
        System.out.println(newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    /**
     * Handles delete command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleDeleteCommand(String input) throws LukeException {
        if (input.length() <= 7) {
            throw new LukeException("Please tell me which task to delete! Try again.");
        }
        try {
            int taskNum = Integer.parseInt(input.split(" ")[1]) - 1;
            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new LukeException("That task number does not exist! Try again.");
            }
            Task deletedTask = tasks.getTask(taskNum);
            tasks.deleteTask(taskNum);
            System.out.println("Noted. I've removed this task:");
            System.out.println(deletedTask);
            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
            storage.save(tasks.getTasks());
        } catch (NumberFormatException e) {
            throw new LukeException("Please give me a valid task number! Try again.");
        }
    }

    /**
     * Handles find command to search for tasks containing keywords.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleFindCommand(String input) throws LukeException {
        if (input.length() <= 5) {
            throw new LukeException("Please enter a keyword to search for! Try again.");
        }

        String[] keywords = input.substring(5).trim().split("\\s+");
        ArrayList<Task> matchingTasks = Parser.findTasksByKeywords(tasks, keywords);

        if (matchingTasks.isEmpty()) {
            ui.showMessages("No matching tasks found!");
        } else {
            ui.showMessages(
                    "Here are the matching tasks in your list:",
                    "Found " + matchingTasks.size() + " matching tasks:"
            );
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println((i + 1) + "." + matchingTasks.get(i).toString());
            }
        }
    }

    /**
     * Adds multiple tasks at once.
     *
     * @param tasks The tasks to add
     */
    public void addMultipleTasks(Task... tasks) {
        for (Task task : tasks) {
            this.tasks.addTask(task);
            System.out.println("Added: " + task);
        }
        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");
        try {
            storage.save(this.tasks.getTasks());
        } catch (LukeException e) {
            ui.showError(e.getMessage());
        }
    }

    /**
     * Main entry point of the application.
     *
     * @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        new Luke("data/luke.txt").run();
    }
}