package GameTime;

import org.junit.jupiter.api.Test;
import simplicity.Model.GameTime.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DateTest {

    @Test
    public void testSetDate() {
        Date date = new Date(0, 0, 0);
        date.setDate(1, 1, 1);
        assertEquals(1, date.getYear());
        assertEquals(1, date.getDay());
        assertEquals(1, date.getHour());
    }

    @Test
    public void testAddToYear() {
        Date date = new Date(0, 0, 0);
        date.addToYear(5);
        assertEquals(5, date.getYear());
        date.addToYear(-2);
        assertEquals(3, date.getYear());
    }
}
