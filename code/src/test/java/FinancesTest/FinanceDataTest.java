package FinancesTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Finances.FinanceData;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinanceDataTest {
    @Test
    public void testToString() {
        FinanceData data = new FinanceData(1000, "ASD");
        assertTrue(data.toString(true).equals("+$1000 ASD"));
        assertTrue(data.toString(false).equals("-$1000 ASD"));
    }
}
