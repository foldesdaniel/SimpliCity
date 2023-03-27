package simplicity.Model.Zones;

import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Zone;

import java.awt.*;

public class Residential extends Zone {

    public Residential(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice, maxPeople);
    }
}
