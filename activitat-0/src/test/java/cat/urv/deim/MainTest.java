package cat.urv.deim;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {
    @Test
    public void testPepito() {
        var main = new Main();
        assertEquals(7, main.suma(3, 4));
    }

    @Test
    public void testJulian() {
        var main = new Main();
        assertEquals(6, main.suma(2, 4));
    }
}
