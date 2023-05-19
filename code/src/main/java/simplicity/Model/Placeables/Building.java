package simplicity.Model.Placeables;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;

import java.awt.*;

@Getter
@NoArgsConstructor
public abstract class Building extends Placeable {
    private float moodBoost;
    private int radius;
    private int maintenanceCost;

    public Building(FieldType type, Point position, int buildPrice, float moodBoost, int radius, int maintenanceCost) {
        super(type, position, buildPrice);
        this.moodBoost = moodBoost;
        this.radius = radius;
        this.maintenanceCost = maintenanceCost;
    }

    @Override
    public int calculateTax() {
        return 0;
    }

    @Override
    public int calculateMaintenance() {
        return maintenanceCost;
    }
}
