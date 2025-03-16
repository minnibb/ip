package luke;

/**
 * Main class for the Luke task manager application.
 * Handles the command loop and processing of user inputs.
 */
public class Luke {
    // Constants for command prefixes to avoid magic strings
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_MARK = "mark ";
    private static final String COMMAND_UNMARK = "unmark ";
    private static final String COMMAND_TODO = "todo ";
    private static final String COMMAND_DEADLINE = "deadline ";
    private static final String COMMAND_EVENT = "event ";
    private static final String COMMAND_DELETE = "delete ";
    private static final String COMMAND_FIND = "find ";
    private static final String COMMAND_SORT = "sort ";

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
                isExit = processCommand(input);
            } catch (LukeException e) {
                ui.showError(e.getMessage());
            }

            ui.showLine();
        }
    }

    /**
     * Processes a user command.
     *
     * @param input The user input command
     * @return true if the program should exit, false otherwise
     * @throws LukeException If command processing fails
     */
    private boolean processCommand(String input) throws LukeException {
        // Guard clause for null or empty input
        if (input == null || input.trim().isEmpty()) {
            ui.showError("Please enter a command");
            return false;
        }

        if (input.equals(COMMAND_BYE)) {
            ui.showBye();
            return true;
        }

        if (input.equals(COMMAND_LIST)) {
            listTasks();
            return false;
        }

        if (input.startsWith(COMMAND_MARK) || input.startsWith(COMMAND_UNMARK)) {
            handleMarkCommand(input);
            return false;
        }

        if (input.startsWith(COMMAND_TODO)) {
            handleTodoCommand(input);
            return false;
        }

        if (input.startsWith(COMMAND_DEADLINE)) {
            handleDeadlineCommand(input);
            return false;
        }

        if (input.startsWith(COMMAND_EVENT)) {
            handleEventCommand(input);
            return false;
        }

        if (input.startsWith(COMMAND_DELETE)) {
            handleDeleteCommand(input);
            return false;
        }

        if (input.startsWith(COMMAND_FIND)) {
            handleFindCommand(input);
            return false;
        }

        // New sort command handler
        if (input.startsWith(COMMAND_SORT)) {
            handleSortCommand(input);
            return false;
        }

        // Default case for unrecognized command
        ui.showError("I don't know what that means! Try again.");
        return false;
    }

    /**
     * Lists all tasks.
     */
    private void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("No tasks in your list!");
            return;
        }

        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            showTaskWithIndex(i + 1, task);
        }
    }

    /**
     * Shows a task with its index.
     *
     * @param index 1-based index to display
     * @param task Task to display
     */
    private void showTaskWithIndex(int index, Task task) {
        System.out.println(index + "." + task.toString());
    }

    /**
     * Handles sort command.
     *
     * @param input Command string (e.g., "sort date" or "sort description")
     * @throws LukeException If command format is invalid
     */
    private void handleSortCommand(String input) throws LukeException {
        if (input.length() <= 5) {
            throw new LukeException("Please specify how to sort! Try 'sort date' or 'sort description'.");
        }

        String sortCriteria = input.substring(5).trim().toLowerCase();

        if (tasks.isEmpty()) {
            System.out.println("No tasks to sort!");
            return;
        }

        switch (sortCriteria) {
            case "date":
                tasks.sortByDate();
                System.out.println("Tasks sorted by date!");
                break;
            case "description":
                tasks.sortByDescription();
                System.out.println("Tasks sorted by description!");
                break;
            case "type":
                tasks.sortByType();
                System.out.println("Tasks sorted by type!");
                break;
            default:
                throw new LukeException("Unknown sort criteria. Try 'sort date', 'sort description', or 'sort type'.");
        }

        listTasks();
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
            int taskNum = extractTaskNumber(input);
            validateTaskIndex(taskNum);

            boolean isMarkCommand = input.startsWith(COMMAND_MARK);
            updateTaskMarkStatus(taskNum, isMarkCommand);

            saveTasksToStorage();

        } catch (NumberFormatException e) {
            throw new LukeException("Please give me a valid task number! Try again.");
        }
    }

    /**
     * Extracts the task number from the command string.
     *
     * @param input Command string
     * @return Zero-based task index
     */
    private int extractTaskNumber(String input) {
        String[] parts = input.split(" ");
        return Integer.parseInt(parts[1]) - 1; // Convert to zero-based index
    }

    /**
     * Validates if a task index is valid.
     *
     * @param taskIndex Zero-based task index
     * @throws LukeException If task index is invalid
     */
    private void validateTaskIndex(int taskIndex) throws LukeException {
        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw new LukeException("That task number does not exist! Try again.");
        }
    }

    /**
     * Updates the mark status of a task.
     *
     * @param taskIndex Zero-based task index
     * @param isMarking True to mark as done, false to mark as not done
     */
    private void updateTaskMarkStatus(int taskIndex, boolean isMarking) {
        if (isMarking) {
            tasks.markTaskAsDone(taskIndex);
            System.out.println("Nice! I've marked this task as done:");
        } else {
            tasks.markTaskAsNotDone(taskIndex);
            System.out.println("OK, I've marked this task as not done yet:");
        }
        System.out.println(tasks.getTask(taskIndex));
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

        String description = input.substring(COMMAND_TODO.length());
        Task newTask = new Task(description, Task.TYPE_TODO);

        addTaskAndShowFeedback(newTask);
    }

    /**
     * Handles deadline command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleDeadlineCommand(String input) throws LukeException {
        Task newTask = Parser.parseDeadline(input);
        addTaskAndShowFeedback(newTask);
    }

    /**
     * Handles event command.
     *
     * @param input Command string
     * @throws LukeException If command format is invalid
     */
    private void handleEventCommand(String input) throws LukeException {
        Task newTask = Parser.parseEvent(input);
        addTaskAndShowFeedback(newTask);
    }

    /**
     * Adds a task and shows feedback to the user.
     *
     * @param task Task to add
     * @throws LukeException If saving fails
     */
    private void addTaskAndShowFeedback(Task task) throws LukeException {
        tasks.addTask(task);
        showTaskAddedMessage(task);
        saveTasksToStorage();
    }

    /**
     * Shows a message that a task was added.
     *
     * @param task The added task
     */
    private void showTaskAddedMessage(Task task) {
        System.out.println("Noted. I've added this task:");
        System.out.println(task);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    /**
     * Saves current tasks to storage.
     *
     * @throws LukeException If saving fails
     */
    private void saveTasksToStorage() throws LukeException {
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
            int taskNum = extractTaskNumber(input);
            validateTaskIndex(taskNum);

            deleteTaskAndShowFeedback(taskNum);

        } catch (NumberFormatException e) {
            throw new LukeException("Please give me a valid task number! Try again.");
        }
    }

    /**
     * Deletes a task and shows feedback.
     *
     * @param taskIndex Zero-based task index
     * @throws LukeException If saving fails
     */
    private void deleteTaskAndShowFeedback(int taskIndex) throws LukeException {
        Task deletedTask = tasks.getTask(taskIndex);
        tasks.deleteTask(taskIndex);

        System.out.println("Noted. I've removed this task:");
        System.out.println(deletedTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");

        saveTasksToStorage();
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

        String keyword = input.substring(COMMAND_FIND.length()).trim().toLowerCase();
        findAndDisplayMatchingTasks(keyword);
    }

    /**
     * Finds and displays tasks matching the keyword.
     *
     * @param keyword Keyword to search for
     */
    private void findAndDisplayMatchingTasks(String keyword) {
        boolean found = false;
        System.out.println("Here are the matching tasks in your list:");

        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.getTask(i);
            String description = task.getDescription().toLowerCase();

            if (description.contains(keyword)) {
                showTaskWithIndex(i + 1, task);
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
    public void addMultipleTasks(Task... newTasks) {
        for (Task task : newTasks) {
            this.tasks.addTask(task);
            System.out.println("Added: " + task);
        }

        System.out.println("Now you have " + this.tasks.size() + " tasks in the list.");

        try {
            saveTasksToStorage();
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