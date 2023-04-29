package simplicity.Model.Placeables;

import lombok.NoArgsConstructor;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Zones.Zone;

import java.awt.*;

@NoArgsConstructor
public abstract class Workplace extends Zone {

    private int taxPerPerson;

    public Workplace(FieldType type, Point position, int buildPrice, int maxPeople, int taxPerPerson) {
        super(type, position, buildPrice, maxPeople);
        this.taxPerPerson = taxPerPerson;
    }

    @Override
    public int calculateTax() {
        int sum = 0;
        for (Person p : this.getPeople()) {
            if (p.getEducationLevel() == EducationLevel.UNIVERSITY) sum += this.taxPerPerson * 2;
            else if (p.getEducationLevel() == EducationLevel.SECONDARY) sum += this.taxPerPerson * 1.5;
            else sum += this.taxPerPerson;
        }
        return sum;
    }

    @Override
    public int calculateMaintenance() {
        return 0;
    }
}
