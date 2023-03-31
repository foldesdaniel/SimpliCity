package simplicity.Model.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Workplace;

import java.awt.*;

@Getter
public class Industrial extends Workplace {

    public Industrial(Point position) {
        super(FieldType.ZONE_INDUSTRIAL, position, 1000, 20, 100);
    }
}
