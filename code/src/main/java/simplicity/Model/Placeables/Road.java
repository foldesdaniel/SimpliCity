package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;

import java.awt.*;

public class Road extends Placeable {
    public Road(Point position) {
        super(FieldType.ROAD, position, 1000);
    }

    @Override
    public int calculateTax() {
        return 0;
    }
}
