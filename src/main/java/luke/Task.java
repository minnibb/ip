package luke;

/**
 * Represents a task in Luke.
 * Can be a todo, deadline, or event.
 */
public class Task {
    // Constants for task types
    public static final String TYPE_TODO = "T";
    public static final String TYPE_DEADLINE = "D";
    public static final String TYPE_EVENT = "E";

    private String description;
    private boolean isDone;
    private String type;  // "T", "D", or "E"
    private String time;  // For deadlines and events

    /**
     * Creates a new task.
     *
     * @param description What the task is about
     * @param type Type of task: "T" (todo), "D" (deadline), "E" (event)
     */
    public Task(String description, String type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.time = "";
    }

    /**
     * Gets the task description.
     *
     * @return Task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if task is completed.
     *
     * @return true if done, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Gets the task type.
     *
     * @return "T", "D", or "E"
     */
    public String getType() {
        return type;
    }

    /**
     * Gets the task time/date.
     *
     * @return Time string for the task
     */
    public String getTime() {
        return time;
    }

    /**
     * Sets the task time/date.
     *
     * @param time Time string to set
     */
    public void setTime(String time) {
        this.time = time;
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns the status display for the task.
     *
     * @return Status string [X] or [ ]
     */
    private String getStatusString() {
        return isDone ? "[X]" : "[ ]";
    }

    /**
     * Formats the task as a string.
     *
     * @return String with task type, status, description and time
     */
    @Override
    public String toString() {
        // Returns formatted task with type marker, status, and details
        String status = getStatusString();

        if (type.equals(TYPE_TODO)) {
            return "[T]" + status + " " + description;
        } else if (type.equals(TYPE_DEADLINE)) {
            return "[D]" + status + " " + description + " (by: " + time + ")";
        } else {
            return "[E]" + status + " " + description + " (from: " + time + ")";
        }
    }
}