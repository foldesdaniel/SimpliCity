package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
public class Service extends Workplace implements Serializable {

    public Service(Point position) {
        super(FieldType.ZONE_SERVICE, position, 9000, 20, 300);
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.ZONE_WORK_SERVICE_IMG;
    }

}
