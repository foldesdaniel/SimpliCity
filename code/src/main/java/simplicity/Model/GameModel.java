package simplicity.Model;

import lombok.Getter;
import lombok.Setter;
import simplicity.Model.Algorithm.NodeCount;
import simplicity.Model.Algorithm.OpenSimplex2S;
import simplicity.Model.Education.EducationLevel;
import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Finances.Finance;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.*;
import simplicity.Model.Persistence.Persistence;
import simplicity.Model.Persistence.SaveEntry;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;
import simplicity.Model.Resource.Animation;
import simplicity.Model.Resource.ResourceLoader;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.util.Queue;
import java.util.Timer;
import java.util.*;

/**
 * The game logic
 */
public class GameModel implements InGameTimeTickListener, Serializable {

    public static final String GAME_TITLE = "SimpliCity";
    public static final Image LOGO_IMG = ResourceLoader.loadImage("logo.png");
    public static final Image LOGO_SMALL_IMG = ResourceLoader.loadImage("logo_small.png");
    public static final Image BACKGROUND_IMG = ResourceLoader.loadImage("bg.jpg");
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
    public static final Image FOREST_ALL = ResourceLoader.loadImage("forest_all.png");
    public static final Image FOREST_NONE = ResourceLoader.loadImage("forest_none.png");
    public static final Image FOREST_HORIZONTAL = ResourceLoader.loadImage("forest_horizontal.png");
    public static final Image FOREST_VERTICAL = ResourceLoader.loadImage("forest_vertical.png");
    public static final Image FOREST_LEFT = ResourceLoader.loadImage("forest_left.png");
    public static final Image FOREST_RIGHT = ResourceLoader.loadImage("forest_right.png");
    public static final Image FOREST_DOWN = ResourceLoader.loadImage("forest_down.png");
    public static final Image FOREST_UP = ResourceLoader.loadImage("forest_up.png");
    public static final Image FOREST_DOWN_TO_LEFT = ResourceLoader.loadImage("forest_dtol.png");
    public static final Image FOREST_DOWN_TO_RIGHT = ResourceLoader.loadImage("forest_dtor.png");
    public static final Image FOREST_UP_TO_LEFT = ResourceLoader.loadImage("forest_utol.png");
    public static final Image FOREST_UP_TO_RIGHT = ResourceLoader.loadImage("forest_utor.png");
    public static final Image FOREST_LEFT_T = ResourceLoader.loadImage("forest_left_t.png");
    public static final Image FOREST_RIGHT_T = ResourceLoader.loadImage("forest_right_t.png");
    public static final Image FOREST_DOWN_T = ResourceLoader.loadImage("forest_down_t.png");
    public static final Image FOREST_UP_T = ResourceLoader.loadImage("forest_up_t.png");
    public static final Image FOREST_OVERLAY = ResourceLoader.loadImage("forest_overlay.png");
    public static final Image ZONE_RESIDENTIAL_IMG = ResourceLoader.loadImage("zone_residential.png");
    public static final Image ZONE_RESIDENTIAL_2_IMG = ResourceLoader.loadImage("zone_residential_2.png");
    public static final Image ZONE_WORK_SERVICE_IMG = ResourceLoader.loadImage("zone_work_service.png");
    public static final Image ZONE_WORK_INDUSTRIAL_IMG = ResourceLoader.loadImage("zone_work_industrial.png");
    public static final Image STADIUM_IMG = ResourceLoader.loadImage("stadium.png");
    public static final Image POLICE_IMG = ResourceLoader.loadImage("police.png");
    public static final Image EDUCATION_SCHOOL_IMG = ResourceLoader.loadImage("edu_school.png");
    public static final Image EDUCATION_UNIVERSITY_IMG = ResourceLoader.loadImage("edu_uni.png");
    public static final Image FIRE_ANIM_1 = ResourceLoader.loadImage("fire_anim_1.png");
    public static final Image FIRE_ANIM_2 = ResourceLoader.loadImage("fire_anim_2.png");
    public static final Image FIRE_ANIM_3 = ResourceLoader.loadImage("fire_anim_3.png");
    public static final Image FIRE_ANIM_4 = ResourceLoader.loadImage("fire_anim_4.png");
    public static final Image FIRE_ANIM_5 = ResourceLoader.loadImage("fire_anim_5.png");
    public static final Image FIRE_ANIM_6 = ResourceLoader.loadImage("fire_anim_6.png");
    public static final Image FIRE_ANIM_7 = ResourceLoader.loadImage("fire_anim_7.png");
    public static final Image FIRE_ANIM_8 = ResourceLoader.loadImage("fire_anim_8.png");
    public static final Font CUSTOM_FONT = ResourceLoader.loadFont("vt323.ttf");

    public static final Color BG_DARK = new Color(61, 63, 65); // default flatlaf dark
    public static final Point NO_SELECTION = new Point(-1, -1);
    public static final int DRAG_THRESHOLD = 5;
    public static final int GRID_SIZE = 20;
    private static final ArrayList<StartStopGameListener> stopGameListeners = new ArrayList<>();
    private static final ArrayList<ForestListener> forestListeners = new ArrayList<>();
    private static GameModel instance;

    @Getter
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    private final ArrayList<MoralChangeListener> moralListeners = new ArrayList<>();
    private final ArrayList<PeopleChangeListener> peopleChangeListeners = new ArrayList<>();
    private final ArrayList<WealthChangeListener> wealthListeners = new ArrayList<>();
    @Getter
    private final ArrayList<Animation> animations = new ArrayList<>();
    @Getter
    private final Finance finance;
    @Getter
    private final ArrayList<Person> people = new ArrayList<>();
    @Getter
    @Setter
    private String cityName = "";
    private Date nextDisaster;
    @Getter
    private int cityMood = 60;
    @Getter
    private Placeable[][] grid;
    private boolean isGameOver = false;

    public GameModel() {
        this.finance = new Finance(100000);
        generateNextDisasterDate();
        // this.initGrid();
        this.fillForest(-0.125);
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.NORMAL);
    }

    /**
     * used to get the instance of the singleton GameModel class
     *
     * @return GameModel instance
     */
    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    /**
     * used to set the instance of the GameModel singleton class after a save
     *
     * @param inst GameModel instance received from loading a save
     */
    public static void loadInstance(GameModel inst) {
        instance = inst;
    }

    /**
     * used to reset the instance of the GameModel singleton class
     */
    public static GameModel reset() {
        instance = null;
        InGameTimeManager.getInstance().setInGameTime(new InGameTime());
        return getInstance();
    }

    /**
     * Displays a confirm dialog
     *
     * @param title   popup title
     * @param message popup message
     * @return the chosen option
     */
    public static int showDialog(String title, String message) {
        return JOptionPane.showConfirmDialog(null, message, title + " | SimpliCity", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Displays a simple popup
     *
     * @param title   popup title
     * @param message popup message
     */
    public static void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title + " | SimpliCity", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Displays an error popup
     *
     * @param title   popup title
     * @param message popup message
     */
    public static void showError(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title + " | SimpliCity", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Displays a popup that tells you that you can't manage a city very well
     */
    public static void showGameOverDialog() {
        JOptionPane.showMessageDialog(null, "You have been fired as the mayor! Click OK to return to menu", "Uh oh! | SimpliCity", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * @param i      index
     * @param j      index
     * @param matrix number matrix made from the grid
     * @return if i and j are within bounds
     */
    public static boolean isSafe(int i, int j, int[][] matrix) {
        return i >= 0 && i < matrix.length && j >= 0 && j < matrix[0].length;
    }

    /**
     * used to calculate paths in the grid along with other functions
     *
     * @param matrix  number matrix made from the grid
     * @param i       x coordinate
     * @param j       y coordinate
     * @param visited number matrix with the already visited area
     * @return false if isSafe is false otherwise if there is a path in another direction then true
     */
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
            return right;
        }
        return false;
    }

    /**
     * used to load a previous save
     *
     * @param filename name of the file to be loaded
     */
    public static void loadGame(String filename) {
        try {
            loadInstance((GameModel) Persistence.load(filename));
            instance.getInGameTime().startInGameTime(InGameSpeeds.NORMAL);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void addStopGameListener(StartStopGameListener l) {
        stopGameListeners.add(l);
    }

    public static void addForestListener(ForestListener l) {
        forestListeners.add(l);
    }

    /**
     * used to generate a random date when the next disaster will occur
     */
    private void generateNextDisasterDate() {
        Random rand = new Random();
        int year = this.inGameTime.getInGameYear() + rand.nextInt(5) + 1;
        int day = rand.nextInt(364) + 1;
        this.nextDisaster = new Date(year, day, 0);
    }

    /**
     * counts how many stadiums are in the position range
     *
     * @param position grid position
     * @return count / 4 because it has a 2x2 size
     */
    public int countStadium(Point position) {
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

    /**
     * counts how many police stations are in the position range
     *
     * @param position grid position
     * @return count
     */
    public int countPolice(Point position) {
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

    /**
     * counts how many industrials are in the position range
     *
     * @param position grid position
     * @return count
     */
    public int countIndustrial(Point position) {
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

    /**
     * @param i x coordinate
     * @param j y coordinate
     * @return returns the Placeable in the given position in the grid
     */
    public Placeable grid(int i, int j) {
        try {
            return this.grid[i][j];
        } catch (ArrayIndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * used to initialize the grid with null values at the beginning of the game
     */
    public void initGrid() {
        this.grid = new Placeable[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                this.grid[i][j] = null; //null == not initialized block
            }
        }
    }

    /**
     * Fills the grid with Forest objects using {@link OpenSimplex2S#noise2}
     *
     * @param threshold threshold
     */
    public void fillForest(double threshold) {
        this.grid = new Placeable[GRID_SIZE][GRID_SIZE];
        int seed = (int) (Math.random() * 10000 + 1);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                float cell = OpenSimplex2S.noise2(seed, j, i);
                this.grid[i][j] = (cell < threshold) ? new Forest(new Point(i, j)) : null;
            }
        }
    }

    /**
     * Creates a boolean matrix representing the available free
     * spaces on the grid (taking Placeable sizes into account)
     *
     * @return matrix
     */
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

    /**
     * Calculates if the given Placeable can be placed at the given position
     *
     * @param p        the Placeable
     * @param position position on grid
     * @return if there's enough space for the Placeable
     */
    public boolean canPlace(Placeable p, Point position) {
        int x = position.x;
        int y = position.y;
        int width = p.getSize().width;
        int height = p.getSize().height;
        if (x + width > GRID_SIZE || y - (height - 1) < 0) return false;
        boolean[][] freeSpaces = this.freeSpaces();
        for (int i = 0; i < p.getSize().height; i++) {
            for (int j = 0; j < p.getSize().width; j++) {
                if (!freeSpaces[y - i][x + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Fills the grid with PlaceableTemps based on the
     * given position and the size of the Placeable
     *
     * @param p        Placeable
     * @param position position on grid
     */
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

    /**
     * Removes the PlaceableTemps from the grid based on
     * the given position and the size of the Placeable
     *
     * @param p        Placeable
     * @param position position on grid
     */
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

    /**
     * used to place a Stadium on the grid
     *
     * @param position the position where the Stadium should be placed
     */
    public void placeStadium(Point position) {
        Stadium pl = new Stadium(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);
        int r = new Stadium(GameModel.NO_SELECTION).getRadius();
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

    /**
     * used to remove an already built Stadium
     *
     * @param position the position where the Stadium is
     */
    public void removeStadium(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        int price = grid[position.x][position.y].getBuildPrice() / 3;
        this.finance.addIncome(price, "Stadium törlés");
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

    /**
     * used to place a Police station on the grid
     *
     * @param position the position where the Police Station should be placed
     */
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

    /**
     * used to remove an already built Police Station
     *
     * @param position the position where the Police Station is
     */
    public void removePolice(Point position) {
        int price = grid[position.x][position.y].getBuildPrice() / 3;
        this.finance.addIncome(price, "Rendőrség törlés");
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

    /**
     * used to place an Industrial zone on the grid
     *
     * @param position the position where the Industrial Zone should be placed
     */
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
                        } else {
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

    /**
     * checks if there is a forest between a residential and industrial zone
     *
     * @param industrial  position of the zone
     * @param residential position of the zone
     * @return return true if found
     */
    public boolean isForestBetweenResidential_Industrial(Point industrial, Point residential) {
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
            } else { // F --- R
                for (int i = fy + 1; i <= ry - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Forest)) return true;
                    }
                }
            }
        } else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Forest)) return true;
                    }
                }
            } else {
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

    /**
     * removes industrial zone if there are no workers
     *
     * @param position    position of the zone
     * @param forceRemove if it is true then it will be deleted no matter what
     */
    public void removeIndustrial(Point position, boolean forceRemove) {
        //check if it can be removed

        Industrial industrial = ((Industrial) grid[position.x][position.y]);
        if (((Industrial) grid[position.x][position.y]).getPeople().size() > 0 && !forceRemove) {
            int buildPrice = industrial.getBuildPrice();
            int choice = JOptionPane.showOptionDialog(null, "Do you want to demolish this Industrial Zone?\nCost " + buildPrice, "Demolition confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) return;
        }

        int price = grid[position.x][position.y].getBuildPrice() / 3;
        this.finance.addIncome(price, "Ipari zóna törlés");
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
                        } else {
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

    /**
     * used to place a Road zone on the grid
     *
     * @param position the position where the Road should be placed
     */
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
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to remove a road
     * if the road connects people to their Workplace or place of education then the player has to agree to the demolition
     * if the player agrees to the demolition, some inhabitants will move places or go work/study somewhere else
     * it will also cost more for the player
     * if the player doesn't agree, nothing happens
     *
     * @param position the road to be removed
     * @return true if the road was removed, false if it wasn't
     */
    public Boolean removeRoad(Point position) {
        Placeable toBeDestroyed = grid[position.x][position.y];
        boolean canBeDestroyed = true;
        OUTER:
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                    for (Person p : ((Residential) grid[i][j]).getPeople()) {
                        if (p.getWorkplace() != null) {
                            if (!canRoadBeDestroyed(grid[i][j], grid[p.getWorkplace().getPosition().x][p.getWorkplace().getPosition().y], toBeDestroyed)) {
                                canBeDestroyed = false;
                                break OUTER;
                            }
                        } else if (p.getEducation() != null) {
                            if (!canRoadBeDestroyed(grid[i][j], grid[p.getEducation().getPosition().x][p.getEducation().getPosition().y], toBeDestroyed)) {
                                canBeDestroyed = false;
                                break OUTER;
                            }
                        }
                    }
                }
            }
        }
        if (canBeDestroyed) {
            grid[position.x][position.y] = null;
            int maintenanceCost = new Road(GameModel.NO_SELECTION).getMaintenanceCost();
            finance.removeYearlySpend(maintenanceCost, "Út fenntartási díj");
            for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
            for (PeopleChangeListener l : this.peopleChangeListeners) l.onPeopleCountChange();
            return true;
        }
        //konfliktusos bontas
        else {
            int choice = JOptionPane.showOptionDialog(null, "Do you want to demolish this road?\nCost: " + toBeDestroyed.getBuildPrice(), "Demolition confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) return false;
            else {
                this.finance.removeMoney(toBeDestroyed.getBuildPrice());
                for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
                ArrayList<Person> affectedPeople = new ArrayList<>();
                for (Person person : this.people) {
                    if (person.getEducation() != null && !canRoadBeDestroyed(person.getHome(), person.getEducation(), toBeDestroyed)) {
                        affectedPeople.add(person);
                    }
                    if (person.getWorkplace() != null && !canRoadBeDestroyed(person.getHome(), person.getWorkplace(), toBeDestroyed)) {
                        affectedPeople.add(person);
                    }
                }
                grid[position.x][position.y] = null;
                for (int i = 0; i < affectedPeople.size(); i++) {
                    Person person = affectedPeople.get(i);
                    Point homePoint = person.getHome().getPosition();

                    int x = homePoint.x;
                    int y = homePoint.y;
                    boolean emptyAround = true;
                    if (x - 1 > 0) {
                        if ((grid[x - 1][y] instanceof Road)) {
                            emptyAround = false;
                        }
                    }
                    if (y - 1 > 0) {
                        if ((grid[x][y - 1] instanceof Road)) {
                            emptyAround = false;
                        }
                    }
                    if (x + 1 < GRID_SIZE) {
                        if ((grid[x + 1][y] instanceof Road)) {
                            emptyAround = false;
                        }
                    }
                    if (y + 1 < GRID_SIZE) {
                        if ((grid[x][y + 1] instanceof Road)) {
                            emptyAround = false;
                        }
                    }
                    if (emptyAround) {
                        cleanUpAfterPerson(person);
                    }
                }
                for (Person p : affectedPeople) {
                    boostMood(p, -6);
                }
                moveAffectedPeople(affectedPeople);
                findOccupation();
            }
        }

        int maintenanceCost = new Road(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Út fenntartási díj");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
        for (PeopleChangeListener l : this.peopleChangeListeners) l.onPeopleCountChange();
        return true;
    }

    public void cleanUpAfterPerson(Person person) {
        if (person.getEducation() != null) {
            int indexOfPerson = person.getEducation().getPeople().indexOf(person);
            person.getEducation().getArrivalDates().remove(indexOfPerson);
            person.getEducation().removePerson(person);
            person.setEducation(null);
        }
        if (person.getWorkplace() != null) {
            person.getWorkplace().removePerson(person);
            person.setWorkplace(null);
        }
        person.getHome().removePerson(person);
        this.people.remove(person);
    }

    /**
     * used to find a new home or new workplace/education for people
     * or make them leave if they have no other choice
     *
     * @param people list of people who have been affected by a demolition
     */
    public void moveAffectedPeople(ArrayList<Person> people) {
        ArrayList<Person> movedPeople = new ArrayList<>();
        OUTER:
        for (int i = 0; i < people.size(); i++) {
            Person p = people.get(i);
            for (int j = 0; j < GRID_SIZE; j++) {
                for (int k = 0; k < GRID_SIZE; k++) {
                    if (grid[j][k] instanceof Residential r && ((Residential) grid[j][k]).areSpacesLeft()) {
                        if (p.getWorkplace() != null) {
                            if (isPath(convertToNumMatrix(r, p.getWorkplace(), null))) {
                                p.getHome().removePerson(p);
                                p.moveIn(r);
                                movedPeople.add(p);
                                continue OUTER;
                            }
                        }
                        if (p.getEducation() != null) {
                            if (isPath(convertToNumMatrix(r, p.getEducation(), null))) {
                                p.getHome().removePerson(p);
                                p.moveIn(r);
                                movedPeople.add(p);
                                continue OUTER;
                            }
                        }
                    }
                }
            }
        }

        people.removeAll(movedPeople);
        for (int i = 0; i < people.size(); i++) {
            Person person = people.get(i);
            cleanUpAfterPerson(person);
        }
    }

    /**
     * used to place a Service zone on the grid
     *
     * @param position the position where the Service Zone should be placed
     */
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

    /**
     * removes service zone
     *
     * @param position position of the service zone
     */
    public void removeService(Point position) {
        //check if it can be removed
        Service service = ((Service) grid[position.x][position.y]);

        if (((Service) grid[position.x][position.y]).getPeople().size() > 0) {
            int buildPrice = service.getBuildPrice();
            int choice = JOptionPane.showOptionDialog(null, "Do you want to demolish this Service Zone?\nCost " + buildPrice, "Demolition confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) return;
        }

        int price = grid[position.x][position.y].getBuildPrice() / 3;
        this.finance.addIncome(price, "Szolgálatási zóna törlés");
        this.finance.addMoney(price);

        ((Service) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to place a Residential zone on the grid
     *
     * @param position the position where the Residential Zone should be placed
     */
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

    /**
     * used to remove a Residential zone
     * if there are people living in the zone the player has to agree to the demolition
     * if the player accepts the people will move houses or leave the city depending on the vacancy
     * if the player doesn't accept, nothing happens
     *
     * @param position position of the Residential zone
     */
    public void removeResidential(Point position) {
        //check if it can be removed
        Residential r = ((Residential) grid[position.x][position.y]);
        if (r.getPeople().size() > 0) {
            int buildPrice = r.getBuildPrice();
            int choice = JOptionPane.showOptionDialog(null, "Do you want to demolish this Residential Zone?\nCost: " + buildPrice, "Demolition confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (choice == JOptionPane.NO_OPTION || choice == JOptionPane.CLOSED_OPTION) return;
            else {
                this.finance.removeMoney(buildPrice);
                this.finance.addBuilt(buildPrice, "Konfliktusos útbontás");
                for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
                for (int i = 0; i < r.getPeople().size(); i++) {
                    Person p = r.getPeople().get(i);
                    if (p.getWorkplace() != null) {
                        p.getWorkplace().removePerson(p);
                        p.setWorkplace(null);
                    }
                    if (p.getEducation() != null) {
                        int indexOfPerson = p.getEducation().getPeople().indexOf(p);
                        p.getEducation().getArrivalDates().remove(indexOfPerson);
                        p.getEducation().removePerson(p);
                        p.setEducation(null);
                    }
                    Residential home = findHome();
                    if (home != null) {
                        p.moveIn(home);
                    } else {
                        this.people.remove(p);
                    }
                }
                grid[position.x][position.y] = null;
                for (PeopleChangeListener l : this.peopleChangeListeners) l.onPeopleCountChange();
                for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
                findOccupation();
            }
            return;
        }

        int price = r.getBuildPrice() / 3;
        this.finance.addIncome(price, "Lakóhely zóna törlés");
        this.finance.addMoney(price);
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to place a School on the grid
     *
     * @param position the position where the School should be placed
     */
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

    /**
     * used to remove a School
     * all students will be kicked out upon demolition
     *
     * @param position the position where the School is
     */
    public void removeSchool(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        ((School) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        int maintenanceCost = new School(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Iskola fenntartási díj");
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to place a Forest on the grid
     *
     * @param position the position where the Forest should be placed
     */
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

        boostForestMood(position, 1);

        //finance
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * boosts people mood in the forest radius
     *
     * @param position forest zone position
     * @param boost    amount of boost
     */
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
                                } else {
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

    /**
     * checks if there is an industrial zone after forest
     *
     * @param forest      position of the forest
     * @param residential position of the residential zone
     * @return returns true if found
     */
    public boolean isIndustrialAfterForest(Point forest, Point residential) {
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
            } else { // F --- R
                for (int i = fy + 1; i <= ry + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && (grid[fx][i] instanceof Industrial)) return true;
                    }
                }
            }
        } else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx + r; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && (grid[i][ry] instanceof Industrial)) return true;
                    }
                }
            } else {
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

    /**
     * checks if forest should boost the people mood in the area
     *
     * @param forest      position of the forest
     * @param residential position of the residential zone
     * @return returns true if it should boost mood
     */
    public boolean doesForestBoostMood(Point forest, Point residential) {
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
            } else { // F --- R
                for (int i = fy + 1; i <= ry - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[fx][i] != null && !(grid[fx][i] instanceof Road)) return false;
                    }
                }
            }
        } else { //same column
            if (fx > rx) {
                // R
                // -
                // F
                for (int i = rx + 1; i <= fx - 1; ++i) {
                    if (i >= 0 && i < GRID_SIZE) {
                        if (grid[i][ry] != null && !(grid[i][ry] instanceof Road)) return false;
                    }
                }
            } else {
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

    /**
     * used to remove a Forest
     *
     * @param position the position where the Forest is
     */
    public void removeForest(Point position) {
        int i = position.x, j = position.y;
        int elapsed = this.inGameTime.getInGameYear() - ((Forest) grid[i][j]).getPlantTime().getYear();
        boostForestMood(position, -elapsed);

        int maintenanceCost = new Forest(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Erdő fenntartási díj");

        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to place a University on the grid
     *
     * @param position the position where the University should be placed
     */
    public void placeUniversity(Point position) {
        University pl = new University(position);
        if (!canPlace(pl, position)) return;
        grid[position.x][position.y] = pl;
        fillTemps(pl, position);

        int price = new University(GameModel.NO_SELECTION).getBuildPrice();
        int maintenanceCost = new University(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeMoney(price);
        finance.addBuilt(price, "Egyetem építés");
        finance.addYearlySpend(maintenanceCost, "Egyetem fenntartási díj");

        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * used to remove a University
     * all students will be kicked out upon demolition
     *
     * @param position the position where the University is
     */
    public void removeUniversity(Point position) {
        deleteTemps(grid[position.x][position.y], position);
        int maintenanceCost = new University(GameModel.NO_SELECTION).getMaintenanceCost();
        finance.removeYearlySpend(maintenanceCost, "Egyetem fenntartási díj");
        ((University) grid[position.x][position.y]).deleteData();
        grid[position.x][position.y] = null;
        for (WealthChangeListener l : this.wealthListeners) l.onWealthChange();
    }

    /**
     * calculates the shortest distance between the residential zone and the workplace
     * based on BFS algorithm
     *
     * @param person the person
     * @param type   type of the workplace
     * @return returns the distance
     */
    public int getWorkplaceDistance(Person person, String type) {
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

    /**
     * used to boost the mood of a person
     *
     * @param p     Person
     * @param boost the amount the person's mood should be boosted by
     */
    public void boostMood(Person p, int boost) {
        p.setBoostMood(p.getBoostMood() + boost);
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();

    }

    /**
     * used to boost a person's mood, depending on how far their occupation is
     *
     * @param person Person
     * @param type   the Placeable we are measuring the distance to - work, school, uni, etc.
     */
    public void boostPersonMoodBasedOnDistance(Person person, String type) {
        if (getWorkplaceDistance(person, type) < 6) boostMood(person, 4);
        else if (getWorkplaceDistance(person, type) < 12) boostMood(person, -2);
        else boostMood(person, -7);
    }

    /**
     * used to find an occupation for a person
     *
     * @param person person that we want to find an occupation for
     * @param type   the type of occupation we want to find - work/education
     */
    private void searchForJob(Person person, String type) {
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
                                    return;
                                }
                            }
                        } else if (type.equals("uni")) {
                            if (current.getType() == FieldType.UNIVERSITY) {
                                if (((University) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((University) current).getPeople().contains(person)) {
                                    person.goToSchool(((University) current));
                                    boostPersonMoodBasedOnDistance(person, type);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        } else if (type.equals("workplace")) {
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
                            if (giveServiceJob(person, type, current, temp)) return;
                        } else { //we need industrial workers
                            if (giveIndustrialJob(person, type, current, temp)) return;
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
                            if (giveIndustrialJob(person, type, current, temp)) return;
                        } else { //we need NOT industrial workers
                            if (giveServiceJob(person, type, current, temp)) return;
                        }
                    }
                }
            }
        }

    }

    private boolean giveIndustrialJob(Person person, String type, Placeable current, Placeable temp) {
        if (current.getType() == FieldType.ZONE_INDUSTRIAL) {
            if (((Industrial) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Industrial) current).getPeople().contains(person)) {
                person.goToWork(((Industrial) current));
                boostPersonMoodBasedOnDistance(person, type);
                return true;
            }
        }
        return false;
    }

    private boolean giveServiceJob(Person person, String type, Placeable current, Placeable temp) {
        if (current.getType() == FieldType.ZONE_SERVICE) {
            if (((Service) current).areSpacesLeft() && isPath(convertToNumMatrix(person.getHome(), temp, null)) && !((Service) current).getPeople().contains(person)) {
                person.goToWork(((Service) current));
                boostPersonMoodBasedOnDistance(person, type);
                return true;
            }
        }
        return false;
    }

    /**
     * @return 1 if there are more people working in Industrial zone, 0 if more in Service zone
     */
    public int workersRatio() {
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

    /**
     * calculates the starting mood of the person based on boosting factors
     *
     * @param person person
     */
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
    }

    /**
     * calculates the current forest boosting mood
     *
     * @param position forest position
     * @return the sum of the years
     */
    public int calculateForestMood(Point position) {
        int sum = 0;

        int r = 3;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < GRID_SIZE && j < GRID_SIZE) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.FOREST) {
                        int elapsed = this.inGameTime.getInGameYear() - ((Forest) grid[i][j]).getPlantTime().getYear();
                        sum += elapsed;
                    }
                }
            }
        }

        return sum;
    }

    /**
     * used to calculate if start point and end point are still connected after destroying a road
     *
     * @param startPoint    start of the path
     * @param endPoint      end of the path
     * @param toBeDestroyed the road we wish to destroy
     * @return true if the road can be destroyed, false if it can't
     */
    public boolean canRoadBeDestroyed(Placeable startPoint, Placeable endPoint, Placeable toBeDestroyed) {
        boolean directPath = isPath(convertToNumMatrix(startPoint, endPoint, null));
        boolean moreThanOnePath = isPath(convertToNumMatrix(startPoint, endPoint, toBeDestroyed));
        return directPath && (moreThanOnePath);
    }

    /**
     * used to prepare date for isPath method
     *
     * @param startPoint    start of the path
     * @param endPoint      end of the path
     * @param toBeDestroyed the road we wish to destroy
     * @return a number matrix representing the grid with numbers
     * 1 - start point
     * 2 - end point
     * 3 - road
     * 0 - everything else
     */
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
        if (endPoint instanceof School) {
            matrix[endPoint.getPosition().x + 1][endPoint.getPosition().y] = 3;
        }
        if (endPoint instanceof University) {
            matrix[endPoint.getPosition().x + 1][endPoint.getPosition().y] = 3;
            matrix[endPoint.getPosition().x + 1][endPoint.getPosition().y - 1] = 3;
            matrix[endPoint.getPosition().x][endPoint.getPosition().y - 1] = 3;
        }
        if (toBeDestroyed != null) matrix[toBeDestroyed.getPosition().x][toBeDestroyed.getPosition().y] = 0;
        return matrix;
    }

    /**
     * @param matrix number matrix provided by convertToNumMatrix
     * @return true if start point and end point are connected, otherwise false
     */
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

    /**
     * used to collect the tax every year from all the eligible Placeables in the grid
     */
    public void newYearTaxCollection() {
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

    /**
     * used to calculate the overall mood of the city
     * takes an average of the zone moods
     */
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

    private void gameOver() {
        if (!isGameOver) {
            isGameOver = true;
            showGameOverDialog();
            InGameTimeManager.getInstance().setInGameTime(new InGameTime());
            for (StartStopGameListener l : stopGameListeners) l.onGameStop();
        }
    }

    /**
     * used to change the mood of all inhabitants yearly
     * mood change depends on the current finances
     * good finances - increasingly bigger mood boost
     * bad finances - increasingly bigger mood decline
     * after 3 years in a row of bad finances the game is over
     */
    private void changeMoodOfPeople() {
        if (this.finance.getCurrentWealth() < -8000) {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() - 0.5);
        } else {
            this.finance.setProfitableYearsInARow(this.finance.getProfitableYearsInARow() + 0.5);
        }

        double multiplier;
        if (this.finance.getProfitableYearsInARow() < -1.5) {
            multiplier = 0.85;
            this.gameOver();
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
                        changeMoodBasedOnTax(size, max, p);
                    }
                } else if (this.grid[i][j] instanceof Service) {
                    int size = ((Service) this.grid[i][j]).getPeople().size();
                    int max = ((Service) this.grid[i][j]).getMaxPeople();

                    for (Person p : ((Service) this.grid[i][j]).getPeople()) {
                        changeMoodBasedOnTax(size, max, p);
                    }
                } else if (this.grid[i][j] instanceof Industrial) {
                    int size = ((Industrial) this.grid[i][j]).getPeople().size();
                    int max = ((Industrial) this.grid[i][j]).getMaxPeople();

                    for (Person p : ((Industrial) this.grid[i][j]).getPeople()) {
                        changeMoodBasedOnTax(size, max, p);
                    }
                }
            }
        }
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
    }

    public void changeMoodBasedOnTax(int size, int max, Person p) {
        if (size == max) boostMood(p, -4);
        else if (size > 2 * max / 3) boostMood(p, -2);
        else if (size > max / 2) boostMood(p, 1);
        else if (size > max / 3) boostMood(p, 2);
        else boostMood(p, 7);
    }

    /**
     * @return if the city mood if over a certain threshold
     */
    public boolean isMoodGoodEnough() {
        return this.cityMood >= 20;
    }

    /**
     * used to add new people to the city
     * the rate of arrival depends on the amount of free spaces and the city mood
     */
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

    /**
     * used to make people leave the city
     * number of people departing based on the city mood and the people who have the lowest mood
     */
    public void departInhabitants() {
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
            if (lp != null) {
                if (lp.getWorkplace() != null) lp.getWorkplace().removePerson(lp);
                if (lp.getHome() != null) lp.getHome().removePerson(lp);
                if (lp.getEducation() != null) {
                    int indexOfPerson = lp.getEducation().getPeople().indexOf(lp);
                    lp.getEducation().getArrivalDates().remove(indexOfPerson);
                    lp.getEducation().removePerson(lp);
                }
                this.people.remove(lp);
            }
        }
        for (PeopleChangeListener l : peopleChangeListeners) l.onPeopleCountChange();
    }

    /**
     * used to find a home for a person
     *
     * @return a Residential if there is a free place, null if there isn't
     */
    public Residential findHome() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (this.grid[i][j] instanceof Residential && ((Residential) this.grid[i][j]).areSpacesLeft() && isNextToARoad(new Point(i, j))) {
                    return ((Residential) this.grid[i][j]);
                }
            }
        }
        return null;
    }

    /**
     * used to find an occupation for people who don't have one currently
     */
    private void findOccupation() {

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
    }

    /**
     * used to see if there are any roads around a point
     *
     * @param point the point in the grid we are investigating
     * @return false if there are no roads, true if there is a road near
     */
    public boolean isNextToARoad(Point point) {
        int x = point.x;
        int y = point.y;

        if (x - 1 >= 0 && this.grid[x - 1][y] instanceof Road) return true;
        if (x + 1 < GRID_SIZE && this.grid[x + 1][y] instanceof Road) return true;
        if (y - 1 >= 0 && this.grid[x][y - 1] instanceof Road) return true;
        return y + 1 < GRID_SIZE && this.grid[x][y + 1] instanceof Road;
    }

    /**
     * used to calculate all the maintenance costs of the eligible buildings in the grid
     */
    public void newYearMaintenanceCost() {
        int sum = 0;
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if ((grid[i][j] != null)) {
                    sum += grid[i][j].calculateMaintenance();
                }
            }
        }
        finance.removeMoney(sum);
    }

    /**
     * rechecks every year the forest logic
     * this is needed in order to delete the yearly maintenance cost at the end
     * and boost forest mood
     */
    private void newYearForest() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j] instanceof Forest) {
                    int elapsed = this.inGameTime.getInGameYear() - ((Forest) grid[i][j]).getPlantTime().getYear();
                    ((Forest) grid[i][j]).setAge(elapsed);
                    for (ForestListener l : forestListeners) l.onForestAgeUp();
                    if (elapsed <= 10) boostForestMood(grid[i][j].getPosition(), 1);
                    if (elapsed == 10)
                        finance.removeYearlySpend(((Forest) grid[i][j]).getMaintenanceCost(), "Erdő fenntartási díj");
                    else {
                        int maintenanceCost = ((Forest) grid[i][j]).getMaintenanceCost();
                        finance.removeYearlySpend(maintenanceCost, "Erdő fenntartási díj");
                        ((Forest) grid[i][j]).setMaintenanceCost(0);
                    }
                }
            }
        }
    }

    /**
     * checks if there is at least one industrial zone already
     *
     * @return true if there is
     */
    public boolean isIndustrialBuiltAlready() {
        for (int i = 0; i < GRID_SIZE; ++i) {
            for (int j = 0; j < GRID_SIZE; ++j) {
                if (grid[i][j] != null && grid[i][j] instanceof Industrial) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * searches one random industrial zone
     *
     * @return the point of the random zone
     */
    public Point searchRandomIndustrial() {
        while (true) {
            Random rand = new Random();
            int i = rand.nextInt(GRID_SIZE);
            int j = rand.nextInt(GRID_SIZE);
            if (grid[i][j] != null && grid[i][j] instanceof Industrial) return grid[i][j].getPosition();
        }
    }

    /**
     * industrial disaster logic
     * (animation, delete industrial zone)
     *
     * @param position position of the zone
     */
    public void doIndustrialDisaster(Point position) {
        playAnim(Animation.createFireAnim(position), 3200);
        removeIndustrial(position, true);
    }

    /**
     * Plays the given Animation
     *
     * @param anim Animation to play
     */
    public void playAnim(Animation anim) {
        addAnimation(anim);
    }

    /**
     * Plays the given Animation for a duration
     *
     * @param duration duration in milliseconds
     * @param anim     Animation to play
     */
    public void playAnim(Animation anim, int duration) {
        playAnim(anim);
        Timer animTimer = new Timer();
        TimerTask animTask = new TimerTask() {
            @Override
            public void run() {
                stopAnimation(anim);
            }
        };
        animTimer.schedule(animTask, duration);
    }

    /**
     * Adds an Animation to the animation list
     *
     * @param anim animation to add
     */
    private void addAnimation(Animation anim) {
        animations.add(anim);
        anim.start();
    }

    /**
     * Stops playing an Animation
     *
     * @param anim animation to stop
     */
    public void stopAnimation(Animation anim) {
        animations.remove(anim);
        anim.stop();
    }

    /**
     * used to remove people from the city whose mood has reached zero
     */
    public void removeDepressedPeople() {
        ArrayList<Person> toBeRemoved = new ArrayList<>();
        for (int i = 0; i < this.people.size(); i++) {
            Person p = this.people.get(i);
            if (p.getMood() == 0) {
                if (p.getEducation() != null) {
                    int indexOfPerson = p.getEducation().getPeople().indexOf(p);
                    p.getEducation().getArrivalDates().remove(indexOfPerson);
                    p.getEducation().removePerson(p);
                }
                if (p.getWorkplace() != null) {
                    p.getWorkplace().removePerson(p);
                }
                p.getHome().removePerson(p);
                toBeRemoved.add(p);
            }
        }
        if (toBeRemoved.size() > 0) this.people.removeAll(toBeRemoved);
        for (int i = 0; i < peopleChangeListeners.size(); i++) {
            peopleChangeListeners.get(i).onPeopleCountChange();
        }

    }

    /**
     * used the save the current GameModel object to a file
     */
    public void saveGame() {
        try {
            SaveEntry.createOrUpdateEntry(getCityName(), this);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * used to drive the logic (functions) based on the passing of in-game time
     */
    @Override
    public void timeTick() {
        if (isGameOver) return;
        if (this.inGameTime.getInGameHour() > 0) {
            calculateCityMood();
            removeDepressedPeople();
        }
        if (this.inGameTime.getInGameDay() > 0 && this.inGameTime.getInGameHour() == 0) {
            if (isMoodGoodEnough()) {
                welcomeNewInhabitants();
                findOccupation();
            } else {
                departInhabitants();
            }
        }
        if (this.inGameTime.getInGameYear() > 0 && this.inGameTime.getInGameDay() == 0 && this.inGameTime.getInGameHour() == 0) {
            changeMoodOfPeople();
            if (isGameOver) return;
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

    /**
     * function that is automatically run when a GameModel object is deserialized
     * used to set certain properties to ensure a game load is functional and accurate
     *
     * @param in GameModel object
     */
    @Serial
    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        calculateCityMood();
        for (MoralChangeListener l : this.moralListeners) l.onMoralChanged();
        for (PeopleChangeListener l : this.peopleChangeListeners) l.onPeopleCountChange();

        InGameTimeManager.getInstance().setInGameTime(this.inGameTime);
        InGameTimeManager.getInstance().getInGameTime().inGameElapsedTime = new Timer();
    }

}
