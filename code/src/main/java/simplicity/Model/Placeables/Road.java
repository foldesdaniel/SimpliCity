package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.Game.FieldType;

import java.awt.*;

public class Road extends Placeable {

    @Getter
    private int maintenanceCost;
    public Road(Point position) {
        super(FieldType.ROAD, position, 1000);
        this.maintenanceCost = 500;
    }

    @Override
    public int calculateTax() {
        return 0;
    }
}
