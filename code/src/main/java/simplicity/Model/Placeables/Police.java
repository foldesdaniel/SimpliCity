package simplicity.Model.Placeables;

import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;

import java.awt.*;
import java.io.Serializable;

@NoArgsConstructor
public class Police extends Building implements Serializable {

    public Police(Point position) {
        super(FieldType.POLICE, position, 2000, 1.2f, 4, 500);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.POLICE_IMG;
    }

    @Override
    public boolean roadConnects() {
        return false;
    }

}
