package PersonTest;

import org.junit.jupiter.api.Test;
import simplicity.Model.Person.Person;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PersonTest {

    @Test
    public void testPersonConstructor() {
        Person person = new Person();
        assertTrue(person.getMood() <= 75 && person.getMood() >= 65);
    }
}
