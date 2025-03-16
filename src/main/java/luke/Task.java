package luke;

/**
 * Represents a task in Luke.
 * Can be a todo, deadline, or event.
 */
public class Task {
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
        assert description != null : "Task description cannot be null";
        assert type != null : "Task type cannot be null";
        assert type.equals("T") || type.equals("D") || type.equals("E") :
                "Task type must be T, D, or E, but was " + type;

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
        assert description != null : "Description should not be null";
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
        assert type != null : "Type should not be null";
        return type;
    }

    /**
     * Gets the task time/date.
     *
     * @return Time string for the task
     */
    public String getTime() {
        assert time != null : "Time should not be null, even if empty";
        return time;
    }

    /**
     * Sets the task time/date.
     *
     * @param time Time string to set
     */
    public void setTime(String time) {
        assert time != null : "Time cannot be set to null";
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
     * Formats the task as a string.
     *
     * @return String with task type, status, description and time
     */
    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        assert type != null : "Type should not be null when converting to string";

        if (type.equals("T")) {
            return "[T]" + status + " " + description;
        } else if (type.equals("D")) {
            assert time != null && !time.isEmpty() : "Time should be set for deadline tasks";
            return "[D]" + status + " " + description + " (by: " + time + ")";
        } else {
            assert time != null && !time.isEmpty() : "Time should be set for event tasks";
            return "[E]" + status + " " + description + " (from: " + time + ")";
        }
    }
}