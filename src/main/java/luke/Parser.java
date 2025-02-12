package luke;

public class Parser {
    public static Task parseDeadline(String input) throws LukeException {
        String[] parts = input.substring(9).split(" /by ");
        if (!input.contains("/by")) {
            throw new LukeException("Please tell me the deadline using /by after a task! Try again.");
        }
        String task = parts[0];
        String dateTime = parts[1];

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            throw new LukeException("Please use format: day/month/year time (e.g., 2/12/2019 1800)");
        }

        String[] date = dateParts[0].split("/");
        String time = dateParts[1];
        String formattedDate = formatDateTime(date, time);

        Task newTask = new Task(task, "D");
        newTask.setTime(formattedDate);
        return newTask;
    }

    public static Task parseEvent(String input) throws LukeException {
        if (!input.contains("/from") || !input.contains("/to")) {
            throw new LukeException("Please tell me the time using /from and /to! Try again.");
        }
        String[] parts = input.substring(6).split(" /from ");
        String[] timeParts = parts[1].split(" /to ");
        String task = parts[0];
        String dateTime = timeParts[0];

        String[] dateParts = dateTime.split(" ");
        if (dateParts.length != 2) {
            throw new LukeException("Please use format: day/month/year time (e.g., 2/12/2019 1400)");
        }

        String[] date = dateParts[0].split("/");
        String time = dateParts[1];
        String formattedDate = formatDateTime(date, time);

        Task newTask = new Task(task, "E");
        newTask.setTime(formattedDate);
        return newTask;
    }

    private static String formatDateTime(String[] date, String time) {
        String monthName = getMonthName(date[1]);
        String formattedTime = formatTime(time);
        return monthName + " " + date[0] + " " + date[2] + ", " + formattedTime;
    }

    private static String getMonthName(String monthNumber) {
        switch(monthNumber) {
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
            default: return monthNumber;
        }
    }

    private static String formatTime(String time) {
        int hour = Integer.parseInt(time.substring(0, 2));
        if (hour < 0 || hour > 23) {
            return time;
        }
        return (hour <= 12 ? hour : hour - 12) + (hour < 12 ? "AM" : "PM");
    }
}
