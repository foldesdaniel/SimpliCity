import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.PlaceableTemp;
import simplicity.Model.Placeables.Police;
import simplicity.Model.Placeables.Stadium;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;

import java.awt.*;
import java.nio.channels.Pipe;

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

    @Test
    public void testDoIndustrialDisaster() {
        gameModel.placeIndustrial(new Point(0,0));
        gameModel.doIndustrialDisaster(new Point(0,0));
        assertNull(gameModel.grid(0,0));
    }

    @Test
    public void testSearchRandomIndustrial() {
        gameModel.placeIndustrial(new Point(10,10));
        assertEquals(new Point(10,10), gameModel.searchRandomIndustrial());
    }

    @Test
    public void testIsIndustrialBuiltAlready() {
        assertFalse(gameModel.isIndustrialBuiltAlready());
        gameModel.placeIndustrial(new Point(10,10));
        assertTrue(gameModel.isIndustrialBuiltAlready());
    }

    @Test
    public void testNewYearMaintenanceCost() {
        gameModel.placeStadium(new Point(2,2));
        gameModel.placePolice(new Point(5,5));
        gameModel.placeRoad(new Point(8,8));

        gameModel.newYearMaintenanceCost();
        //26900 because of the cost of placing
        assertEquals(26900, gameModel.getCurrentWealth());
    }

    @Test
    public void testIsNextToARoad() {
        gameModel.placeResidential(new Point(1,1));
        assertFalse(gameModel.isNextToARoad(gameModel.grid(1,1).getPosition()));
        gameModel.placeRoad(new Point(1,2));
        assertTrue(gameModel.isNextToARoad(gameModel.grid(1,1).getPosition()));
    }

    @Test
    public void testFindHome() {
        assertNull(gameModel.findHome());
        gameModel.placeResidential(new Point(0,0));
        gameModel.placeRoad(new Point(0,1));
        assertEquals(gameModel.grid(0,0),gameModel.findHome());
    }

    @Test
    public void testIsMoodGoodEnough() {
        assertTrue(gameModel.isMoodGoodEnough());
    }

    @Test
    public void testGetCurrentWealth() {
        assertEquals(35000, gameModel.getCurrentWealth());
    }

    @Test
    public void testNewYearTaxCollection() {
        gameModel.placeIndustrial(new Point(1,1));
        gameModel.placeResidential(new Point(2,2));
        gameModel.placeService(new Point(4,4));
        new Person().goToWork( (Industrial) gameModel.grid(1,1));
        new Person().goToWork( (Service) gameModel.grid(4,4));
        new Person().moveIn( (Residential) gameModel.grid(2,2));
        gameModel.newYearTaxCollection();
        //cost of 3 buildings: 20000
        assertEquals(15600, gameModel.getCurrentWealth());
    }

    @Test
    public void testCanRoadBeDestroyed() {
        gameModel.placeResidential(new Point(0,0));
        gameModel.placeRoad(new Point(1,0));
        gameModel.placeResidential(new Point(2,0));
        assertFalse(gameModel.canRoadBeDestroyed(gameModel.grid(0,0), gameModel.grid(2,0), gameModel.grid(1,0)));

        gameModel.placeRoad(new Point(0,1));
        gameModel.placeRoad(new Point(1,1));
        gameModel.placeRoad(new Point(2,1));
        assertTrue(gameModel.canRoadBeDestroyed(gameModel.grid(0,0), gameModel.grid(2,0), gameModel.grid(1,0)));
    }

    @Test
    public void testWorkersRatio() {
        gameModel.placeIndustrial(new Point(0,0));
        gameModel.placeService(new Point(2,2));
        Person person = new Person();
        gameModel.getPeople().add(person);
        person.goToWork( (Industrial) gameModel.grid(0,0));
        assertEquals(1, gameModel.workersRatio());

        for(int i = 0; i < 2; i++) {
            Person p = new Person();
            gameModel.getPeople().add(p);
            p.goToWork( (Service) gameModel.grid(2,2));
        }
        assertEquals(0, gameModel.workersRatio());
    }

    @Test
    public void testBoostMood() {
        Person person = new Person();
        int personMood = person.getMood();
        gameModel.boostMood(person, 5);
        assertEquals(personMood + 5, person.getMood());

        gameModel.boostMood(person, -15);
        assertEquals(personMood -10, person.getMood());
    }
    
}
