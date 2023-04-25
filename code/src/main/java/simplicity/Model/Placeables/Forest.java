package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;

import java.awt.*;

@Getter
public class Forest extends Placeable {
    private final Date plantTime;

    public Forest(Point position, Date plantTime) {
        super(FieldType.FOREST, position, 1000);
        this.plantTime = plantTime;
    }

    public Forest(Point position) {
        this(position, new Date(0,0,0)); // need default plant time
    }

    @Override
    public int calculateTax() {
        return 0;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.FOREST_IMG;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }

}
