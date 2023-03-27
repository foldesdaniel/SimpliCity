package simplicity.Model.Finances;

import lombok.Getter;

@Getter
public class FinanceData {
    private int price;
    private String text;

    public FinanceData(int price, String text) {
        this.price = price;
        this.text = text;
    }
}
