package simplicity.Model.Zones;

import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;

public class Service extends Workplace {

    public Service(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice, maxPeople);
    }
}
