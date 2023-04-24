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
    private static GameModel instance;
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    //just for testing purposes
    @Getter
    private final int gridSize = 20;
    private final ArrayList<MoralChangeListener> moralListeners = new ArrayList<>();
    private final ArrayList<PeopleChangeListener> peopleChangeListeners = new ArrayList<>();
    private final ArrayList<WealthChangeListener> wealthListeners = new ArrayList<>();
    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    @Getter
    private int cityMood = 50;
    private Placeable grid[][];
    private Finance finance;
    private int industrialCount = 0;
    private int serviceCount = 0;
    @Getter
    private ArrayList<Person> people = new ArrayList<>();
    public GameModel() {
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
        this.finance = new Finance(-1000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;

        //Initialize grid
        this.initGrid();

        /*for (int i = 0; i < 10; i++) {
            this.people.add(new Person());
        }*/

        //TESTING feature/14_mood
        //this.printGrid();

        grid[0][0] = new Residential(new Point(0, 0));
        grid[1][0] = new Residential(new Point(1, 0));
        grid[0][1] = new Road(new Point(0, 1));
        grid[1][1] = new Road(new Point(1, 1));
        grid[1][2] = new Residential(new Point(1, 2));
        grid[0][2] = new Industrial(new Point(0,2));
        School s1 = new School(new Point(2,1));
        grid[2][1] = s1;
        grid[3][1] = new PlaceableTemp(s1);

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


    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.STADIUM) count++;
                }
            }
        }

        return count;
    }

    private int countPolice(Point position) {
        int count = 0;
        int r = new Police(new Point(-1, -1)).getRadius();

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
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
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                System.out.print(grid[j][i] + " ");
            }
            System.out.println();
        }
        System.out.println("******************");
    }

    public void initGrid() {
        this.grid = new Placeable[this.gridSize][this.gridSize];
        for (int i = 0; i < this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                this.grid[i][j] = null; //null == not initialized block
            }
        }
    }

    public boolean gridPlace(Placeable p, int i, int j) {
        if (this.grid[i][j] == null) {
            p.setPosition(new Point(i, j));
            this.grid[i][j] = p;
            return true;
        } else {
            return false;
        }
    }

    private boolean[][] freeSpaces() {
        boolean[][] spaces = new boolean[this.gridSize][this.gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                spaces[i][j] = true;
            }
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
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
        if (x + width > gridSize || y - (height - 1) < 0) return false;
        boolean[][] freeSpaces = this.freeSpaces();
        for (int i = 0; i < p.getSize().height; i++) {
            for (int j = 0; j < p.getSize().width; j++) {
                //if (grid[x + j][y - i] != null) {
                if (!freeSpaces[y - i][x + j]) {
                    System.out.println("cant place 2");
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
                this.grid[position.x + j][position.y - i] = new PlaceableTemp(p);
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, 5);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, 5);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeStadium(Point position) {
        grid[position.x][position.y] = null;
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        int maintenanceCost = new Stadium(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Stadium fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, -5);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, -5);
                            }
                        }
                    }
                }
            }
        }
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, 5);
                            }
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removePolice(Point position) {
        grid[position.x][position.y] = null;
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        int maintenanceCost = new Police(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Rendőrség fenntartási díj");

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, -5);
                            }
                        } else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace) grid[i][j]).getPeople()) {
                                //calculateMood(p);
                                boostMood(p, -5);
                            }
                        }
                    }
                }
            }
        }
    }

    //todo : place/remove road, forest, service, residential, school, university and finish industrial

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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        for (Person p : ((Residential) grid[i][j]).getPeople()) {
                            //calculateMood(p);
                            boostMood(p, -5);
                        }
                    }
                }
            }
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeIndustrial(Point position) {
        ((Industrial) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        int r = 5;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        for (Person p : ((Residential) grid[i][j]).getPeople()) {
                            //calculateMood(p);
                            boostMood(p, 5);
                        }
                    }
                }
            }
        }
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
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
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
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
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
        ((Service) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
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
        grid[position.x][position.y] = null;
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
        ((School) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;

        int maintenanceCost = new School(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Iskola fenntartási díj");
    }

    public void placeForest(Point position) {
        Forest pl = new Forest(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        //finance
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    public void removeForest(Point position) {
        grid[position.x][position.y] = null;
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
        ((University) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
    }

    private Boolean searchForStadium(Person person) {
        //Searching around home first
        Residential home = person.getHome();
        Point homePosition = home.getPosition();
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();

        for (int i = homePosition.x - r; i <= homePosition.x + r; ++i) {
            for (int j = homePosition.y - r; j <= homePosition.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.POLICE) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private int getWorkplaceDistance(Person person, String type) {
        if(type.equals("uni") || type.equals("secondary")) type = "school";
        Residential home = person.getHome();
        Point position = home.getPosition();

        Placeable workplace = null;
        if (type.equals("workplace")) workplace = person.getWorkplace();
        if (type.equals("school")) workplace = person.getEducation();
        if (workplace == null) return 0;
        Point workplacePosition = workplace.getPosition();

        Queue<NodeCount> queue = new LinkedList<>();
        Set<Point> visited = new HashSet<>();

        if (position.x + 1 < gridSize && grid[position.x + 1][position.y] != null) {
            if (grid[position.x + 1][position.y].getType() == FieldType.ROAD) {
                queue.add(new NodeCount(new Point(position.x + 1, position.y), 1));
                visited.add(new Point(position.x + 1, position.y));
            }
            if (grid[position.x + 1][position.y].getPosition().equals(workplacePosition)) {
                return 1;
            }
        }
        if (position.y + 1 < gridSize && grid[position.x][position.y + 1] != null) {
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

            if (!visited.contains(new Point(position.x + 1, position.y)) && position.x + 1 < gridSize && grid[position.x + 1][position.y] != null) {
                if (grid[position.x + 1][position.y].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x + 1, position.y), nc.count + 1));
                    visited.add(new Point(position.x + 1, position.y));
                } else if (grid[position.x + 1][position.y].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x, position.y + 1)) && position.y + 1 < gridSize && grid[position.x][position.y + 1] != null) {
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
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
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
        if (getWorkplaceDistance(person, type) < 6) boostMood(person, 5);
        else if (getWorkplaceDistance(person, type) < 12) boostMood(person, 3);
        else boostMood(person, 1);
    }

    private boolean searchForJob(Person person, String type) {
        Residential home = person.getHome();
        Point position = home.getPosition();
        int x = position.x;
        int y = position.y;

        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                if (!(x == i && y == j) && grid[i][j] != null) {
                    Placeable current = grid[i][j];
                    if (current instanceof PlaceableTemp) {
                        current = ((PlaceableTemp)current).getPlaceable();
                    }
                    //GO TO WORK
                    if (type.equals("workplace")) {
                        if (current.getType() == FieldType.ZONE_INDUSTRIAL) {
                            //INDUSTRIAL
                            if (((Industrial) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), (Industrial) current, null)) && !((Industrial) current).getPeople().contains(person)) {
//                            if (((Industrial) current).areSpacesLeft() && !((Industrial) current).getPeople().contains(person)) {
                                System.out.println("In Industrial");
                                person.goToWork(((Industrial) current));
//                                ((Industrial) grid[i][j]).addPerson(person);
                                boostPersonMoodBasedOnDistance(person, type);
                                return true;
                            }
                        } else if (current.getType() == FieldType.ZONE_SERVICE) {
                            //SERVICE
                            if (((Service) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), (Service) current, null)) && !((Service) current).getPeople().contains(person)) {
//                            if (((Service) current).areSpacesLeft() && !((Service) current).getPeople().contains(person)) {
                                System.out.println("In Service");
                                person.goToWork(((Service) current));
//                                ((Service) grid[i][j]).addPerson(person);
                                boostPersonMoodBasedOnDistance(person, type);
                                return true;
                            }
                        }
                    }
                    //GO TO SCHOOL
                    else if (type.equals("secondary")) {
                        if (current.getType() == FieldType.SCHOOL) {
                            //HIGH SCHOOL
                            if (((School) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), (School) current, null)) && !((School) current).getPeople().contains(person)) {
//                            if (((School) current).areSpacesLeft() && !((School) current).getPeople().contains(person)) {
                                System.out.println("In School");
                                person.goToSchool(((School) current));
//                                ((School) grid[i][j]).addPerson(person);
                                boostPersonMoodBasedOnDistance(person, type);
                                return true;
                            }
                        }
                    }
                    else if (type.equals("uni")) {
                         if (current.getType() == FieldType.UNIVERSITY) {
                            //UNIVERSITY
                            if (((University) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), (University) current, null)) && !((University) current).getPeople().contains(person)) {
//                            if (((University) current).areSpacesLeft() && !((University) current).getPeople().contains(person)) {
                                System.out.println("In Uni");
                                person.goToSchool(((University) current));
//                                ((University) grid[i][j]).addPerson(person);
                                boostPersonMoodBasedOnDistance(person, type);
                                return true;
                            }
                        }
                    }
                }
            }
        }

        return false;
    }

    private void calculateMood(Person person) {
        int count = countStadium(person.getHome().getPosition());
        person.setBoostMood(count * 5);
        count = countPolice(person.getHome().getPosition());
        person.setBoostMood(count * 5);
        count = countIndustrial(person.getHome().getPosition());
        person.setBoostMood(-count * 5);
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();

        //todo : searchForForest && boost mood based on tax
    }

    private boolean canRoadBeDestroyed(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        boolean directPath = isPath(convertToNumMatrix(startPoint, endPoint, null));
        boolean moreThanOnePath = isPath(convertToNumMatrix(startPoint, endPoint, toBeDestroyed));
        return directPath && (moreThanOnePath);
    }

    private int[][] convertToNumMatrix(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
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

    private boolean isPath(int[][] matrix) {
        int n = gridSize;
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
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
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
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j] instanceof Residential) {
//                    System.out.println("MOOD OF ZONE: " + ((Residential) this.grid[i][j]).calculateZoneMood());
//                    System.out.println("NUM OF ZONE: " + (numOfZones + 1));
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
        if (this.finance.getCurrentWealth() < -2500) {
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
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }

    private boolean isMoodGoodEnough() {
        return this.cityMood >= 25;
    }

    private void welcomeNewInhabitants() {
        int freeSpace = 0;
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j] instanceof Residential && isNextToARoad(new Point(i, j))) {
                    freeSpace += ((Residential) this.grid[i][j]).numOfSpacesLeft();
                }
            }
        }
        double incomingNewPeople = Math.ceil(freeSpace * (cityMood / 100.0));
//        System.out.println(incomingNewPeople);
        for (int i = 0; i < (int) incomingNewPeople; i++) {
            Person tmp = new Person(findHome());
            calculateMood(tmp);
            this.people.add(tmp);
            for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
            for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
        }
//        System.out.println(this.people.size());
    }

    private void departInhabitants() {
        double outgoingPeople = Math.ceil(this.people.size() * ((100 - cityMood - 30) / 100.0));
//        System.out.println("OUTGOING PEOPLE " + outgoingPeople);
//        System.out.println("BEFORE REMOVAL " + this.people.size());
//        System.out.println("LAST INDEX: " + (this.people.size() - 1 - (int) outgoingPeople));
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
            if (lp.getEducation() != null) lp.getEducation().removePerson(lp);
            this.people.remove(lp);
        }
//        System.out.println("AFTER REMOVAL: " + this.people.size());
    }

    private Residential findHome() {
        for (int i = 0; i < this.gridSize; i++) {
            for (int j = 0; j < this.gridSize; j++) {
                if (this.grid[i][j] instanceof Residential && ((Residential) this.grid[i][j]).areSpacesLeft() && isNextToARoad(new Point(i, j))) {
                    return ((Residential) this.grid[i][j]);
                }
            }
        }
        return null;
    }

    private void findOccupation() {
        printCurrentEmployment();
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
            }

        }
        printCurrentEmployment();
    }

    private void printCurrentEmployment() {
        for(Person p : this.people) {
            System.out.println("M: " + p.getMood() + " | E: " + p.getEducation() + " | W: " + p.getWorkplace());
        }
        System.out.println("-------------------------");
    }


    private boolean isNextToARoad(Point point) {
        int x = point.x;
        int y = point.y;

        if (x - 1 >= 0 && this.grid[x - 1][y] instanceof Road) return true;
        if (x + 1 < this.gridSize && this.grid[x + 1][y] instanceof Road) return true;
        if (y - 1 >= 0 && this.grid[x][y - 1] instanceof Road) return true;
        if (y + 1 < this.gridSize && this.grid[x][y + 1] instanceof Road) return true;
        return false;
    }

    @Override
    public void timeTick() {
        if (this.inGameTime.getInGameHour() > 0) {
            //System.out.println("City mood: " + this.cityMood);
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
        // System.out.println("******************");
        if (this.inGameTime.getInGameDay() > 0 && this.inGameTime.getInGameDay() % 20 == 0 && this.inGameTime.getInGameHour() == 0) {
            if (isMoodGoodEnough()) {
                welcomeNewInhabitants();
                findOccupation();
//                System.out.println("ADD");
            } else {
                departInhabitants();
//                System.out.println("REMOVE");
            }
        }
        if (this.inGameTime.getInGameYear() > 0 && this.inGameTime.getInGameDay() == 0 && this.inGameTime.getInGameHour() == 0) {
            //triggers new year tax collection
            //and city mood change
            changeMoodOfPeople();
        }
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }
}
