package PersonTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Education.School;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {

    @Test
    public void testPersonConstructor() {
        Person person = new Person();
        assertTrue(person.getMood() <= 75 && person.getMood() >= 65);

        Residential res = new Residential(new Point(0, 0));
        Person person2 = new Person(res);
        assertEquals(person2.getHome(), res);

        Person person3 = new Person(res, 50);
        assertTrue(person3.getMood() >= 45 && person3.getMood() <= 55);
        assertEquals(person3.getHome(), res);
    }

    @Test
    public void testGoToSchool() {
        Person person = new Person();
        School school = new School(new Point(0, 0));
        person.goToSchool(school);
        assertSame(person.getEducation(), school);
        assertTrue(school.getPeople().contains(person));
    }

    @Test
    public void testGoToWork() {
        Person person = new Person();
        Industrial workplace = new Industrial(new Point(0, 0));
        person.goToWork(workplace);
        assertSame(person.getWorkplace(), workplace);
        assertTrue(workplace.getPeople().contains(person));
    }

    @Test
    public void testMoveIn() {
        Person person = new Person();
        Residential res = new Residential(new Point(0, 0));
        person.moveIn(res);
        assertSame(person.getHome(), res);
    }
}
