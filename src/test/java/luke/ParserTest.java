package luke;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void testParseEvent() {
        try {
            Task result = Parser.parseEvent("event meeting /from 2/12/2025 1400 /to 1600");
            assertEquals("meeting", result.getDescription(),
                    "Task description should be 'meeting'");
        } catch (LukeException e) {
            fail("Should not fail for valid input");
        }
    }
}