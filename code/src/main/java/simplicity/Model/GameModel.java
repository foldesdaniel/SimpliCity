package simplicity.Model;

import lombok.Getter;
import simplicity.Model.Algorithm.NodeCount;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Finances.Finance;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Listeners.WealthChangeListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;
import simplicity.Model.Resource.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.util.Queue;
import java.util.*;

public class GameModel implements InGameTimeTickListener {

    public static final String GAME_TITLE = "SimpliCity";
    public static final Image BACKGROUND_IMG = ResourceLoader.loadImage("bg_temp.jpg");
    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image SELECTION_VALID_IMG = ResourceLoader.loadImage("selection_valid.png");
    public static final Image SELECTION_INVALID_IMG = ResourceLoader.loadImage("selection_invalid.png");
    public static final Image TILE_HOVER_IMG = ResourceLoader.loadImage("hover.png");
    public static final Image ROAD_STRAIGHT_IMG = ResourceLoader.loadImage("road.png");
    public static final Image ROAD_TURN_IMG = ResourceLoader.loadImage("road_turn.png");
    public static final Image ROAD_T = ResourceLoader.loadImage("road_t.png");
    public static final Image ROAD_ALL = ResourceLoader.loadImage("road_all.png");
    public static final Image FOREST_IMG = ResourceLoader.loadImage("forest.png");
    public static final Image ZONE_RESIDENTIAL_IMG = ResourceLoader.loadImage("zone_residential.png");
    public static final Image ZONE_RESIDENTIAL_2_IMG = ResourceLoader.loadImage("zone_residential_2.png");
    public static final Image ZONE_WORK_SERVICE_IMG = ResourceLoader.loadImage("zone_work_service.png");
    public static final Image ZONE_WORK_INDUSTRIAL_IMG = ResourceLoader.loadImage("zone_work_industrial.png");
    public static final Image STADIUM_IMG = ResourceLoader.loadImage("stadium.png");
    public static final Image POLICE_IMG = ResourceLoader.loadImage("police.png");
    public static final Image EDUCATION_SCHOOL_IMG = ResourceLoader.loadImage("edu_school.png");
    public static final Image EDUCATION_UNIVERSITY_IMG = ResourceLoader.loadImage("edu_uni.png");
    public static final Font CUSTOM_FONT = ResourceLoader.loadFont("vt323.ttf");
    public static final Color BG_DARK = new Color(61, 63, 65); // default flatlaf dark
    public static final Point NO_SELECTION = new Point(-1, -1);
    public static final int DRAG_THRESHOLD = 5;
    private static GameModel instance;
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    public static final int GRID_SIZE = 20;
    private final ArrayList<MoralChangeListener> moralListeners = new ArrayList<>();
    private final ArrayList<PeopleChangeListener> peopleChangeListeners = new ArrayList<>();
    private final ArrayList<WealthChangeListener> wealthListeners = new ArrayList<>();
    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    @Getter
    private int cityMood = 60;
    private Placeable grid[][];
    private Finance finance;
    @Getter
    private ArrayList<Person> people = new ArrayList<>();
    private int r;

    public GameModel() {
        this.finance = new Finance(35000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;
        generateNextDisasterDate();

        //Initialize grid
        this.initGrid();

        /*for (int i = 0; i < 10; i++) {
            this.people.add(new Person());
        }*/

        //this.printGrid();

//        grid[0][0] = new Residential(new Point(0, 0));
//        grid[1][0] = new Residential(new Point(1, 0));
//        grid[0][1] = new Road(new Point(0, 1));
//        grid[1][1] = new Road(new Point(1, 1));
//        grid[1][2] = new Residential(new Point(1, 2));
//        grid[0][2] = new Industrial(new Point(0,2));
//        School s1 = new School(new Point(2,1));
//        grid[2][1] = s1;
//        grid[3][1] = new PlaceableTemp(s1, new Point(3,1));
//        grid[4][1] = new Road(new Point(4,1));
//        grid[5][1] = new Residential(new Point(5,1));


//        System.out.println(isPath(convertToNumMatrix(grid[5][1],grid[3][1],null)));
//
//        placeUniversity(new Point(6,6));
//        placeRoad(new Point(7,4));
//        placeResidential(new Point(7,3));
//        System.out.println((grid[7][5] instanceof PlaceableTemp) + " " + grid[7][5].getPosition());
//        System.out.println((grid[3][1] instanceof PlaceableTemp) + " " + grid[3][1].getPosition());

//        grid[0][3] = new Road(new Point(0,3));
//        grid[0][4] = new Road(new Point(0,4));
//        grid[0][5] = new Road(new Point(0,5));
//        grid[1][2] = new Road(new Point(1,2));
//        grid[2][2] = new Road(new Point(2,2));
//        grid[3][2] = new Road(new Point(3,2));
//        grid[3][3] = new Road(new Point(3,3));
//        grid[3][4] = new Road(new Point(3,4));
//        grid[3][5] = new Road(new Point(3,5));
//        grid[1][5] = new Road(new Point(1,5));
//
//
//        grid[2][5] = new Service(new Point(2, 5));
//
//        ((Residential)grid[0][1]).getPeople().get(0).goToWork((Workplace)grid[2][5]);
//        System.out.println(removeRoad(new Point(1, 2)));


        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
    }

    private void generateNextDisasterDate() {
        Random rand = new Random();
        int year = this.inGameTime.getInGameYear() + rand.nextInt(5) + 1;
        int day = rand.nextInt(364) + 1;
        this.nextDisaster = new Date(year, day, 0);
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    public static GameModel reset() {
        instance = null;
        return getInstance();
    }

    public static int showDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title + " | SimpliCity", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    public static void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title + " | SimpliCity", JOptionPane.WARNING_MESSAGE);
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

    private int countStadium(Point position) {
        int count = 0;
        int r = new Stadium(new Point(-1, -1)).getRadius();

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.STADIUM) count++;
                }
            }
        }

        return count / 4;
    }

    private int countPolice(Point position) {
        int count = 0;
        int r = new Police(new Point(-1, -1)).getRadius();

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.POLICE) count++;
                }
            }
        }

        return count;
    }

    private int countIndustrial(Point position) {
        int count = 0;
        int r = 5;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL) count++;
                }
            }
        }

        return count;
    }

    public Placeable grid(int i, int j) {
        return this.grid[i][j];
    }

    public void printGrid() {
        System.out.println("******************");
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                System.out.print(grid[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println("******************");
    }

    public void initGrid() {
        this.grid = new Placeable[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                this.grid[i][j] = null; //null == not initialized block
            }
        }
    }

    private boolean[][] freeSpaces() {
        boolean[][] spaces = new boolean[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                spaces[i][j] = true;
            }
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                Placeable p = this.grid[j][i];
                if (p != null) {
                    int width = p.getSize().width;
                    int height = p.getSize().height;
                    for (int ii = 0; ii < height; ii++) {
                        for (int jj = 0; jj < width; jj++) {
                            spaces[i - ii][j + jj] = false;
                        }
                    }
                }
            }
        }
        return spaces;
    }

    public boolean canPlace(Placeable p, Point position) {
        int x = position.x;
        int y = position.y;
        int width = p.getSize().width;
        int height = p.getSize().height;
        if (x + width > GRID_SIZE || y - (height - 1) < 0) return false;
        boolean[][] freeSpaces = this.freeSpaces();
        for (int i = 0; i < p.getSize().height; i++) {
            for (int j = 0; j < p.getSize().width; j++) {
                //if (grid[x + j][y - i] != null) {
                if (!freeSpaces[y - i][x + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    private void fillTemps(Placeable p, Point position) {
        Dimension size = p.getSize();
        if (size.width == 1 && size.height == 1) return;
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                if (i == 0 && j == 0) continue;
                Point newPos = new Point(position.x + j, position.y - i);
                this.grid[newPos.x][newPos.y] = new PlaceableTemp(p, newPos);
            }
        }
    }

    private void deleteTemps(Placeable p, Point position) {
        Dimension size = p.getSize();
        if (size.width == 1 && size.height == 1) return;
        for (int i = 0; i < size.height; i++) {
            for (int j = 0; j < size.width; j++) {
                if (i == 0 && j == 0) continue;
                Point newPos = new Point(position.x + j, position.y - i);
                this.grid[newPos.x][newPos.y] = null;
            }
        }
    }

    public void placeStadium(Point position) {
        Stadium pl = new Stadium(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int r = new Stadium(GameModel.NO_SELECTION).getRadius(); // TODO: placeholder radius
        int price = new Stadium(GameModel.NO_SELECTION).getBuildPrice();
        int maintenanceCost = new Stadium(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeMoney(price);
        finance.addBuilt(price, "Stadium építés");
        finance.addYearlySpend(maintenanceCost, "Stadium fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, 7);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                boostMood(p, 7);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeStadium(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        int price = ((Stadium) grid[position.x][position.y]).getBuildPrice() / 3;
        this.finance.addIncome(price,"Stadium törlés");
        this.finance.addMoney(price);
        grid[position.x][position.y] = null;
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        int maintenanceCost = new Stadium(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Stadium fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, -7);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                boostMood(p, -7);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placePolice(Point position) {
        Police pl = new Police(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int r = new Police(GameModel.NO_SELECTION).getRadius();
        int price = new Police(GameModel.NO_SELECTION).getBuildPrice();
        int maintenanceCost = new Police(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeMoney(price);
        finance.addBuilt(price, "Rendőrség építés");
        finance.addYearlySpend(maintenanceCost, "Rendőrség fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, 6);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removePolice(Point position) {
        int price = ((Police) grid[position.x][position.y]).getBuildPrice() / 3;
        this.finance.addIncome(price,"Rendőrség törlés");
        this.finance.addMoney(price);

        grid[position.x][position.y] = null;
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        int maintenanceCost = new Police(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Rendőrség fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, -6);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                boostMood(p, -6);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeIndustrial(Point position) {
        Industrial pl = new Industrial(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int r = 5;
        int price = new Industrial(GameModel.NO_SELECTION).getBuildPrice();
        finance.removeMoney(price);
        finance.addBuilt(price, "Ipari zóna kijelölés");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        if (isForestBetweenResidential_Industrial(position, grid[i][j].getPosition())) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, -3);
                            }
                        }
                        else {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, -7);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    private boolean isForestBetweenResidential_Industrial(Point industrial, Point residential) {
        int fx = industrial.x, fy = industrial.y;
        int rx = residential.x, ry = residential.y;

        if (fx != rx && fy != ry) return false; //not a straight line

        if (fx == rx) { //same row
            if (fy > ry) { // R --- F
                for (int i = ry + 1; i <= fy - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Forest)) return true;
                    }
                }
            }
            else { // F --- R
                for (int i = fy + 1; i <= ry - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Forest)) return true;
                    }
                }
            }
        }
        else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Forest)) return true;
                    }
                }
            }
            else {
                // F
                // -
                // R
                for (int i = fx + 1; i <= rx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Forest)) return true;
                    }
                }
            }
        }

        return false;
    }

    public void removeIndustrial(Point position, boolean forceRemove) {
        //check if it can be removed
        if (((Industrial) grid[position.x][position.y]).getPeople().size() > 0 && !forceRemove) return;

        int price = ((Industrial) grid[position.x][position.y]).getBuildPrice() / 3;
        this.finance.addIncome(price,"Ipari zóna törlés");
        this.finance.addMoney(price);

        ((Industrial) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        int r = 5;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        if (isForestBetweenResidential_Industrial(position, grid[i][j].getPosition())) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, 3);
                            }
                        }
                        else {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                boostMood(p, 7);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeRoad(Point position) {
        Road pl = new Road(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int price = new Road(GameModel.NO_SELECTION).getBuildPrice();
        finance.removeMoney(price);
        int maintenanceCost = new Road(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.addBuilt(price, "Út építés");
        finance.addYearlySpend(maintenanceCost, "Út fenntartási díj");

        //recalculating mood for every person
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                    for (Person p : ((Residential) grid[i][j]).getPeople()) {
                        //calculateMood(p); TODO
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public Boolean removeRoad(Point position) {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                    for (Person p : ((Residential) grid[i][j]).getPeople()) {
                        if (p.getWorkplace() != null) {
                            if (!canRoadBeDestroyed(grid[i][j], grid[p.getWorkplace().getPosition().x][p.getWorkplace().getPosition().y], grid[position.x][position.y])) {
                                return false;
                            }
                        } else if (p.getEducation() != null) {
                            if (!canRoadBeDestroyed(grid[i][j], grid[p.getEducation().getPosition().x][p.getEducation().getPosition().y], grid[position.x][position.y])) {
                                return false;
                            }
                        }
                    }
                }
            }
        }

        grid[position.x][position.y] = null;
        int maintenanceCost = new Road(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Út fenntartási díj");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
        return true;
    }

    public void placeService(Point position) {
        Service pl = new Service(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int price = new Service(GameModel.NO_SELECTION).getBuildPrice();
        finance.removeMoney(price);
        finance.addBuilt(price, "Szolgáltatási zóna kijelölés");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeService(Point position) {
        //check if it can be removed
        if (((Service) grid[position.x][position.y]).getPeople().size() > 0) return;

        int price = ((Service) grid[position.x][position.y]).getBuildPrice() / 3;
        this.finance.addIncome(price,"Szolgálatási zóna törlés");
        this.finance.addMoney(price);

        ((Service) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeResidential(Point position) {
        Residential pl = new Residential(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int price = new Residential(GameModel.NO_SELECTION).getBuildPrice();
        finance.removeMoney(price);
        finance.addBuilt(price, "Lakóhely zóna kijelölés");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeResidential(Point position) {
        //check if it can be removed
        if (((Residential) grid[position.x][position.y]).getPeople().size() > 0) return;

        int price = ((Residential) grid[position.x][position.y]).getBuildPrice() / 3;
        this.finance.addIncome(price,"Lakóhely zóna törlés");
        this.finance.addMoney(price);
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeSchool(Point position) {
        School pl = new School(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int price = new School(GameModel.NO_SELECTION).getBuildPrice();
        int maintenanceCost = new School(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeMoney(price);
        finance.addBuilt(price, "Iskola építés");
        finance.addYearlySpend(maintenanceCost, "Iskola fenntartási díj");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeSchool(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        ((School) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        int maintenanceCost = new School(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Iskola fenntartási díj");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeForest(Point position) {
        Forest pl = new Forest(position, new Date(this.inGameTime.getInGameYear(), this.inGameTime.getInGameDay(), this.inGameTime.getInGameHour()));
        if (!canPlace(pl, position)) return;

        int price = pl.getBuildPrice();
        finance.removeMoney(price);
        finance.addBuilt(price, "Erdő építés");
        int maintenanceCost = pl.getMaintenanceCost();
        finance.addYearlySpend(maintenanceCost, "Erdő fenntartási díj");

        grid[position.x][position.y] = pl;
        fillTemps(pl, position);

        int r = 3;

        boostForestMood(position, 1);

        //finance
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    private void boostForestMood(Point position, int boost) {
        int r = 3;
        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            if (doesForestBoostMood(position, grid[i][j].getPosition())) {
                                if (isIndustrialAfterForest(position, grid[i][j].getPosition())) {
                                    for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                        boostMood(p, boost + 3);
                                    }
                                }
                                else {
                                    for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                        boostMood(p, boost);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean isIndustrialAfterForest(Point forest, Point residential) {
        int fx = forest.x, fy = forest.y;
        int rx = residential.x, ry = residential.y;

        int r = 3;

        if (fx != rx && fy != ry) return false; //not a straight line

        if (fx == rx) { //same row
            if (fy > ry) { // R --- F
                for (int i = ry + 1; i <= fy + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Industrial)) return true;
                    }
                }
            }
            else { // F --- R
                for (int i = fy + 1; i <= ry + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Industrial)) return true;
                    }
                }
            }
        }
        else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Industrial)) return true;
                    }
                }
            }
            else {
                // F
                // -
                // R
                for (int i = fx + 1; i <= rx + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Industrial)) return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean doesForestBoostMood(Point forest, Point residential) {
        int fx = forest.x, fy = forest.y;
        int rx = residential.x, ry = residential.y;

        if (fx != rx && fy != ry) return false; //not a straight line

        if (fx == rx) { //same row
            if (fy > ry) { // R --- F
                for (int i = ry + 1; i <= fy - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && !(grid[fx][i] instanceof Road)) return false;
                    }
                }
            }
            else { // F --- R
                for (int i = fy + 1; i <= ry - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && !(grid[fx][i] instanceof Road)) return false;
                    }
                }
            }
        }
        else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && !(grid[i][ry] instanceof Road)) return false;
                    }
                }
            }
            else {
                // F
                // -
                // R
                for (int i = fx + 1; i <= rx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && !(grid[i][ry] instanceof Road)) return false;
                    }
                }
            }
        }

        return true;
    }

    public void removeForest(Point position) {
        int i = position.x, j = position.y;;
        int elapsed = this.inGameTime.getInGameYear() - ((Forest)grid[i][j]).getPlantTime().getYear();
        boostForestMood(position, -elapsed);

        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void placeUniversity(Point position) {
        University pl = new University(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        finance.removeMoney(new University(GameModel.NO_SELECTION).getBuildPrice());
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeUniversity(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        ((University) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    private Boolean searchForStadium(Person person) {
        //Searching around home first
        Residential home = person.getHome();
        Point homePosition = home.getPosition();
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        for (int i = homePosition.x - r; i <= homePosition.x + r; ++i) {
            for (int j = homePosition.y - r; j <= homePosition.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.STADIUM) {
                        return true;
                    }
                }
            }
        }

        //If not found around home then search around workplace
        Workplace workplace = person.getWorkplace();
        if (workplace == null) return false;
        Point workplacePosition = workplace.getPosition();

        for (int i = workplacePosition.x - r; i <= workplacePosition.x + r; ++i) {
            for (int j = workplacePosition.y - r; j <= workplacePosition.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.STADIUM) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private Boolean searchForPolice(Person person) {
        Residential home = person.getHome();
        Point homePosition = home.getPosition();
        int r = new Police(GameModel.NO_SELECTION).getRadius();

        for (int i = homePosition.x - r; i <= homePosition.x + r; ++i) {
            for (int j = homePosition.y - r; j <= homePosition.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.POLICE) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int getWorkplaceDistance(Person person, String type) {
        if (type.equals("uni") || type.equals("secondary")) type = "school";
        Residential home = person.getHome();
        Point position = home.getPosition();

        Placeable workplace = null;
        if (type.equals("workplace")) workplace = person.getWorkplace();
        if (type.equals("school")) workplace = person.getEducation();
        if (workplace == null) return 0;
        Point workplacePosition = workplace.getPosition();

        Queue<NodeCount> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();

        if (position.x + 1 < GRID_SIZE && grid[position.x + 1][position.y] != null) {
            if (grid[position.x + 1][position.y].getType() == FieldType.ROAD) {
                queue.add(new NodeCount(new Point(position.x + 1, position.y), 1));
                visited.add(new Point(position.x + 1, position.y));
            }
            if (grid[position.x + 1][position.y].getPosition().equals(workplacePosition)) {
                return 1;
            }
        }
        if (position.y + 1 < GRID_SIZE && grid[position.x][position.y + 1] != null) {
            if (grid[position.x][position.y + 1].getType() == FieldType.ROAD) {
                queue.add(new NodeCount(new Point(position.x, position.y + 1), 1));
                visited.add(new Point(position.x, position.y + 1));
            }
            if (grid[position.x][position.y + 1].getPosition().equals(workplacePosition)) {
                return 1;
            }
        }
        if (position.y - 1 >= 0 && grid[position.x][position.y - 1] != null) {
            if (grid[position.x][position.y - 1].getType() == FieldType.ROAD) {
                queue.add(new NodeCount(new Point(position.x, position.y - 1), 1));
                visited.add(new Point(position.x, position.y - 1));
            }
            if (grid[position.x][position.y - 1].getPosition().equals(workplacePosition)) {
                return 1;
            }
        }
        if (position.x - 1 >= 0 && grid[position.x - 1][position.y] != null) {
            if (grid[position.x - 1][position.y].getType() == FieldType.ROAD) {
                queue.add(new NodeCount(new Point(position.x - 1, position.y), 1));
                visited.add(new Point(position.x - 1, position.y));
            }
            if (grid[position.x - 1][position.y].getPosition().equals(workplacePosition)) {
                return 1;
            }
        }

        while (!queue.isEmpty()) {
            NodeCount nc = queue.remove();
            position = nc.position;
            //System.out.println("Position : " + position.x + " " + position.y);

            if (!visited.contains(new Point(position.x + 1, position.y)) && position.x + 1 < GRID_SIZE && grid[position.x + 1][position.y] != null) {
                if (grid[position.x + 1][position.y].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x + 1, position.y), nc.count + 1));
                    visited.add(new Point(position.x + 1, position.y));
                } else if (grid[position.x + 1][position.y].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x, position.y + 1)) && position.y + 1 < GRID_SIZE && grid[position.x][position.y + 1] != null) {
                if (grid[position.x][position.y + 1].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x, position.y + 1), nc.count + 1));
                    visited.add(new Point(position.x, position.y + 1));
                } else if (grid[position.x][position.y + 1].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x, position.y - 1)) && position.y - 1 >= 0 && grid[position.x][position.y - 1] != null) {
                if (grid[position.x][position.y - 1].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x, position.y - 1), nc.count + 1));
                    visited.add(new Point(position.x, position.y - 1));
                } else if (grid[position.x][position.y - 1].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x - 1, position.y)) && position.x - 1 >= 0 && grid[position.x - 1][position.y] != null) {
                if (grid[position.x - 1][position.y].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x - 1, position.y), nc.count + 1));
                    visited.add(new Point(position.x - 1, position.y));
                } else if (grid[position.x - 1][position.y].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
        }

        return -1;
    }

    private Boolean searchForIndustrial(Person person) {
        Residential home = person.getHome();
        Point homePosition = home.getPosition();
        int r = 5;

        for (int i = homePosition.x - r; i <= homePosition.x + r; ++i) {
            for (int j = homePosition.y - r; j <= homePosition.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void boostMood(Person p, int boost) {
        p.setBoostMood(p.getBoostMood() + boost);
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();

    }

    private void boostPersonMoodBasedOnDistance(Person person, String type) {
        //TODO: when object is deleted then recalculate the distance boost
        if (getWorkplaceDistance(person, type) < 6) boostMood(person, 4);
        else if (getWorkplaceDistance(person, type) < 12) boostMood(person, -2);
        else boostMood(person, -7);
    }

    private boolean searchForJob(Person person, String type) {
        Residential home = person.getHome();
        Point position = home.getPosition();
        int x = position.x;
        int y = position.y;

        if (type.equals("secondary") || type.equals("uni")) {
            for (int i = 0; i < GRID_SIZE; ++i) {
                for (int j = 0; j < GRID_SIZE; ++j) {
                    if (!(x == i && y == j) && grid[i][j] != null) {
                        Placeable current = grid[i][j];
                        Placeable temp;
                        if (current instanceof PlaceableTemp) {
                            temp = current;
                            current = ((PlaceableTemp) current).getPlaceable();
                        } else {
                            temp = current;
                        }
                        //GO TO SCHOOL
                        if (type.equals("secondary")) {
                            if (current.getType() == FieldType.SCHOOL) {
                                //HIGH SCHOOL
                                if (((School) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((School) current).getPeople().contains(person)) {
                                    person.goToSchool(((School) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        } else if (type.equals("uni")) {
                            if (current.getType() == FieldType.UNIVERSITY) {
                                if (((University) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((University) current).getPeople().contains(person)) {
                                    person.goToSchool(((University) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        else if (type.equals("workplace")) {
            for (int i = 0; i < GRID_SIZE; ++i) {
                for (int j = 0; j < GRID_SIZE; ++j) {
                    if (!(x == i && y == j) && grid[i][j] != null) {
                        Placeable current = grid[i][j];
                        Placeable temp;
                        if (current instanceof PlaceableTemp) {
                            temp = current;
                            current = ((PlaceableTemp) current).getPlaceable();
                        } else {
                            temp = current;
                        }
                        if (workersRatio() == 1) { //we need service workers
                            if (current.getType() == FieldType.ZONE_SERVICE) {
                                if (((Service) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Service) current).getPeople().contains(person)) {
                                    person.goToWork(((Service) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        } else { //we need industrial workers
                            if (current.getType() == FieldType.ZONE_INDUSTRIAL) {
                                if (((Industrial) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Industrial) current).getPeople().contains(person)) {
                                    person.goToWork(((Industrial) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < GRID_SIZE; ++i) {
                for (int j = 0; j < GRID_SIZE; ++j) {
                    if (!(x == i && y == j) && grid[i][j] != null) {
                        Placeable current = grid[i][j];
                        Placeable temp;
                        if (current instanceof PlaceableTemp) {
                            temp = current;
                            current = ((PlaceableTemp) current).getPlaceable();
                        } else {
                            temp = current;
                        }
                        if (workersRatio() == 1) { //we need NOT service workers
                            if (current.getType() == FieldType.ZONE_INDUSTRIAL) {
                                if (((Industrial) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Industrial) current).getPeople().contains(person)) {
                                    person.goToWork(((Industrial) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        } else { //we need NOT industrial workers
                            if (current.getType() == FieldType.ZONE_SERVICE) {
                                if (((Service) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Service) current).getPeople().contains(person)) {
                                    person.goToWork(((Service) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private int workersRatio() {
        //return 1 if we need service workers
        //return 0 if we need industrial workers

        int industrialCount = 0;
        int serviceCount = 0;

        for (Person p : this.people) {
            if (p.getWorkplace() != null) {
                if (p.getWorkplace() instanceof Industrial) industrialCount++;
                else if (p.getWorkplace() instanceof Service) serviceCount++;
            }
        }

        return industrialCount > serviceCount ? 1 : 0;
    }

    private void calculateMood(Person person) {
        int count = countStadium(person.getHome().getPosition());
        person.setBoostMood(count * 7);
        count = countPolice(person.getHome().getPosition());
        person.setBoostMood(count * 6);
        count = countIndustrial(person.getHome().getPosition());
        person.setBoostMood(-count * 6);
        int forestMood = calculateForestMood(person.getHome().getPosition());
        person.setBoostMood(forestMood);
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
        //todo : searchForForest
    }

    private int calculateForestMood(Point position) {
        int sum = 0;

        int r = 3;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.FOREST) {
                        int elapsed = this.inGameTime.getInGameYear() - ((Forest)grid[i][j]).getPlantTime().getYear();
                        sum += elapsed;
                    }
                }
            }
        }

        return sum;
    }

    private boolean canRoadBeDestroyed(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        boolean directPath = isPath(convertToNumMatrix(startPoint, endPoint, null));
        boolean moreThanOnePath = isPath(convertToNumMatrix(startPoint, endPoint, toBeDestroyed));
        return directPath && (moreThanOnePath);
    }

    private int[][] convertToNumMatrix(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        int[][] matrix = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] instanceof Road) matrix[i][j] = 3;
                else matrix[i][j] = 0;
            }
        }
        matrix[startPoint.getPosition().x][startPoint.getPosition().y] = 1;
        matrix[endPoint.getPosition().x][endPoint.getPosition().y] = 2;
        if (toBeDestroyed != null) matrix[toBeDestroyed.getPosition().x][toBeDestroyed.getPosition().y] = 0;
        return matrix;
    }

    private boolean isPath(int[][] matrix) {
        int n = GRID_SIZE;
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

    private void newYearTaxCollection() {
        int sum = 0;
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if ((grid[i][j] != null)) {
                    sum += grid[i][j].calculateTax();

                }
            }
        }
        finance.addIncome(sum, "Éves adó összeg");
        finance.addMoney(sum);
    }

    private void calculateCityMood() {
        if (this.people.size() == 0) return;
        int cityMood = 0;
        int numOfZones = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (this.grid[i][j] instanceof Residential) {
                    cityMood += ((Residential) this.grid[i][j]).calculateZoneMood();
                    if (((Residential) this.grid[i][j]).getPeople().size() != 0) {
                        numOfZones++;
                    }
                }
            }
        }
        if (numOfZones != 0) {
            this.cityMood = cityMood / numOfZones;
        }
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }

    public void addMoralChangeListener(MoralChangeListener l) {
        this.moralListeners.add(l);
    }

    public void addPeopleChangeListener(PeopleChangeListener l) {
        this.peopleChangeListeners.add(l);
    }

    public void addWealthChangeListener(WealthChangeListener l) {
        this.wealthListeners.add(l);
    }

    public int getCurrentWealth() {
        return this.finance.getCurrentWealth();
    }

    private void changeMoodOfPeople() {
        if (this.finance.getCurrentWealth() < -8000) {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() - 0.5);
        } else {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() + 0.5);
        }

        double multiplier = 1;
        if (this.finance.getProfitableYearsInARow() < -1.5) {
            //gameover
            multiplier = 0.85;
        } else if (this.finance.getProfitableYearsInARow() > 1.5) {
            multiplier = 1.15;
        } else {
            multiplier = (10 + this.finance.getProfitableYearsInARow()) / 10.0;
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
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
                //change mood based on tax
                if (this.grid[i][j] instanceof Residential) {
                    int size = ((Residential) this.grid[i][j]).getPeople().size();
                    int max = ((Residential) this.grid[i][j]).getMaxPeople();

                    for (Person p : ((Residential) this.grid[i][j]).getPeople()) {
                        if (size == max) boostMood(p, -4);
                        else if (size > 2 * max / 3) boostMood(p, -2);
                        else if (size > max / 2) boostMood(p, 1);
                        else if (size > max / 3) boostMood(p, 2);
                        else boostMood(p, 7);
                    }
                } else if (this.grid[i][j] instanceof Service) {
                    int size = ((Service) this.grid[i][j]).getPeople().size();
                    int max = ((Service) this.grid[i][j]).getMaxPeople();

                    for (Person p : ((Service) this.grid[i][j]).getPeople()) {
                        if (size == max) boostMood(p, -4);
                        else if (size > 2 * max / 3) boostMood(p, -2);
                        else if (size > max / 2) boostMood(p, 1);
                        else if (size > max / 3) boostMood(p, 2);
                        else boostMood(p, 7);
                    }
                } else if (this.grid[i][j] instanceof Industrial) {
                    int size = ((Industrial) this.grid[i][j]).getPeople().size();
                    int max = ((Industrial) this.grid[i][j]).getMaxPeople();

                    for (Person p : ((Industrial) this.grid[i][j]).getPeople()) {
                        if (size == max) boostMood(p, -4);
                        else if (size > 2 * max / 3) boostMood(p, -2);
                        else if (size > max / 2) boostMood(p, 1);
                        else if (size > max / 3) boostMood(p, 2);
                        else boostMood(p, 7);
                    }
                }
            }
        }
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }

    private boolean isMoodGoodEnough() {
        return this.cityMood >= 30;
    }

    private void welcomeNewInhabitants() {
        int freeSpace = 0;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (this.grid[i][j] instanceof Residential && isNextToARoad(new Point(i, j))) {
                    freeSpace += ((Residential) this.grid[i][j]).numOfSpacesLeft();
                }
            }
        }
        double incomingNewPeople = Math.ceil(freeSpace * (cityMood / 100.0));
        for (int i = 0; i < (int) incomingNewPeople; i++) {
            Person tmp = new Person(findHome(), cityMood);
            calculateMood(tmp);
            this.people.add(tmp);
            for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
            for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
        }
    }

    private void departInhabitants() {
        double outgoingPeople = Math.ceil(this.people.size() * ((100 - cityMood - 30) / 100.0));
        //remove outgoingPeople amount of people from this.people who have the lowest mood
        for (int i = 0; i < outgoingPeople; i++) {
            int lowestMood = 101;
            Person lp = null;
            for (Person p : this.people) {
                if (p.getMood() < lowestMood) {
                    lowestMood = p.getMood();
                    lp = p;
                }
            }
            //TODO lp could be null
            if (lp.getWorkplace() != null) lp.getWorkplace().removePerson(lp);
            if (lp.getHome() != null) lp.getHome().removePerson(lp);
            if (lp.getEducation() != null) {
                int indexOfPerson = lp.getEducation().getPeople().indexOf(lp);
                lp.getEducation().getArrivalDates().remove(indexOfPerson);
                lp.getEducation().removePerson(lp);
            }
            this.people.remove(lp);
        }
        for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
    }

    private Residential findHome() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (this.grid[i][j] instanceof Residential && ((Residential) this.grid[i][j]).areSpacesLeft() && isNextToARoad(new Point(i, j))) {
                    return ((Residential) this.grid[i][j]);
                }
            }
        }
        return null;
    }

    private void findOccupation() {

//        printCurrentEmployment();

        Random random = new Random();
        for (Person person : this.people) {
            if (person.getEducation() == null && person.getWorkplace() == null) {
                int occupation = random.nextInt(2);
                //0 - study
                //1 - work
                if (occupation == 0) {
                    if (person.getEducationLevel() == EducationLevel.PRIMARY) {
                        searchForJob(person, "secondary");
                    } else if (person.getEducationLevel() == EducationLevel.SECONDARY) {
                        searchForJob(person, "uni");
                    } else searchForJob(person, "workplace");
                } else {
                    searchForJob(person, "workplace");
                }

                //if school or workplace not found then search for the other one
                if (person.getEducation() == null && person.getWorkplace() == null) {
                    if (occupation == 1) {
                        if (person.getEducationLevel() == EducationLevel.PRIMARY) {
                            searchForJob(person, "secondary");
                        } else if (person.getEducationLevel() == EducationLevel.SECONDARY) {
                            searchForJob(person, "uni");
                        } else searchForJob(person, "workplace");
                    } else {
                        searchForJob(person, "workplace");
                    }
                }

                //reduce person mood if job not found
                if (person.getEducation() == null && person.getWorkplace() == null) {
                    boostMood(person, -4);
                }
            }

        }

//        printCurrentEmployment();
    }

    private void printCurrentEmployment() {
        for (Person p : this.people) {
            System.out.println("M: " + p.getMood() + " | E: " + p.getEducation() + " | W: " + p.getWorkplace());
        }
        System.out.println("-------------------------");
    }


    private boolean isNextToARoad(Point point) {
        int x = point.x;
        int y = point.y;

        if (x - 1 >= 0 && this.grid[x - 1][y] instanceof Road) return true;
        if (x + 1 < GRID_SIZE && this.grid[x + 1][y] instanceof Road) return true;
        if (y - 1 >= 0 && this.grid[x][y - 1] instanceof Road) return true;
        if (y + 1 < GRID_SIZE && this.grid[x][y + 1] instanceof Road) return true;
        return false;
    }

    private void newYearMaintenanceCost() {
        int sum = 0;
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if ((grid[i][j] != null)) {
                    sum += grid[i][j].calculateMaintenance();
                }
            }
        }
        //TODO: ??? finance class : implement show yearly maintenance cost ???
        finance.removeMoney(sum);
    }

    private void newYearForest() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j] instanceof Forest) {
                    int elapsed = this.inGameTime.getInGameYear() - ((Forest)grid[i][j]).getPlantTime().getYear();
                    if (elapsed <= 10) boostForestMood(grid[i][j].getPosition(), 1);
                    else  {
                        int maintenanceCost = ((Forest)grid[i][j]).getMaintenanceCost();
                        finance.removeYearlySpend(maintenanceCost, "Erdő fenntartási díj");
                        ((Forest)grid[i][j]).setMaintenanceCost(0);
                    }
                }
            }
        }
    }

    private boolean isIndustrialBuiltAlready() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j] instanceof Industrial) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point searchRandomIndustrial() {
        while (true) {
            Random rand = new Random();
            int i = rand.nextInt(GRID_SIZE);
            int j = rand.nextInt(GRID_SIZE);
            if (grid[i][j] != null && grid[i][j] instanceof Industrial) return grid[i][j].getPosition();
        }
    }

    private void doIndustrialDisaster(Point position) {
        //animation for 3-5 sec

        removeIndustrial(position, true);
    }

    private void removeDepressedPeople() {
        for (int i = 0; i < this.people.size(); i++) {
            Person p = this.people.get(i);
            if (p.getMood() == 0) {
                System.out.println("Here");
                if (p.getEducation() != null) {
                    int indexOfPerson = p.getEducation().getPeople().indexOf(p);
                    p.getEducation().getArrivalDates().remove(indexOfPerson);
                    p.getEducation().removePerson(p);
                }
                if (p.getWorkplace() != null) {
                    p.getWorkplace().removePerson(p);
                    System.out.println("removed");
                }
                p.getHome().removePerson(p);
                this.people.remove(p);
                System.out.println("Removed a person!");
            }
        }
    }

    @Override
    public void timeTick() {
        if (this.inGameTime.getInGameHour() > 0) {
            calculateCityMood();
            removeDepressedPeople();
        }
        if (this.inGameTime.getInGameDay() > 0 && this.inGameTime.getInGameDay() % 20 == 0 && this.inGameTime.getInGameHour() == 0) {
            if (isMoodGoodEnough()) {
                welcomeNewInhabitants();
                findOccupation();
            } else {
                departInhabitants();
            }
        }
        if (this.inGameTime.getInGameYear() > 0 && this.inGameTime.getInGameDay() == 0 && this.inGameTime.getInGameHour() == 0) {
            //triggers new year tax collection
            //and city mood change
            changeMoodOfPeople();
            newYearTaxCollection();
            newYearMaintenanceCost();
            newYearForest();
        }
        if (this.inGameTime.getInGameYear() == nextDisaster.getYear() && this.inGameTime.getInGameDay() == nextDisaster.getDay()) {
            //disaster logic
            generateNextDisasterDate();
            if (isIndustrialBuiltAlready()) {
                Point position = searchRandomIndustrial();
                doIndustrialDisaster(position);
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }
}
