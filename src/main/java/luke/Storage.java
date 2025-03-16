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
        this.filePath = filePath;
        File directory = new File("./data");
        if (!directory.exists()) {
            directory.mkdir();
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
                try {
                    String line = fileScanner.nextLine();
                    if (line.trim().isEmpty()) {
                        continue; // Skip empty lines
                    }

                    String[] parts = line.split(" \\| ");
                    if (parts.length < 3) {
                        System.out.println("Warning: Skipping invalid line: " + line);
                        continue; // Skip invalid lines
                    }

                    Task task = new Task(parts[2], parts[0]);
                    if (parts[1].equals("1")) {
                        task.markAsDone();
                    }
                    if (parts.length > 3) {
                        task.setTime(parts[3]);
                    }
                    tasks.add(task);
                } catch (Exception e) {
                    // If one line fails, continue with the rest
                    System.out.println("Warning: Error parsing line in save file");
                }
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
        try {
            // Make sure the data directory exists
            File directory = new File("./data");
            if (!directory.exists()) {
                boolean created = directory.mkdir();
                if (!created) {
                    throw new LukeException("Could not create data directory for saving.");
                }
            }

            FileWriter writer = new FileWriter(filePath);
            for (Task task : tasks) {
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