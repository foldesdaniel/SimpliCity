package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;

import java.awt.*;

public class Stadium extends Building {

    public Stadium(Point position) {
        super(FieldType.STADIUM, position, 1000, 1.2f, 5, 500);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.STADIUM_IMG;
    }

}
