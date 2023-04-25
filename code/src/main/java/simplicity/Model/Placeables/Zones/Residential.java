package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;

@Getter
public class Residential extends Zone {

    private final int taxPerInhabitant;

    public Residential(Point position) {
        super(FieldType.ZONE_RESIDENTIAL, position, 5000, 4);
        this.taxPerInhabitant = 100;
    }

    public int calculateZoneMood() {
        int mood = 0;
        for (Person p : this.getPeople()) {
            mood += p.getMood();
        }
        return this.getPeople().size() == 0 ? 0 : (int) mood / this.getPeople().size();
    }

    @Override
    public int calculateTax() {
        return this.getPeople().size() * this.taxPerInhabitant;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return GameModel.ZONE_RESIDENTIAL_IMG;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }

}
