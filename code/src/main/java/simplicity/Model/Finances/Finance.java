package simplicity.Model.Finances;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;

@Getter
@NoArgsConstructor(force = true)
public class Finance implements Serializable {
    private final ArrayList<FinanceData> incomeList; //stores incomes with their information
    private final ArrayList<FinanceData> builtList; //stores built costs with their information

    private final ArrayList<FinanceData> yearlySpendList; //stores maintenance costs with their information
    private int currentWealth; //player's current money
    @Setter
    private double profitableYearsInARow = 0;

    public Finance(int currentWealth) {
        this.currentWealth = currentWealth;
        this.incomeList = new ArrayList<>();
        this.builtList = new ArrayList<>();
        this.yearlySpendList = new ArrayList<>();
    }

    /**
     * Remove money from the player's current wealth
     *
     * @param money The amount to remove
     */
    public void removeMoney(int money) {
        currentWealth -= money;
    }

    /**
     * Add money to the player's current wealth
     *
     * @param money The amount to add
     */
    public void addMoney(int money) {
        currentWealth += money;
    }

    /**
     * Add income to the list
     *
     * @param price The profit
     * @param text  Information about the building
     */
    public void addIncome(int price, String text) {
        if (this.incomeList != null) {
            this.incomeList.add(new FinanceData(price, text));
        }
    }

    /**
     * Add built price to the list with text
     *
     * @param price The expense
     * @param text  Information about the building
     */
    public void addBuilt(int price, String text) {
        if (this.builtList != null) {
            this.builtList.add(new FinanceData(price, text));
        }
    }

    /**
     * Add yearly maintenance price with text
     *
     * @param price The yearly expense
     * @param text  Information about the building
     */
    public void addYearlySpend(int price, String text) {
        if (this.yearlySpendList != null) {
            this.yearlySpendList.add(new FinanceData(price, text));
        }
    }

    /**
     * Remove yearly maintenance price with text
     *
     * @param price The yearly expense
     * @param text  Information about the building
     */
    public void removeYearlySpend(int price, String text) {
        int ind = -1;
        if (this.yearlySpendList != null) {
            for (int i = 0; i < this.yearlySpendList.size(); ++i) {
                FinanceData data = this.yearlySpendList.get(i);
                if (data.getPrice() == price && data.getText().equals(text)) {
                    ind = i;
                    break;
                }
            }
        }
        if (ind != -1) this.yearlySpendList.remove(ind);
    }

    /**
     * Making displayable information about the built expenses
     *
     * @return information about the built expenses
     */
    public String builtToString() {
        StringBuilder s = new StringBuilder();
        if (this.builtList != null) {
            for (FinanceData data : this.builtList) {
                s.append(data.toString(false)).append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Making displayable information about the yearly maintenance expenses
     *
     * @return information about the yearly maintenance expenses
     */
    public String yearlySpendToString() {
        StringBuilder s = new StringBuilder();
        if (this.yearlySpendList != null) {
            for (FinanceData data : this.yearlySpendList) {
                s.append(data.toString(false)).append("\n");
            }
        }
        return s.toString();
    }

    /**
     * Making displayable information about the income
     *
     * @return information about the income
     */
    public String incomeToString() {
        StringBuilder s = new StringBuilder();
        if (this.incomeList != null) {
            for (FinanceData data : this.incomeList) {
                s.append(data.toString(true)).append("\n");
            }
        }
        return s.toString();
    }
}
