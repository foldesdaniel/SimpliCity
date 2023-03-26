package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;

import java.awt.*;

public abstract class Workplace extends Zone {

    public Workplace(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice, maxPeople);
    }
}
