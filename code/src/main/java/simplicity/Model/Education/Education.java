package simplicity.Model.Education;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.Placeables.Zones.Zone;

import java.awt.*;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public abstract class Education extends Zone {

    private final ArrayList<Date> arrivalDates = new ArrayList<>();

    private int maintenanceCost;

    public Education(FieldType type, Point position, int buildPrice, int maxPeople, int maintenanceCost) {
        super(type, position, buildPrice, maxPeople);
        this.maintenanceCost = maintenanceCost;
    }

    public abstract void graduate(ArrayList<Integer> studentIds);

    @Override
    public int calculateTax() {
        return 0;
    }

    @Override
    public int calculateMaintenance() {
        return maintenanceCost;
    }
}
