package simplicity.Model.Placeables;

import lombok.Getter;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Person.Person;

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

    public void addPerson(Person person) {
        people.add(person);
    }
}
