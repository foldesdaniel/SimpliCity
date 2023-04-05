package simplicity.Model.Finances;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class Finance {
    private ArrayList<FinanceData> incomeList;
    private ArrayList<FinanceData> spendList;
    private int currentWealth;

    public Finance(int currentWealth) {
        this.currentWealth = currentWealth;
    }

    public void removeMoney(int money) {
        currentWealth -= money;
    }
}
