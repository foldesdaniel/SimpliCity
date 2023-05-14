package FinancesTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Finances.Finance;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinanceTest {
    @Test
    public void testFinanceTestConstructor() {
        Finance finance = new Finance(1000);
        assertEquals(1000, finance.getCurrentWealth());
    }

    @Test
    public void testRemoveMoney() {
        Finance finance = new Finance(1000);
        finance.removeMoney(500);
        assertEquals(500, finance.getCurrentWealth());
    }

    @Test
    public void testAddMoney() {
        Finance finance = new Finance(1000);
        finance.addMoney(500);
        assertEquals(1500, finance.getCurrentWealth());
    }

    @Test
    public void testAddIncome() {
        Finance finance = new Finance(1000);
        finance.addIncome(2000, "ASD");
        assertEquals(1, finance.getIncomeList().size());
        assertEquals(2000, finance.getIncomeList().get(0).getPrice());
        assertEquals("ASD", finance.getIncomeList().get(0).getText());
    }

    @Test
    public void testAddBuilt() {
        Finance finance = new Finance(1000);
        finance.addBuilt(2000, "ASD");
        assertEquals(1, finance.getBuiltList().size());
        assertEquals(2000, finance.getBuiltList().get(0).getPrice());
        assertEquals("ASD", finance.getBuiltList().get(0).getText());
    }

    @Test
    public void testAddYearlySpend() {
        Finance finance = new Finance(1000);
        finance.addYearlySpend(2000, "ASD");
        assertEquals(1, finance.getYearlySpendList().size());
        assertEquals(2000, finance.getYearlySpendList().get(0).getPrice());
        assertEquals("ASD", finance.getYearlySpendList().get(0).getText());
    }

    @Test
    public void testRemoveYearlySpend() {
        Finance finance = new Finance(1000);
        finance.addYearlySpend(2000, "ASD");
        finance.removeYearlySpend(2000, "ASD");
        assertEquals(0, finance.getYearlySpendList().size());
    }

    @Test
    public void testBuiltToString() {
        Finance finance = new Finance(5000);
        finance.addBuilt(1000, "ASD1");
        finance.addBuilt(2000, "ASD2");
        assertEquals("-$1000 ASD1\n-$2000 ASD2\n", finance.builtToString());
    }

    @Test
    public void testYearlySpendToString() {
        Finance finance = new Finance(5000);
        finance.addYearlySpend(1000, "ASD1");
        finance.addYearlySpend(2000, "ASD2");
        assertEquals("-$1000 ASD1\n-$2000 ASD2\n", finance.yearlySpendToString());
    }

    @Test
    public void testIncomeToString() {
        Finance finance = new Finance(5000);
        finance.addIncome(1000, "ASD1");
        finance.addIncome(2000, "ASD2");
        assertEquals("+$1000 ASD1\n+$2000 ASD2\n", finance.incomeToString());
    }
}
