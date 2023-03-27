package simplicity.Model.GameTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Date {
    private int year;
    private int day;
    private int hour;

    public void setDate(int year, int day, int hour) {
        this.year = year;
        this.day = day;
        this.hour = hour;
    }
}
