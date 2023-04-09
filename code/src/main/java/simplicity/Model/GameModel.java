package simplicity.Model;

import simplicity.Model.Education.School;
import simplicity.Model.Finances.Finance;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.Road;
import simplicity.Model.Resource.ResourceLoader;
import simplicity.Model.Zones.Residential;

import java.awt.*;

public class GameModel implements InGameTimeTickListener {

    public static final String GAME_TITLE = "SimpliCity";

    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image SELECTION_2_IMG = ResourceLoader.loadImage("selection2.png");
    public static final Image ROAD_STRAIGHT_IMG = ResourceLoader.loadImage("road.png");
    public static final Image ROAD_TURN_IMG = ResourceLoader.loadImage("road_turn.png");
    public static final Image ROAD_T = ResourceLoader.loadImage("road_t.png");
    public static final Image ROAD_ALL = ResourceLoader.loadImage("road_all.png");
    public static final Font CUSTOM_FONT = ResourceLoader.loadFont("vt323.ttf");
    public static final Color BG_DARK = new Color(61, 63, 65); // default flatlaf dark
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    //just for testing purposes
    private final int gridSize = 2;
    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    private int cityMood;
    private Placeable grid[][];
    private Finance finance;

    public GameModel() {
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
        this.finance = new Finance(10000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;

        //Initialize grid
//        this.grid = new Placeable[][]{
//                {new School(new Point(0, 0)), new School(new Point(0, 1)), new Road(new Point(0, 2))},
//                {new School(new Point(1, 0)), new Road(new Point(1, 1)), new Road(new Point(1, 2))},
//                {new School(new Point(2, 0)), new School(new Point(2, 1)), new Road(new Point(2, 2))}
//        };
        this.grid = new Placeable[][]{
                {new School(new Point(0, 0)), new Residential(new Point(0, 1))},
                {new Residential(new Point(1, 0)), new Road(new Point(1, 1))}
        };

        Person p1 = new Person();
        Person p2 = new Person();
        Person p3 = new Person();
        Person p4 = new Person();

        p1.moveIn((Residential) grid[0][1]);
        p2.moveIn((Residential) grid[0][1]);
        p3.moveIn((Residential) grid[1][0]);
        p3.moveIn((Residential) grid[1][0]);

        System.out.println(((Residential) grid[0][1]).calculateZoneMood());
        System.out.println(((Residential) grid[1][0]).calculateZoneMood());

    }

    public static boolean isSafe(int i, int j, int[][] matrix) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length;
    }

    public static boolean isPath(int[][] matrix, int i, int j, boolean[][] visited) {
        if (isSafe(i, j, matrix) && matrix[i][j] != 0 && !visited[i][j]) {

            visited[i][j] = true;

            if (matrix[i][j] == 2) return true;

            boolean up = isPath(matrix, i - 1, j, visited);
            if (up) return true;

            boolean left = isPath(matrix, i, j - 1, visited);
            if (left) return true;

            boolean down = isPath(matrix, i + 1, j, visited);
            if (down) return true;

            boolean right = isPath(matrix, i, j + 1, visited);
            if (right) return true;
        }
        return false;
    }

    public boolean canRoadBeDestroyed(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        boolean directPath = isPath(convertToNumMatrix(startPoint, endPoint, null), gridSize);
        boolean moreThanOnePath = isPath(convertToNumMatrix(startPoint, endPoint, toBeDestroyed), gridSize);
        return directPath && (moreThanOnePath);
    }

    public int[][] convertToNumMatrix(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        int[][] matrix = new int[this.gridSize][this.gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (grid[i][j] instanceof Road) matrix[i][j] = 3;
                else matrix[i][j] = 0;
            }
        }
        matrix[startPoint.getPosition().x][startPoint.getPosition().y] = 1;
        matrix[endPoint.getPosition().x][endPoint.getPosition().y] = 2;
        if (toBeDestroyed != null) matrix[toBeDestroyed.getPosition().x][toBeDestroyed.getPosition().y] = 0;
        return matrix;
    }

    public boolean isPath(int[][] matrix, int n) {
        boolean[][] visited = new boolean[n][n];
        boolean flag = false;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1 && !visited[i][j]) {
                    if (isPath(matrix, i, j, visited)) {
                        flag = true;
                        break;
                    }
                }
            }
        }
        return flag;
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

    void calculateCityMood() {
        int cityMood = 0;
        int numOfZones = 0;
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j] instanceof Residential) {
                    cityMood += ((Residential) this.grid[i][j]).calculateZoneMood();
                    numOfZones++;
                }
            }
        }
        if (numOfZones != 0) {
            this.cityMood = (int) cityMood / numOfZones;
        }
    }

    public void changeMoodOfPeople() {
        if (this.finance.getCurrentWealth() < -10000) {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() - 1);
        } else {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() + 1);
        }

        double multiplier = 1;
        if (this.finance.getProfitableYearsInARow() < -3) {
            //gameover
            multiplier = 0.7;
        } else if (this.finance.getProfitableYearsInARow() > 3) {
            multiplier = 1.3;
        } else {
            multiplier = (10 + this.finance.getProfitableYearsInARow()) / 10.0;
        }
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j] instanceof Residential) {
                    for (int k = 0; k < ((Residential) this.grid[i][j]).getPeople().size(); k++) {
                        if (multiplier >= 1) {
                            ((Residential) this.grid[i][j])
                                    .getPeople()
                                    .get(k)
                                    .setMood(Math.min((int) (((Residential) this.grid[i][j]).getPeople().get(k).getMood() * multiplier), 100));
                        } else {
                            ((Residential) this.grid[i][j])
                                    .getPeople()
                                    .get(k)
                                    .setMood(Math.max((int) (((Residential) this.grid[i][j]).getPeople().get(k).getMood() * multiplier), 0));
                        }
                    }
                }
            }
        }
    }


    @Override
    public void timeTick() {
        if (inGameTime.getInGameHour() > 0) {
            System.out.println("City mood: " + this.cityMood);
            calculateCityMood();
        }
//        System.out.println("******************");
//        for (int i = 0; i < gridSize; ++i) {
//            for (int j = 0; j < gridSize; ++j) {
//                System.out.print(grid[i][j] + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("Current money : " + finance.getCurrentWealth());
        System.out.println("******************");
        if (inGameTime.getInGameYear() > 0 && inGameTime.getInGameDay() == 0 && inGameTime.getInGameHour() == 0) {
            //triggers new year tax collection
            //and city mood change
            changeMoodOfPeople();
        }
    }
}
