public class Task {
    protected String description;
    protected boolean isDone;
    protected String type;  // "T", "D", or "E"
    protected String time;  // For deadlines and events

    public Task(String description, String type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
        this.time = "";
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    @Override
    public String toString() {
        String status = isDone ? "[X]" : "[ ]";
        if (type.equals("T")) {
            return "[T]" + status + " " + description;
        } else if (type.equals("D")) {
            return "[D]" + status + " " + description + " (by: " + time + ")";
        } else {
            return "[E]" + status + " " + description + " (from: " + time + ")";
        }
    }
}