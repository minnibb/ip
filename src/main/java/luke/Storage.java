package luke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles loading and saving tasks from file.
 */
public class Storage {
    private final String filePath;

    /**
     * Creates a storage handler with specified file path.
     * Makes a data folder if it doesn't exist.
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
                String line = fileScanner.nextLine();
                String[] parts = line.split(" \\| ");

                Task task = new Task(parts[2], parts[0]);
                if (parts[1].equals("1")) {
                    task.markAsDone();
                }
                if (parts.length > 3) {
                    task.setTime(parts[3]);
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
        try {
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