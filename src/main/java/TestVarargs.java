package luke;

public class TestVarargs {
    public static void main(String[] args) {
        // Test UI's showMessages
        Ui ui = new Ui();
        System.out.println("Testing Ui.showMessages:");
        ui.showMessages("Message 1", "Message 2", "Message 3");

        // Test Parser's findTasksByKeywords
        TaskList tasks = new TaskList();
        tasks.addTask(new Task("Meeting with team", "T"));
        tasks.addTask(new Task("Complete assignment", "T"));
        tasks.addTask(new Task("Prepare presentation", "T"));

        System.out.println("\nTesting Parser.findTasksByKeywords:");
        var results = Parser.findTasksByKeywords(tasks, "meeting", "assignment");
        System.out.println("Found " + results.size() + " tasks with keywords 'meeting' or 'assignment'");
        for (Task task : results) {
            System.out.println("- " + task);
        }

        // Test Luke's addMultipleTasks
        Luke luke = new Luke("data/test.txt");
        System.out.println("\nTesting Luke.addMultipleTasks:");
        Task task1 = new Task("Test task 1", "T");
        Task task2 = new Task("Test task 2", "T");
        luke.addMultipleTasks(task1, task2);
    }
}
