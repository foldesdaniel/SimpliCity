package simplicity.Model.Placeables.Zones;

import lombok.Getter;
import lombok.NoArgsConstructor;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;

import java.awt.*;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public abstract class Zone extends Placeable {

    private final ArrayList<Person> people = new ArrayList<>();
    private final ArrayList<PeopleChangeListener> peopleChangeListeners = new ArrayList<>();
    private int maxPeople = 15;

    public Zone(FieldType type, Point position, int buildPrice, int maxPeople) {
        super(type, position, buildPrice);
        this.maxPeople = maxPeople;
    }

    /**
     * @return if there is any space left in the zone
     */
    public boolean areSpacesLeft() {
        return this.maxPeople != this.people.size();
    }

    /**
     * @return number of spaces left in the zone
     */
    public int numOfSpacesLeft() {
        return this.maxPeople - this.people.size();
    }

    /**
     * @param person adds this person to the zone
     */
    public void addPerson(Person person) {
        people.add(person);
        for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
    }

    /**
     * used to remove this person from the zone
     *
     * @param person person object to be removed
     */
    public void removePerson(Person person) {
        people.remove(person);
        for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
    }

    /**
     * used to remove this person from the zone
     *
     * @param index index of person to be removed
     */
    public void removePerson(int index) {
        people.remove(index);
        for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
    }

    public void addPeopleChangeListener(PeopleChangeListener l) {
        if (!peopleChangeListeners.contains(l)) peopleChangeListeners.add(l);
    }

    public void removePeopleChangeListener(PeopleChangeListener l) {
        peopleChangeListeners.remove(l);
    }

    /**
     * used to set the workplace and education of the people in the zone to null
     */
    public void deleteData() {
        for (Person p : people) {
            p.setWorkplace(null);
            p.setEducation(null);
        }
    }

}
