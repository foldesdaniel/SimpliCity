package GameTime;

import org.junit.jupiter.api.Test;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class InGameTimeTest {

    @Test
    public void testConstructor() {
        InGameTime inGameTime = new InGameTime(1, 1, 1);
        assertEquals(1, inGameTime.getInGameYear());
        assertEquals(1, inGameTime.getInGameDay());
        assertEquals(1, inGameTime.getInGameHour());
    }

    @Test
    public void testSetInGameTime() {
        InGameTime inGameTime = new InGameTime(1, 1, 1);
        inGameTime.setInGameTime(2, 2, 2);
        assertEquals(2, inGameTime.getInGameYear());
        assertEquals(2, inGameTime.getInGameDay());
        assertEquals(2, inGameTime.getInGameHour());
    }

    @Test
    public void testStartAndStopInGameTime() throws InterruptedException {
        InGameTime inGameTime = new InGameTime();
        inGameTime.startInGameTime(InGameSpeeds.FAST);
        Thread.sleep(500);
        inGameTime.stopInGameTime();
        assertTrue(inGameTime.getInGameHour() > 0);
    }
}
