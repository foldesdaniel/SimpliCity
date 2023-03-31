package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;

import java.awt.*;

public class Police extends Building {
    public Police(Point position) {
        super(FieldType.POLICE, position, 1000, 1.2f, 4, 500);
    }
}
