package luke;

public class AssertionTest {
    public static void main(String[] args) {
        System.out.println("Testing if assertions are enabled...");

        // This should fail and terminate the program if assertions are enabled
        assert false : "This is a test assertion that should fail";

        // If we reach here, assertions are not enabled
        System.out.println("WARNING: Assertions are NOT enabled!");
    }
}
