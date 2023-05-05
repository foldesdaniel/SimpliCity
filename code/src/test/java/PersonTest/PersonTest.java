package PersonTest;

import org.junit.Test;
import simplicity.Model.Person.Person;

import static org.junit.Assert.assertTrue;

public class PersonTest {

    public PersonTest() {
    }

    @Test
    public void testPersonConstructor() {
        Person person = new Person();
        assertTrue(person.getMood() <= 75 && person.getMood() >= 65);
    }
}
