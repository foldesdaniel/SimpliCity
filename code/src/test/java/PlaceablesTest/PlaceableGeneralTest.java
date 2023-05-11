package PlaceablesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.Placeables.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlaceableGeneralTest {

    Forest forest;
    Police police;
    Road road;
    Stadium stadium;
    PlaceableTemp placeableTemp;

    @BeforeEach
    public void generatePlaceables() {
        forest = new Forest(new Point(0, 0), new Date(1, 1, 1));
        police = new Police(new Point(2, 2));
        road = new Road(new Point(4, 4));
        stadium = new Stadium(new Point(7, 7));
        placeableTemp = new PlaceableTemp(stadium, new Point(8, 7));
    }

    @Test
    public void testForestConstructor() {
        Date date = new Date(1, 1, 1);
        assertEquals(FieldType.FOREST, forest.getType());
        assertEquals(new Point(0, 0), forest.getPosition());
        assertEquals(5000, forest.getBuildPrice());
        assertEquals(date.getYear(), forest.getPlantTime().getYear());
        assertEquals(date.getDay(), forest.getPlantTime().getDay());
        assertEquals(date.getHour(), forest.getPlantTime().getHour());
        assertEquals(500, forest.getMaintenanceCost());
    }

    @Test
    public void testForestCalculateTax() {
        assertEquals(0, forest.calculateTax());
    }

    @Test
    public void testCalculateMaintenance() {
        assertEquals(0, forest.calculateMaintenance());
    }

    @Test
    public void testRoadConnects() {
        assertFalse(forest.roadConnects());
    }

    @Test
    public void testPoliceConstructor() {
        assertEquals(FieldType.POLICE, police.getType());
        assertEquals(new Point(2, 2), police.getPosition());
        assertEquals(2000, police.getBuildPrice());
        assertEquals(1.2f, police.getMoodBoost());
        assertEquals(4, police.getRadius());
        assertEquals(500, police.getMaintenanceCost());
    }

    @Test
    public void testPoliceConnects() {
        assertFalse(police.roadConnects());
    }

    @Test
    public void testRoadConstructor() {
        assertEquals(FieldType.ROAD, road.getType());
        assertEquals(new Point(4, 4), road.getPosition());
        assertEquals(600, road.getBuildPrice());
        assertEquals(100, road.getMaintenanceCost());
    }

    @Test
    public void testRoadCalculateTax() {
        assertEquals(100, road.calculateTax());
    }

    @Test
    public void testRoadCalculateMaintenance() {
        assertEquals(0, road.calculateMaintenance());
    }

    @Test
    public void testStadiumConstructor() {
        assertEquals(FieldType.STADIUM, stadium.getType());
        assertEquals(new Point(7, 7), stadium.getPosition());
        assertEquals(3000, stadium.getBuildPrice());
        assertEquals(1.2f, stadium.getMoodBoost());
        assertEquals(5, stadium.getRadius());
        assertEquals(500, stadium.getMaintenanceCost());
    }

    @Test
    public void testPlaceableTempConstructor() {
        assertEquals(FieldType.STADIUM, placeableTemp.getType());
        assertEquals(new Point(8, 7), placeableTemp.getPosition());
        assertEquals(3000, placeableTemp.getBuildPrice());
        assertEquals(stadium, placeableTemp.getPlaceable());
    }

    @Test
    public void testPlaceableTempCalculateTax() {
        assertEquals(0, placeableTemp.calculateTax());
    }

    @Test
    public void testPlaceableTempCalculateMaintenance() {
        assertEquals(500, placeableTemp.calculateMaintenance());
    }

    @Test
    public void testGetDisplayPosition() {
        assertEquals(new Point(7, 7), placeableTemp.getDisplayPosition());
    }

}
