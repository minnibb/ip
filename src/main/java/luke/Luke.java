package luke;

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
        assert filePath != null : "File path cannot be null";

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
            assert tasks.getTask(i) != null : "Task cannot be null";
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

            // Add assertions to validate task index
            assert taskNum >= 0 : "Task number should be positive";

            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new LukeException("That task number does not exist! Try again.");
            }

            // At this point, we're sure the task index is valid
            assert tasks.getTask(taskNum) != null : "Task at index " + taskNum + " should not be null";

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
        assert description != null && !description.isEmpty() : "Task description cannot be empty";

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
        assert input != null && input.startsWith("deadline ") : "Input should start with 'deadline '";

        Task newTask = Parser.parseDeadline(input);
        assert newTask != null : "Parser should not return null task";

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
        assert input != null && input.startsWith("event ") : "Input should start with 'event '";

        Task newTask = Parser.parseEvent(input);
        assert newTask != null : "Parser should not return null task";

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

            // Add assertions to validate task index
            assert taskNum >= 0 : "Task number should be positive";

            if (taskNum < 0 || taskNum >= tasks.size()) {
                throw new LukeException("That task number does not exist! Try again.");
            }

            // At this point, we're sure the task index is valid
            assert tasks.getTask(taskNum) != null : "Task at index " + taskNum + " should not be null";

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

        String keyword = input.substring(5).trim().toLowerCase();
        assert keyword != null && !keyword.isEmpty() : "Search keyword cannot be empty";

        boolean found = false;

        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            assert task != null : "Task cannot be null";

            String description = task.getDescription().toLowerCase();
            assert description != null : "Task description cannot be null";

            if (description.contains(keyword)) {
                System.out.println((i + 1) + "." + task.toString());
                found = true;
            }
        }

        if (!found) {
            System.out.println("No matching tasks found!");
        }
    }

    /**
     * Adds multiple tasks at once.
     *
     * @param tasks The tasks to add
     */
    public void addMultipleTasks(Task... tasks) {
        assert tasks != null : "Tasks array cannot be null";

        for (Task task : tasks) {
            assert task != null : "Task cannot be null";
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