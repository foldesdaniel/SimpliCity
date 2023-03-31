package simplicity.Model;

import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Education.School;
import simplicity.Model.Finances.Finance;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Forest;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Resource.ResourceLoader;
import simplicity.Model.Zones.Industrial;

import java.awt.*;

public class GameModel implements InGameTimeTickListener {

    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image ROAD_STRAIGHT_IMG = ResourceLoader.loadImage("road.png");
    public static final Image ROAD_TURN_IMG = ResourceLoader.loadImage("road_turn.png");
    public static final Image ROAD_T = ResourceLoader.loadImage("road_t.png");
    public static final Image ROAD_ALL = ResourceLoader.loadImage("road_all.png");

    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    private Placeable grid[][];
    private final int gridSize = 20;
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    private Finance finance;

    public GameModel() {
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.NORMAL);
        this.finance = new Finance(10000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;

        //Initialize grid
        grid = new Placeable[gridSize][gridSize];
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                grid[i][j] = null; //null == not initialized block
            }
        }

        //TEST
        Industrial industrial = new Industrial(new Point(10, 10));
        Forest forest = new Forest(new Point(10, 10), new Date(10, 10, 10));
        industrial.addPerson(new Person());
        industrial.addPerson(new Person());
        industrial.addPerson(new Person());
        grid[10][10] = industrial;
        finance.removeMoney(industrial.getBuildPrice());
        newYearTaxCollection();
    }

    @Override
    public void timeTick() {
        /*System.out.println("******************");
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("Current money : " + finance.getCurrentWealth());
        System.out.println("******************");
        if (inGameTime.getInGameYear() > 0 && inGameTime.getInGameDay() == 0 && inGameTime.getInGameHour() == 0) {
            //triggers new year
        }*/
    }

    void newYearTaxCollection() {
        int sum = 0;
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                sum += (grid[i][j] != null) ? grid[i][j].calculateTax() : 0;
            }
            System.out.println();
        }
        finance.removeMoney(sum);
        System.out.println(finance.getCurrentWealth());
    }
}
