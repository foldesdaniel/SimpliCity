package Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import simplicity.Model.Persistence.Persistence;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PersistenceTest {

    @AfterAll
    public static void removeFiles() {
        Persistence.delete("__testSave.txt");
    }

    @Test
    public void testSaveAndLoad() throws IOException, ClassNotFoundException {

        TestObject testObject = new TestObject();
        testObject.name = "test";
        testObject.num = 7;
        testObject.isTrue = false;
        Persistence.save(testObject, "__testSave.txt");

        TestObject loadedTestObject = (TestObject) Persistence.load("__testSave.txt");
        assertEquals("test", loadedTestObject.name);
        assertEquals(7, loadedTestObject.num);
        assertFalse(loadedTestObject.isTrue);
    }

}
