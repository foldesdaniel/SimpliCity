package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;

import java.awt.*;

public abstract class Placeable {

    private FieldType type;
    private Point position;
    private int buildPrice;

    public Placeable(FieldType type, Point position, int buildPrice) {
        this.type = type;
        this.position = position;
        this.buildPrice = buildPrice;
    }
}
