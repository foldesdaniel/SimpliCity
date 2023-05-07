package GameTime;

import org.junit.jupiter.api.Test;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class InGameTimeManagerTest {

    @Test
    public void testGetInstance() {
        InGameTimeManager inGameTimeManager = InGameTimeManager.getInstance();
        assertNotNull(inGameTimeManager);
        InGameTimeManager inGameTimeManager2 = InGameTimeManager.getInstance();
        assertEquals(inGameTimeManager, inGameTimeManager2);
    }

    @Test
    public void testGetInGameTime() {
        InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
        assertNotNull(inGameTime);
        InGameTime inGameTime2 = InGameTimeManager.getInstance().getInGameTime();
        assertEquals(inGameTime, inGameTime2);
    }

}
