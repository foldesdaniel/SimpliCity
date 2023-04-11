package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;

import java.awt.*;

@Getter
public class Forest extends Placeable {
    private Date plantTime;
    public Forest(Point position, Date plantTime) {
        super(FieldType.FOREST, position, 1000);
        this.plantTime = plantTime;
    }

    @Override
    public int calculateTax() {
        return 0;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.FOREST_IMG;
    }

}
