package luke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles saving and loading tasks from file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a storage handler with specified file path.
     * Makes a data folder if it doesn't exist yet.
     *
     * @param filePath Path to the save file
     */
    public Storage(String filePath) {
        assert filePath != null : "File path cannot be null";

        this.filePath = filePath;
        File directory = new File("./data");
        if (!directory.exists()) {
            boolean created = directory.mkdir();
            assert created : "Failed to create data directory";
        }
    }

    /**
     * Loads tasks from the save file.
     *
     * @return List of tasks from the file
     * @throws LukeException If file can't be read
     */
    public ArrayList<Task> load() throws LukeException {
        ArrayList<Task> tasks = new ArrayList<>();
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                return tasks;
            }

            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                assert line != null : "Line read from file cannot be null";

                String[] parts = line.split(" \\| ");
                assert parts.length >= 3 : "Invalid file format: each line should have at least 3 parts";

                String type = parts[0];
                assert type.equals("T") || type.equals("D") || type.equals("E") :
                        "Task type must be T, D, or E, but was " + type;

                String doneStatus = parts[1];
                assert doneStatus.equals("0") || doneStatus.equals("1") :
                        "Done status must be 0 or 1, but was " + doneStatus;

                String description = parts[2];
                assert description != null && !description.isEmpty() : "Task description cannot be empty";

                Task task = new Task(description, type);
                if (doneStatus.equals("1")) {
                    task.markAsDone();
                }
                if (parts.length > 3) {
                    String time = parts[3];
                    assert time != null : "Time part cannot be null";
                    task.setTime(time);
                }
                tasks.add(task);
            }
            fileScanner.close();
        } catch (IOException e) {
            throw new LukeException("Something went wrong loading the file!");
        }
        return tasks;
    }

    /**
     * Saves tasks to the save file.
     *
     * @param tasks List of tasks to save
     * @throws LukeException If file can't be written
     */
    public void save(ArrayList<Task> tasks) throws LukeException {
        assert tasks != null : "Tasks list cannot be null";

        try {
            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
                assert task != null : "Task in the list cannot be null";

                writer.write(task.getType() + " | " +
                        (task.isDone() ? "1" : "0") + " | " +
                        task.getDescription() +
                        (task.getTime().isEmpty() ? "" : " | " + task.getTime()) +
                        "\n");
            }
            writer.close();
        } catch (IOException e) {
            throw new LukeException("Something went wrong saving the file!");
        }
    }
}