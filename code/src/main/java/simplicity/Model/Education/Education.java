package simplicity.Model.Education;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.Placeables.Zone;

import java.awt.*;
import java.util.ArrayList;

@Getter
public abstract class Education extends Zone {

    private final ArrayList<Date> arrivalDates = new ArrayList<>();

    public Education(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice, maxPeople);
    }

    public abstract void graduate(ArrayList<Integer> studentIds);


}
