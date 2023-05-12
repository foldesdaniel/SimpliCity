import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.GameModel;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.*;
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
        assertFalse(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertFalse(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertFalse(gameModel.grid(3, 1) instanceof PlaceableTemp);
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
    public void testPlaceIndustrial() {
        gameModel.placeIndustrial(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Industrial);
        assertEquals(29000, gameModel.getCurrentWealth());
        assertEquals("-$6000 Ipari zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeIndustrial(new Point(2, 2));
        assertEquals(29000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveIndustrial() {
        gameModel.placeIndustrial(new Point(2, 2));
        gameModel.removeIndustrial(new Point(2, 2), false);
        assertFalse(gameModel.grid(2, 2) instanceof Industrial);

        gameModel.placeIndustrial(new Point(2, 2));
        ((Industrial)gameModel.grid(2,2)).addPerson(new Person());
        gameModel.removeIndustrial(new Point(2, 2), false);
        assertTrue(gameModel.grid(2, 2) instanceof Industrial);
        gameModel.removeIndustrial(new Point(2, 2), true);
        assertFalse(gameModel.grid(2, 2) instanceof Industrial);
    }

    @Test
    public void testPlaceRoad() {
        gameModel.placeRoad(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Road);
        assertEquals(34400, gameModel.getCurrentWealth());
        assertEquals("-$600 Út építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$100 Út fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeRoad(new Point(2, 2));
        assertEquals(34400, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveRoad() {
        gameModel.placeRoad(new Point(2, 2));
        gameModel.removeRoad(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Road);
        assertEquals("", gameModel.getFinance().yearlySpendToString());

        //complicated test
        Person p = new Person();
        gameModel.placeRoad(new Point(2, 2));
        gameModel.placeResidential(new Point(1, 2));
        gameModel.placeService(new Point(2, 1));
        ((Residential)gameModel.grid(1,2)).addPerson(p);
        ((Residential)gameModel.grid(1,2)).getPeople().get(0).goToWork((Service)gameModel.grid(2, 1));
        gameModel.removeRoad(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Road);
    }

    @Test
    public void testPlaceForest() {
        gameModel.placeForest(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Forest);
        assertEquals(30000, gameModel.getCurrentWealth());
        assertEquals("-$5000 Erdő építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Erdő fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeForest(new Point(2, 2));
        assertEquals(30000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveForest() {
        gameModel.placeForest(new Point(2, 2));
        gameModel.removeForest(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Forest);
        assertEquals("", gameModel.getFinance().yearlySpendToString());
    }

    @Test
    public void testPlaceUniversity() {
        gameModel.placeUniversity(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof University);
        assertTrue(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 1) instanceof PlaceableTemp);
        assertEquals(26000, gameModel.getCurrentWealth());
        assertEquals("-$9000 Egyetem építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$1900 Egyetem fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeForest(new Point(2, 2));
        assertEquals(26000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveUniversity() {
        gameModel.placeUniversity(new Point(2, 2));
        gameModel.removeUniversity(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof University);
        assertFalse(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertFalse(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertFalse(gameModel.grid(3, 1) instanceof PlaceableTemp);
        assertEquals("", gameModel.getFinance().yearlySpendToString());
    }

    @Test
    public void testPlaceSchool() {
        gameModel.placeSchool(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof School);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertEquals(28000, gameModel.getCurrentWealth());
        assertEquals("-$7000 Iskola építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$1500 Iskola fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeSchool(new Point(2, 2));
        assertEquals(28000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveSchool() {
        gameModel.placeSchool(new Point(2, 2));
        gameModel.removeSchool(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof School);
        assertFalse(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertEquals("", gameModel.getFinance().yearlySpendToString());
    }

    @Test
    public void testPlaceService() {
        gameModel.placeService(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Service);
        assertEquals(26000, gameModel.getCurrentWealth());
        assertEquals("-$9000 Szolgáltatási zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeService(new Point(2, 2));
        assertEquals(26000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveService() {
        gameModel.placeService(new Point(2, 2));
        gameModel.removeService(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Service);
    }

    @Test
    public void testPlaceResidential() {
        gameModel.placeResidential(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Residential);
        assertEquals(30000, gameModel.getCurrentWealth());
        assertEquals("-$5000 Lakóhely zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeResidential(new Point(2, 2));
        assertEquals(30000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveResidential() {
        gameModel.placeResidential(new Point(2, 2));
        gameModel.removeResidential(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Residential);

        //complicated test
        gameModel.placeResidential(new Point(2, 2));
        ((Residential)gameModel.grid(2, 2)).addPerson(new Person());
        gameModel.removeResidential(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Residential);
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
