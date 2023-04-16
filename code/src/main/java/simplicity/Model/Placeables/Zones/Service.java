package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;

@Getter
public class Service extends Workplace {

    public Service(Point position) {
        super(FieldType.ZONE_SERVICE, position, 1000, 20, 100);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.ZONE_WORK_SERVICE_IMG;
    }

}
