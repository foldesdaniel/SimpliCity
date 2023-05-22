package FinancesTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Finances.FinanceData;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinanceDataTest {
    @Test
    public void testToString() {
        FinanceData data = new FinanceData(1000, "ASD");
        assertEquals("+$1000 ASD", data.toString(true));
        assertEquals("-$1000 ASD", data.toString(false));
    }
}
