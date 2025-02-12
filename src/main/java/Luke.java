public class Luke {
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

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
                } else {
                    ui.showError("I don't know what that means! Try again.");
                }
            } catch (LukeException e) {
                ui.showError(e.getMessage());
            }
            ui.showLine();
        }
    }

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

    private void handleDeadlineCommand(String input) throws LukeException {
        Task newTask = Parser.parseDeadline(input);
        tasks.addTask(newTask);
        System.out.println("Noted. I've added this task:");
        System.out.println(newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

    private void handleEventCommand(String input) throws LukeException {
        Task newTask = Parser.parseEvent(input);
        tasks.addTask(newTask);
        System.out.println("Noted. I've added this task:");
        System.out.println(newTask);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
        storage.save(tasks.getTasks());
    }

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

    public static void main(String[] args) {
        new Luke("data/luke.txt").run();
    }
}