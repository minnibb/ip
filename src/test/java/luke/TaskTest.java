package luke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {
    @Test
    public void testMarkAsDone() {
        Task task = new Task("test task", "T");
        task.markAsDone();

        assertTrue(task.isDone(), "Task should be marked as done");
    }
}