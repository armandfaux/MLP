import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class ActivationTest {
    @Test
    public void testSigmoidAtZero() {
        double result = Activation.sigmoid(0);
        assertEquals(0.5, result, 1e-9, "Sigmoid at 0 should be 0.5");
    }

    @Test
    public void testSigmoidAtPositive() {
        double result = Activation.sigmoid(2);
        assertTrue(result > 0.5 && result < 1.0, "Sigmoid at positive values should be between 0.5 and 1");
    }

    @Test
    public void testSigmoidAtNegative() {
        double result = Activation.sigmoid(-2);
        assertTrue(result > 0.0 && result < 0.5, "Sigmoid at negative values should be between 0 and 0.5");
    }

    @Test
    public void testSigmoidAtLargePositive() {
        double result = Activation.sigmoid(1000);
        assertEquals(1.0, result, 1e-9, "Sigmoid at large positive values should approach 1");
    }

    @Test
    public void testSigmoidAtLargeNegative() {
        double result = Activation.sigmoid(-1000);
        assertEquals(0.0, result, 1e-9, "Sigmoid at large negative values should approach 0");
    }
}
