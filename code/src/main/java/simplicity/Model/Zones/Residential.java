package simplicity.Model.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Zone;

import java.awt.*;

@Getter
public class Residential extends Zone {

    private final int taxPerInhabitant;

    public Residential(Point position) {
        super(FieldType.ZONE_RESIDENTIAL, position, 1000, 20);
        this.taxPerInhabitant = 100;
    }

    @Override
    public int calculateTax() {
        return this.getPeople().size() * this.taxPerInhabitant;
    }
}
