package simplicity.Model.Placeables;

import simplicity.Model.Game.FieldType;
import simplicity.Model.Placeables.Zones.Zone;

import java.awt.*;

public abstract class Workplace extends Zone {

    private int taxPerPerson;

    public Workplace(FieldType type, Point position, int buildPrice, int maxPeople, int taxPerPerson) {
        super(type, position, buildPrice, maxPeople);
        this.taxPerPerson = taxPerPerson;
    }

    @Override
    public int calculateTax() {
        return this.getPeople().size() * this.taxPerPerson;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }
}
