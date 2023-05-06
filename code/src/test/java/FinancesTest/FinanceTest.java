package FinancesTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Finances.Finance;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FinanceTest {
    @Test
    public void testFinanceTestConstructor() {
        Finance finance = new Finance(1000);
        assertTrue(finance.getCurrentWealth() == 1000);
    }

    @Test
    public void testRemoveMoney() {
        Finance finance = new Finance(1000);
        finance.removeMoney(500);
        assertTrue(finance.getCurrentWealth() == 500);
    }

    @Test
    public void testAddMoney() {
        Finance finance = new Finance(1000);
        finance.addMoney(500);
        assertTrue(finance.getCurrentWealth() == 1500);
    }

    @Test
    public void testAddIncome() {
        Finance finance = new Finance(1000);
        finance.addIncome(2000, "ASD");
        assertTrue(finance.getIncomeList().size() == 1);
        assertTrue(finance.getIncomeList().get(0).getPrice() == 2000);
        assertTrue(finance.getIncomeList().get(0).getText().equals("ASD"));
    }

    @Test
    public void testAddBuilt() {
        Finance finance = new Finance(1000);
        finance.addBuilt(2000, "ASD");
        assertTrue(finance.getBuiltList().size() == 1);
        assertTrue(finance.getBuiltList().get(0).getPrice() == 2000);
        assertTrue(finance.getBuiltList().get(0).getText().equals("ASD"));
    }

    @Test
    public void testAddYearlySpend() {
        Finance finance = new Finance(1000);
        finance.addYearlySpend(2000, "ASD");
        assertTrue(finance.getYearlySpendList().size() == 1);
        assertTrue(finance.getYearlySpendList().get(0).getPrice() == 2000);
        assertTrue(finance.getYearlySpendList().get(0).getText().equals("ASD"));
    }

    @Test
    public void testRemoveYearlySpend() {
        Finance finance = new Finance(1000);
        finance.addYearlySpend(2000, "ASD");
        finance.removeYearlySpend(2000, "ASD");
        assertTrue(finance.getYearlySpendList().size() == 0);
    }

    @Test
    public void testBuiltToString() {
        Finance finance = new Finance(5000);
        finance.addBuilt(1000, "ASD1");
        finance.addBuilt(2000, "ASD2");
        assertTrue(finance.builtToString().equals("-$1000 ASD1\n-$2000 ASD2\n"));
    }

    @Test
    public void testYearlySpendToString() {
        Finance finance = new Finance(5000);
        finance.addYearlySpend(1000, "ASD1");
        finance.addYearlySpend(2000, "ASD2");
        assertTrue(finance.yearlySpendToString().equals("-$1000 ASD1\n-$2000 ASD2\n"));
    }

    @Test
    public void testIncomeToString() {
        Finance finance = new Finance(5000);
        finance.addIncome(1000, "ASD1");
        finance.addIncome(2000, "ASD2");
        assertTrue(finance.incomeToString().equals("+$1000 ASD1\n+$2000 ASD2\n"));
    }
}
