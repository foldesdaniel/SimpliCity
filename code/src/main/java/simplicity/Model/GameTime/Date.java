package simplicity.Model.GameTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * custom Date implementation for the project
 * holds Year, Day, Hour
 * specifically made to avoid different month lengths
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class Date implements Serializable {
    private int year;
    private int day;
    private int hour;

    public void setDate(int year, int day, int hour) {
        this.year = year;
        this.day = day;
        this.hour = hour;
    }

    /**
     * increase year by a specific amount
     *
     * @param year amount of years to increase by
     */
    public void addToYear(int year) {
        this.year += year;
    }
}
