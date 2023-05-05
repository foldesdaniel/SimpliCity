package PersonTest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import simplicity.Model.Person.Person;

public class PersonTest {

    @Test
    void testPersonConstructor() {
        Person person = new Person();
        assertTrue(person.getMood() <= 75 && person.getMood() >= 65);
    }
}
