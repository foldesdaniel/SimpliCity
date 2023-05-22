import org.junit.jupiter.api.BeforeAll;
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

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class GameModelTest {

    GameModel gameModel;

    @BeforeAll
    public static void mockJOptionPane() {
        mockStatic(JOptionPane.class);
        when(JOptionPane.showOptionDialog(any(), any(), any(), anyInt(), anyInt(), any(), any(), any()))
                .thenReturn(JOptionPane.NO_OPTION);
    }

    @BeforeEach
    public void generateModel() {
        gameModel = new GameModel();
        gameModel.initGrid();
    }

    @Test
    public void testGameModelConstructor() {
        assertEquals(100000, gameModel.getCurrentWealth());
    }

    @Test
    public void testPlaceStadium() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeStadium(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Stadium);
        assertTrue(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 1) instanceof PlaceableTemp);
        assertEquals(currentWealth - 3000, gameModel.getCurrentWealth());
        assertEquals("-$3000 Stadium építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Stadium fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeStadium(new Point(2, 2));
        assertEquals(currentWealth - 3000, gameModel.getCurrentWealth());
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
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placePolice(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Police);
        assertEquals(currentWealth - 2000, gameModel.getCurrentWealth());
        assertEquals("-$2000 Rendőrség építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Rendőrség fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placePolice(new Point(2, 2));
        assertEquals(currentWealth - 2000, gameModel.getCurrentWealth());
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
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeIndustrial(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Industrial);
        assertEquals(currentWealth - 6000, gameModel.getCurrentWealth());
        assertEquals("-$6000 Ipari zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeIndustrial(new Point(2, 2));
        assertEquals(currentWealth - 6000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveIndustrial() {
        gameModel.placeIndustrial(new Point(2, 2));
        gameModel.removeIndustrial(new Point(2, 2), false);
        assertFalse(gameModel.grid(2, 2) instanceof Industrial);

        gameModel.placeIndustrial(new Point(2, 2));
        ((Industrial) gameModel.grid(2, 2)).addPerson(new Person());
        gameModel.removeIndustrial(new Point(2, 2), false);
        assertTrue(gameModel.grid(2, 2) instanceof Industrial);
        gameModel.removeIndustrial(new Point(2, 2), true);
        assertFalse(gameModel.grid(2, 2) instanceof Industrial);
    }

    @Test
    public void testPlaceRoad() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeRoad(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Road);
        assertEquals(currentWealth - 600, gameModel.getCurrentWealth());
        assertEquals("-$600 Út építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$100 Út fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeRoad(new Point(2, 2));
        assertEquals(currentWealth - 600, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveRoad() {
        gameModel.placeRoad(new Point(2, 2));
        gameModel.removeRoad(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Road);
        assertEquals("", gameModel.getFinance().yearlySpendToString());

//        complicated test
        Person p = new Person();
        gameModel.placeRoad(new Point(2, 2));
        gameModel.placeResidential(new Point(1, 2));
        gameModel.placeService(new Point(2, 1));
        ((Residential) gameModel.grid(1, 2)).addPerson(p);
        ((Residential) gameModel.grid(1, 2)).getPeople().get(0).goToWork((Service) gameModel.grid(2, 1));
        gameModel.removeRoad(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Road);
    }

    @Test
    public void testPlaceForest() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeForest(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Forest);
        assertEquals(currentWealth - 5000, gameModel.getCurrentWealth());
        assertEquals("-$5000 Erdő építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$500 Erdő fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeForest(new Point(2, 2));
        assertEquals(currentWealth - 5000, gameModel.getCurrentWealth());
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
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeUniversity(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof University);
        assertTrue(gameModel.grid(2, 1) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertTrue(gameModel.grid(3, 1) instanceof PlaceableTemp);
        assertEquals(currentWealth - 9000, gameModel.getCurrentWealth());
        assertEquals("-$9000 Egyetem építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$1900 Egyetem fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeForest(new Point(2, 2));
        assertEquals(currentWealth - 9000, gameModel.getCurrentWealth());
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
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeSchool(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof School);
        assertTrue(gameModel.grid(3, 2) instanceof PlaceableTemp);
        assertEquals(currentWealth - 7000, gameModel.getCurrentWealth());
        assertEquals("-$7000 Iskola építés\n", gameModel.getFinance().builtToString());
        assertEquals("-$1500 Iskola fenntartási díj\n", gameModel.getFinance().yearlySpendToString());

        gameModel.placeSchool(new Point(2, 2));
        assertEquals(currentWealth - 7000, gameModel.getCurrentWealth());
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
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeService(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Service);
        assertEquals(currentWealth - 9000, gameModel.getCurrentWealth());
        assertEquals("-$9000 Szolgáltatási zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeService(new Point(2, 2));
        assertEquals(currentWealth - 9000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveService() {
        gameModel.placeService(new Point(2, 2));
        gameModel.removeService(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Service);
    }

    @Test
    public void testPlaceResidential() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeResidential(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Residential);
        assertEquals(currentWealth - 5000, gameModel.getCurrentWealth());
        assertEquals("-$5000 Lakóhely zóna kijelölés\n", gameModel.getFinance().builtToString());

        gameModel.placeResidential(new Point(2, 2));
        assertEquals(currentWealth - 5000, gameModel.getCurrentWealth());
    }

    @Test
    public void testRemoveResidential() {
        gameModel.placeResidential(new Point(2, 2));
        gameModel.removeResidential(new Point(2, 2));
        assertFalse(gameModel.grid(2, 2) instanceof Residential);

//        complicated test
        gameModel.placeResidential(new Point(2, 2));
        ((Residential) gameModel.grid(2, 2)).addPerson(new Person());
        gameModel.removeResidential(new Point(2, 2));
        assertTrue(gameModel.grid(2, 2) instanceof Residential);
    }

    @Test
    public void testCountStadium() {
        gameModel.placeStadium(new Point(2, 2));
        gameModel.placeStadium(new Point(4, 4));
        gameModel.placeStadium(new Point(7, 7));
        assertEquals(3, gameModel.countStadium(new Point(5, 5)));
    }

    @Test
    public void testCountPolice() {
        gameModel.placePolice(new Point(3, 3));
        gameModel.placePolice(new Point(4, 4));
        gameModel.placePolice(new Point(5, 5));
        assertEquals(3, gameModel.countPolice(new Point(4, 4)));
    }

    @Test
    public void testCountIndustrial() {
        gameModel.placeIndustrial(new Point(3, 3));
        gameModel.placeIndustrial(new Point(4, 4));
        gameModel.placeIndustrial(new Point(5, 5));
        assertEquals(3, gameModel.countIndustrial(new Point(4, 4)));
    }

    @Test
    public void testIsForestBetweenResidential_Industrial() {
        gameModel.placeResidential(new Point(1, 1));
        gameModel.placeIndustrial(new Point(1, 5));
        gameModel.placeForest(new Point(1, 3));
        assertTrue(gameModel.isForestBetweenResidential_Industrial(new Point(1, 1), new Point(1, 5)));
    }

    @Test
    public void testIsIndustrialAfterForest() {
        gameModel.placeResidential(new Point(1, 1));
        gameModel.placeIndustrial(new Point(1, 5));
        gameModel.placeForest(new Point(1, 3));
        assertTrue(gameModel.isIndustrialAfterForest(new Point(1, 3), new Point(1, 1)));
    }

    @Test
    public void testDoesForestBoostMood() {
        gameModel.placeResidential(new Point(1, 1));
        gameModel.placeForest(new Point(1, 3));
        assertTrue(gameModel.doesForestBoostMood(new Point(1, 3), new Point(1, 1)));
    }

    @Test
    public void testGetWorkplaceDistance() {
        gameModel.placeResidential(new Point(1, 1));
        gameModel.placeService(new Point(1, 6));
        for (int i = 2; i <= 5; ++i)
            gameModel.placeRoad(new Point(1, i));
        Person p = new Person();
        p.moveIn((Residential) gameModel.grid(1, 1));
        p.goToWork((Service) gameModel.grid(1, 6));
        assertEquals(5, gameModel.getWorkplaceDistance(p, "workplace"));
    }

    @Test
    public void testBoostPersonMoodBasedOnDistance() {
        gameModel.placeResidential(new Point(1, 1));
        gameModel.placeService(new Point(1, 6));
        for (int i = 2; i <= 5; ++i)
            gameModel.placeRoad(new Point(1, i));
        Person p = new Person();
        p.moveIn((Residential) gameModel.grid(1, 1));
        p.goToWork((Service) gameModel.grid(1, 6));
        int prevmood = p.getMood();
        gameModel.boostPersonMoodBasedOnDistance(p, "workplace");
        assertTrue(p.getMood() > prevmood);
    }

    @Test
    public void testCalculateForestMood() {
        gameModel.placeForest(new Point(1, 3));
        assertEquals(0, gameModel.calculateForestMood(new Point(1, 3)));
    }

    @Test
    public void testRemoveDepressedPeople() {
        Residential residential = new Residential(new Point(0, 0));
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
        gameModel.placeIndustrial(new Point(0, 0));
        gameModel.doIndustrialDisaster(new Point(0, 0));
        assertNull(gameModel.grid(0, 0));
    }

    @Test
    public void testSearchRandomIndustrial() {
        gameModel.placeIndustrial(new Point(10, 10));
        assertEquals(new Point(10, 10), gameModel.searchRandomIndustrial());
    }

    @Test
    public void testIsIndustrialBuiltAlready() {
        assertFalse(gameModel.isIndustrialBuiltAlready());
        gameModel.placeIndustrial(new Point(10, 10));
        assertTrue(gameModel.isIndustrialBuiltAlready());
    }

    @Test
    public void testNewYearMaintenanceCost() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeStadium(new Point(2, 2));
        gameModel.placePolice(new Point(5, 5));
        gameModel.placeRoad(new Point(8, 8));

        gameModel.newYearMaintenanceCost();
        assertEquals(currentWealth - 8100, gameModel.getCurrentWealth());
    }

    @Test
    public void testIsNextToARoad() {
        gameModel.placeResidential(new Point(1, 1));
        assertFalse(gameModel.isNextToARoad(gameModel.grid(1, 1).getPosition()));
        gameModel.placeRoad(new Point(1, 2));
        assertTrue(gameModel.isNextToARoad(gameModel.grid(1, 1).getPosition()));
    }

    @Test
    public void testFindHome() {
        assertNull(gameModel.findHome());
        gameModel.placeResidential(new Point(0, 0));
        gameModel.placeRoad(new Point(0, 1));
        assertEquals(gameModel.grid(0, 0), gameModel.findHome());
    }

    @Test
    public void testIsMoodGoodEnough() {
        assertTrue(gameModel.isMoodGoodEnough());
    }

    @Test
    public void testGetCurrentWealth() {
        assertEquals(100000, gameModel.getCurrentWealth());
    }

    @Test
    public void testNewYearTaxCollection() {
        int currentWealth = gameModel.getCurrentWealth();
        gameModel.placeIndustrial(new Point(1, 1));
        gameModel.placeResidential(new Point(2, 2));
        gameModel.placeService(new Point(4, 4));
        new Person().goToWork((Industrial) gameModel.grid(1, 1));
        new Person().goToWork((Service) gameModel.grid(4, 4));
        new Person().moveIn((Residential) gameModel.grid(2, 2));
        gameModel.newYearTaxCollection();


        assertEquals(currentWealth - 19400, gameModel.getCurrentWealth());
    }

    @Test
    public void testCanRoadBeDestroyed() {
        gameModel.placeResidential(new Point(0, 0));
        gameModel.placeRoad(new Point(1, 0));
        gameModel.placeResidential(new Point(2, 0));
        assertFalse(gameModel.canRoadBeDestroyed(gameModel.grid(0, 0), gameModel.grid(2, 0), gameModel.grid(1, 0)));

        gameModel.placeRoad(new Point(0, 1));
        gameModel.placeRoad(new Point(1, 1));
        gameModel.placeRoad(new Point(2, 1));
        assertTrue(gameModel.canRoadBeDestroyed(gameModel.grid(0, 0), gameModel.grid(2, 0), gameModel.grid(1, 0)));
    }

    @Test
    public void testWorkersRatio() {
        gameModel.placeIndustrial(new Point(0, 0));
        gameModel.placeService(new Point(2, 2));
        Person person = new Person();
        gameModel.getPeople().add(person);
        person.goToWork((Industrial) gameModel.grid(0, 0));
        assertEquals(1, gameModel.workersRatio());

        for (int i = 0; i < 2; i++) {
            Person p = new Person();
            gameModel.getPeople().add(p);
            p.goToWork((Service) gameModel.grid(2, 2));
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
        assertEquals(personMood - 10, person.getMood());
    }

    @Test
    public void testCleanUpAfterPerson() {
        Residential residential = new Residential(new Point(0, 0));
        School school = new School(new Point(2, 2));
        Industrial industrial = new Industrial(new Point(4, 4));
        Person person = new Person();
        person.moveIn(residential);
        person.goToSchool(school);
        Person person1 = new Person();
        person1.moveIn(residential);
        person1.goToWork(industrial);
        gameModel.getPeople().add(person);
        gameModel.getPeople().add(person1);

        gameModel.cleanUpAfterPerson(person);
        gameModel.cleanUpAfterPerson(person1);

        assertEquals(0, gameModel.getPeople().size());
        assertEquals(0, school.getPeople().size());
        assertEquals(0, industrial.getPeople().size());
    }

    @Test
    public void testDepartInhabitants() {
        Residential residential = new Residential(new Point(0, 0));
        Service service = new Service(new Point(2, 2));
        School school = new School(new Point(4, 4));
        for (int i = 0; i < 4; i++) {
            Person person = new Person();
            person.setMood((i + 1) * 10);
            person.moveIn(residential);
            if (i % 2 == 0) {
                person.goToSchool(school);
            } else {
                person.goToWork(service);
            }
            gameModel.getPeople().add(person);
        }
        gameModel.departInhabitants();
        assertTrue(gameModel.getPeople().size() < 4);
    }

    @Test
    public void testGrid() {
        assertNull(gameModel.grid(0, 0));
        gameModel.placeResidential(new Point(0, 0));
        assertTrue(gameModel.grid(0, 0) instanceof Residential);
        assertNull(gameModel.grid(-1, -1));

    }

}
