package luke;

/**
 * Parser class that converts user commands into Task objects.
 * Handles the formatting and validation of input strings.
 */
public class Parser {
    /**
     * Parses a deadline command and creates a deadline task.
     *
     * @param input User input in the format "deadline TASK /by DATE TIME"
     * @return A new deadline Task object
     * @throws LukeException If input format is wrong
     */
    public static Task parseDeadline(String input) throws LukeException {
        assert input != null : "Input cannot be null";
        assert input.startsWith("deadline ") : "Input should start with 'deadline '";

        if (!input.contains("/by")) {
            throw new LukeException("Please tell me the deadline using /by after a task! Try again.");
        }

        String[] parts = input.substring(9).split(" /by ");
        assert parts.length == 2 : "Deadline format should have exactly one /by separator";

        String task = parts[0];
        String dateTime = parts[1];
        assert task != null && !task.isEmpty() : "Task description cannot be empty";
        assert dateTime != null && !dateTime.isEmpty() : "Deadline date/time cannot be empty";

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            throw new LukeException("Please use format: day/month/year time (e.g., 2/12/2019 1800)");
        }

        String[] date = dateParts[0].split("/");
        assert date.length == 3 : "Date should have day, month, and year parts";

        String time = dateParts[1];
        String formattedDate = formatDateTime(date, time);
        assert formattedDate != null && !formattedDate.isEmpty() : "Formatted date should not be empty";

        Task newTask = new Task(task, "D");
        newTask.setTime(formattedDate);
        return newTask;
    }

    /**
     * Parses an event command and creates an event task.
     *
     * @param input User input in the format "event TASK /from DATE TIME /to END_TIME"
     * @return A new event Task object
     * @throws LukeException If input format is wrong
     */
    public static Task parseEvent(String input) throws LukeException {
        assert input != null : "Input cannot be null";
        assert input.startsWith("event ") : "Input should start with 'event '";

        if (!input.contains("/from") || !input.contains("/to")) {
            throw new LukeException("Please tell me the time using /from and /to! Try again.");
        }

        String[] parts = input.substring(6).split(" /from ");
        assert parts.length == 2 : "Event format should have exactly one /from separator";

        String[] timeParts = parts[1].split(" /to ");
        assert timeParts.length == 2 : "Event format should have exactly one /to separator";

        String task = parts[0];
        String dateTime = timeParts[0];
        assert task != null && !task.isEmpty() : "Task description cannot be empty";
        assert dateTime != null && !dateTime.isEmpty() : "Event date/time cannot be empty";

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            throw new LukeException("Please use format: day/month/year time (e.g., 2/12/2019 1400)");
        }

        String[] date = dateParts[0].split("/");
        assert date.length == 3 : "Date should have day, month, and year parts";

        String time = dateParts[1];
        String formattedDate = formatDateTime(date, time);
        assert formattedDate != null && !formattedDate.isEmpty() : "Formatted date should not be empty";

        Task newTask = new Task(task, "E");
        newTask.setTime(formattedDate);
        return newTask;
    }

    /**
     * Formats date and time into a readable string.
     *
     * @param date Array with [day, month, year]
     * @param time Time in 24-hour format (e.g., "1800")
     * @return Formatted date string like "Jan 1 2023, 6PM"
     */
    private static String formatDateTime(String[] date, String time) {
        assert date != null && date.length == 3 : "Date array must have day, month, and year";
        assert time != null && !time.isEmpty() : "Time cannot be empty";

        String monthName = getMonthName(date[1]);
        assert monthName != null : "Month name cannot be null";

        String formattedTime = formatTime(time);
        assert formattedTime != null : "Formatted time cannot be null";

        return monthName + " " + date[0] + " " + date[2] + ", " + formattedTime;
    }

    /**
     * Converts a month number to its name.
     *
     * @param monthNumber Month as string ("1" to "12")
     * @return Short month name (e.g., "Jan")
     */
    private static String getMonthName(String monthNumber) {
        assert monthNumber != null : "Month number cannot be null";

        switch (monthNumber) {
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
            default:
                assert false : "Invalid month number: " + monthNumber;
                return monthNumber;
        }
    }

    /**
     * Converts 24-hour time to 12-hour AM/PM format.
     *
     * @param time Time in 24-hour format
     * @return Time with AM/PM suffix
     */
    private static String formatTime(String time) {
        assert time != null && !time.isEmpty() : "Time cannot be empty";
        assert time.length() >= 2 : "Time should have at least 2 digits";

        int hour = Integer.parseInt(time.substring(0, 2));
        assert hour >= 0 && hour <= 23 : "Hour must be between 0 and 23, but was " + hour;

        if (hour < 0 || hour > 23) {
            return time;
        }
        return (hour <= 12 ? hour : hour - 12) + (hour < 12 ? "AM" : "PM");
    }

    /**
     * Searches for tasks matching any of the provided keywords.
     *
     * @param taskList The list of tasks to search
     * @param keywords Variable number of keywords to search for
     * @return ArrayList of tasks that match any keyword
     */
    public static java.util.ArrayList<Task> findTasksByKeywords(TaskList taskList, String... keywords) {
        assert taskList != null : "TaskList cannot be null";
        assert keywords != null : "Keywords array cannot be null";

        java.util.ArrayList<Task> matchingTasks = new java.util.ArrayList<>();

        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.getTask(i);
            assert task != null : "Task cannot be null";

            String description = task.getDescription().toLowerCase();
            assert description != null : "Task description cannot be null";

            for (String keyword : keywords) {
                assert keyword != null : "Keyword cannot be null";

                if (description.contains(keyword.toLowerCase())) {
                    matchingTasks.add(task);
                    break;  // Once we find a match, no need to check other keywords
                }
            }
        }

        return matchingTasks;
    }
}