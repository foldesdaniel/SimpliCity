package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.util.ArrayList;

@Getter
public abstract class Zone extends Placeable {

    private int maxPeople = 15;
    private ArrayList<Person> people = new ArrayList<>();

    public Zone(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice);
        this.maxPeople = maxPeople;
    }

    public boolean areSpacesLeft() {
        if(this.maxPeople == this.people.size()) return false;
        return true;
    }

    public int numOfSpacesLeft() {
        return this.maxPeople - this.people.size();
    }

    public void addPerson(Person person) {
        people.add(person);
    }
}
