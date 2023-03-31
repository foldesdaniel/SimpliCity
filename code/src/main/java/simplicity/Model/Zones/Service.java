package simplicity.Model.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;

@Getter
public class Service extends Workplace {

    public Service(Point position) {
        super(FieldType.ZONE_SERVICE, position, 1000, 20, 100);
    }
}
