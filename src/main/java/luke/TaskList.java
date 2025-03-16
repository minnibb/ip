package luke;

import java.util.ArrayList;

/**
 * Manages a list of tasks.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates a new empty task list.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Creates a task list with existing tasks.
     *
     * @param tasks The list of tasks to start with
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add
     */
    public void addTask(Task task) {
        tasks.add(task);
    }

    /**
     * Removes a task from the list.
     *
     * @param index The index of the task to remove
     */
    public void deleteTask(int index) {
        tasks.remove(index);
    }

    /**
     * Gets a specific task from the list.
     *
     * @param index The index of the task to get
     * @return The task at the specified index
     */
    public Task getTask(int index) {
        return tasks.get(index);
    }

    /**
     * Gets the number of tasks in the list.
     *
     * @return The number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Checks if the task list is empty.
     *
     * @return true if the list has no tasks, false otherwise
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Gets all tasks.
     *
     * @return List of all tasks
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    /**
     * Marks a task as done.
     *
     * @param index The index of the task to mark as done
     */
    public void markTaskAsDone(int index) {
        tasks.get(index).markAsDone();
    }

    /**
     * Marks a task as not done.
     *
     * @param index The index of the task to mark as not done
     */
    public void markTaskAsNotDone(int index) {
        tasks.get(index).markAsNotDone();
    }

    /**
     * Sorts tasks by their date/time (for deadline and event tasks).
     * Tasks without dates come first.
     */
    public void sortByDate() {
        tasks.sort((t1, t2) -> {
            // Tasks without dates first
            if (t1.getTime().isEmpty() && !t2.getTime().isEmpty()) {
                return -1;
            }
            if (!t1.getTime().isEmpty() && t2.getTime().isEmpty()) {
                return 1;
            }
            if (t1.getTime().isEmpty() && t2.getTime().isEmpty()) {
                return 0;
            }
            // Otherwise compare dates
            return t1.getTime().compareTo(t2.getTime());
        });
    }

    /**
     * Sorts tasks alphabetically by description.
     */
    public void sortByDescription() {
        tasks.sort((t1, t2) -> t1.getDescription().compareToIgnoreCase(t2.getDescription()));
    }

    /**
     * Sorts tasks by type (todo, deadline, event).
     * Maintains stable ordering within each type.
     */
    public void sortByType() {
        tasks.sort((t1, t2) -> t1.getType().compareTo(t2.getType()));
    }
}