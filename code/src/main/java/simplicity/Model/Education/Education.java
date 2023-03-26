package simplicity.Model.Education;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;
import java.util.ArrayList;

@Getter
public abstract class Education extends Workplace {

    private final ArrayList<int[]> arrivalDates = new ArrayList<>();

    public Education(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice, maxPeople);
    }

    public abstract void graduate(ArrayList<Integer> studentIds);


}
