package simplicity.Model.GameTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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

    public void addToYear(int year) {
        this.year += year;
    }
}
