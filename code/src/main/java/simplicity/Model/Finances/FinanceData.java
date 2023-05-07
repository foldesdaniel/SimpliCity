package simplicity.Model.Finances;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class FinanceData implements Serializable {
    private int price;
    private String text;

    public FinanceData(int price, String text) {
        this.price = price;
        this.text = text;
    }

    /**
     * @param positive used to calculate if expense of income
     * @return formatted string of expense / income
     */
    public String toString(boolean positive) {
        return (positive ? "+$" : "-$") + this.price + " " + this.text;
    }
}
