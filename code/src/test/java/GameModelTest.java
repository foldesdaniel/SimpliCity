import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.PlaceableTemp;
import simplicity.Model.Placeables.Police;
import simplicity.Model.Placeables.Stadium;
import simplicity.Model.Placeables.Zones.Residential;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameModelTest {

    GameModel gameModel;

    @BeforeEach
    public void generateModel() {
        gameModel = new GameModel();
    }

    @Test
    public void testGameModelConstructor() {
        assertEquals(35000, gameModel.getCurrentWealth());
    }

    @Test
    public void testPlaceStadium() {
        gameModel.placeStadium(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Stadium);
        assertTrue(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 1) instanceof PlaceableTemp);
        assertEquals(32000, gameModel.getCurrentWealth());
        assertEquals("-$3000 Stadium építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Stadium fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeStadium(new Point(2, 2));
        assertEquals(32000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveStadium() {
        gameModel.placeStadium(new Point(2, 2));
        gameModel.removeStadium(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Stadium);
        assertEquals("", gameModel.getFinance().yearlySpendToString());
    }

    @Test
    public void testPlacePolice() {
        gameModel.placePolice(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Police);
        assertEquals(33000, gameModel.getCurrentWealth());
        assertEquals("-$2000 Rendőrség építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Rendőrség fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placePolice(new Point(2, 2));
        assertEquals(33000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemovePolice() {
        gameModel.placePolice(new Point(2, 2));
        gameModel.removePolice(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Police);
        assertEquals("", gameModel.getFinance().yearlySpendToString());
    }

    @Test
    public void testRemoveDepressedPeople() {
        Residential residential = new Residential(new Point(0,0));
        for (int i = 0; i < 3; i++) {
            Person person = new Person();
            person.setMood(0);
            person.moveIn(residential);
            gameModel.getPeople().add(person);
        }
        Person person = new Person();
        person.setMood(1);
        person.moveIn(residential);
        gameModel.getPeople().add(person);

        gameModel.removeDepressedPeople();
        assertEquals(1, gameModel.getPeople().size());
    }

}
