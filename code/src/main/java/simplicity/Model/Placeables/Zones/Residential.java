package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor(force = true)
public class Residential extends Zone implements Serializable {

    private final int taxPerInhabitant;

    public Residential(Point position) {
        super(FieldType.ZONE_RESIDENTIAL, position, 5000, 4);
        this.taxPerInhabitant = 100;
    }

    /**
     * used to calculate the collective mood of the residential zone
     *
     * @return the avg. mood of people living in here
     */
    public int calculateZoneMood() {
        int mood = 0;
        for (Person p : this.getPeople()) {
            mood += p.getMood();
        }
        return this.getPeople().size() == 0 ? 0 : mood / this.getPeople().size();
    }

    /**
     * used to calculate the tax of this residential zone
     *
     * @return tax per person * the number of inhabitants
     */
    @Override
    public int calculateTax() {
        return this.getPeople().size() * this.taxPerInhabitant;
    }

    @Override
    public Image getImage(Placeable left, Placeable right, Placeable up, Placeable down) {
        return this.getPeople().size() > 2 ? GameModel.ZONE_RESIDENTIAL_2_IMG : GameModel.ZONE_RESIDENTIAL_IMG;
    }

    /**
     * @return 0, since residential zone have no maintenance fee
     */
    @Override
    public int calculateMaintenance() {
        return 0;
    }

}
