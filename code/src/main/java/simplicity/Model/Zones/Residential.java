package simplicity.Model.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Zone;

import java.awt.*;

@Getter
public class Residential extends Zone {

    private final int taxPerInhabitant;

    public Residential(Point position) {
        super(FieldType.ZONE_RESIDENTIAL, position, 1000, 20);
        this.taxPerInhabitant = 100;
    }

    public int calculateZoneMood() {
        int mood = 0;
        for (Person p: this.getPeople()) {
            mood += p.getMood();
        }
        return (int) mood / this.getPeople().size();
    }

    @Override
    public int calculateTax() {
        return this.getPeople().size() * this.taxPerInhabitant;
    }
}
