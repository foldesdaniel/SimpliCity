package PlaceablesTest.ZonesTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.Education.School;
import simplicity.Model.Game.FieldType;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class ZoneGeneralTest {

    Industrial industrial;
    Service service;
    Residential residential;

    @BeforeEach
    public void generateZones() {
        industrial = new Industrial(new Point(0, 0));
        service = new Service(new Point(3, 3));
        residential = new Residential(new Point(6, 6));
    }

    @Test
    public void testIndustrialConstructor() {
        assertSame(industrial.getType(), FieldType.ZONE_INDUSTRIAL);
        assertEquals(industrial.getPosition(), new Point(0, 0));
        assertEquals(6000, industrial.getBuildPrice());
        assertEquals(20, industrial.getMaxPeople());
    }

    @Test
    public void testIndustrialCalculateTax() {
        Person p = new Person();
        p.goToWork(industrial);
        assertEquals(200, industrial.calculateTax());

        Person p2 = new Person();
        p2.setEducationLevel(EducationLevel.SECONDARY);
        p2.goToWork(industrial);
        assertEquals(500, industrial.calculateTax());

        Person p3 = new Person();
        p3.setEducationLevel(EducationLevel.UNIVERSITY);
        p3.goToWork(industrial);
        assertEquals(900, industrial.calculateTax());
    }

    @Test
    public void testIndustrialCalculateMaintenance() {
        assertEquals(0, industrial.calculateMaintenance());
    }

    @Test
    public void testServiceConstructor() {
        assertSame(service.getType(), FieldType.ZONE_SERVICE);
        assertEquals(service.getPosition(), new Point(3, 3));
        assertEquals(9000, service.getBuildPrice());
        assertEquals(20, service.getMaxPeople());
    }

    @Test
    public void testServiceCalculateTax() {
        Person p = new Person();
        p.goToWork(service);
        assertEquals(300, service.calculateTax());

        Person p2 = new Person();
        p2.setEducationLevel(EducationLevel.SECONDARY);
        p2.goToWork(service);
        assertEquals(750, service.calculateTax());

        Person p3 = new Person();
        p3.setEducationLevel(EducationLevel.UNIVERSITY);
        p3.goToWork(service);
        assertEquals(1350, service.calculateTax());
    }

    @Test
    public void testServiceCalculateMaintenance() {
        assertEquals(0, service.calculateMaintenance());
    }

    @Test
    public void testResidentialConstructor() {
        assertSame(residential.getType(), FieldType.ZONE_RESIDENTIAL);
        assertEquals(residential.getPosition(), new Point(6, 6));
        assertEquals(5000, residential.getBuildPrice());
        assertEquals(4, residential.getMaxPeople());
        assertEquals(100, residential.getTaxPerInhabitant());
    }

    @Test
    public void testResidentialCalculateTax() {
        Person p = new Person();
        p.moveIn(residential);
        Person p2 = new Person();
        p2.moveIn(residential);
        Person p3 = new Person();
        p3.moveIn(residential);

        assertEquals(300, residential.calculateTax());
    }

    @Test
    public void testResidentialCalculateMaintenance() {
        assertEquals(0, residential.calculateMaintenance());
    }

    @Test
    public void testResidentialCalculateZoneMood() {
        assertEquals(0, residential.calculateZoneMood());

        Person p = new Person();
        p.setMood(20);
        p.moveIn(residential);
        Person p2 = new Person();
        p2.setMood(30);
        p2.moveIn(residential);
        Person p3 = new Person();
        p3.setMood(40);
        p3.moveIn(residential);

        assertEquals(30, residential.calculateZoneMood());
    }

    @Test
    public void testAreSpacesLeft() {
        assertTrue(residential.areSpacesLeft());
        for (int i = 0; i < 4; i++) {
            new Person().moveIn(residential);
        }
        assertFalse(residential.areSpacesLeft());
    }

    @Test
    public void testNumOfSpacesLeft() {
        assertEquals(4, residential.numOfSpacesLeft());
        for (int i = 0; i < 2; i++) {
            new Person().moveIn(residential);
        }
        assertEquals(2, residential.numOfSpacesLeft());
    }

    @Test
    public void testAddPerson() {
        residential.addPerson(new Person());
        assertEquals(1, residential.getPeople().size());
    }

    @Test
    public void testRemovePerson() {
        Person p = new Person();
        Person p2 = new Person();
        p.moveIn(residential);
        p2.moveIn(residential);
        residential.removePerson(1);
        residential.removePerson(p);

        assertEquals(0, residential.getPeople().size());
    }

    @Test
    public void testDeleteData() {
        Person p = new Person();
        p.setEducation(new School());
        Person p2 = new Person();
        p.setWorkplace(industrial);
        p.moveIn(residential);
        p2.moveIn(residential);

        residential.deleteData();
        for (int i = 0; i < residential.getPeople().size(); i++) {
            assertNull(residential.getPeople().get(i).getEducation());
            assertNull(residential.getPeople().get(i).getWorkplace());
        }
    }

}
