package simplicity.Model;

import simplicity.Model.Algorithm.NodeCount;
import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Finances.Finance;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Person.Person;
import simplicity.Model.Placeables.*;
import simplicity.Model.Resource.ResourceLoader;
import simplicity.Model.Zones.Industrial;
import simplicity.Model.Zones.Residential;
import simplicity.Model.Zones.Service;

import java.awt.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

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
    private final int gridSize = 20;
    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    private int cityMood;
    private Placeable grid[][];
    private Finance finance;

    private int industrialCount = 0;

    private int serviceCount = 0;

    public GameModel() {
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
        this.finance = new Finance(10000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;

        //Initialize grid
        this.grid = new Placeable[this.gridSize][this.gridSize];
        for (int i = 0; i < this.gridSize; ++i) {
            for (int j = 0; j < this.gridSize; ++j) {
                this.grid[i][j] = null; //null == not initialized block
            }
        }

        //TESTING feature/13_mood_with_radius
        System.out.println("******************");
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("******************");

//        grid[0][1] = new Residential(new Point(0,1));
//        grid[0][2] = new Road(new Point(0,2));
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
//        System.out.println(getWorkplaceDistance(((Residential)grid[0][1]).getPeople().get(0)));


    }

    private void placeStadium(Point position) {
        grid[position.x][position.y] = new Stadium(position);
        int r = new Stadium(new Point(-1, -1)).getRadius();
        finance.removeMoney(new Stadium(new Point(-1, -1)).getBuildPrice());

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                        else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeStadium(Point position) {
        grid[position.x][position.y] = null;
        int r = new Stadium(new Point(-1, -1)).getRadius();

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                        else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placePolice(Point position) {
        grid[position.x][position.y] = new Police(position);
        int r = new Police(new Point(-1, -1)).getRadius();
        finance.removeMoney(new Police(new Point(-1, -1)).getBuildPrice());

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void removePolice(Point position) {
        grid[position.x][position.y] = null;
        int r = new Stadium(new Point(-1, -1)).getRadius();

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null) {
                        if (grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                            for (Person p : ((Residential)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                        else if (grid[i][j].getType() == FieldType.ZONE_INDUSTRIAL || grid[i][j].getType() == FieldType.ZONE_SERVICE) {
                            for (Person p : ((Workplace)grid[i][j]).getPeople()) {
                                calculateMood(p);
                            }
                        }
                    }
                }
            }
        }
    }

    private void placeIndustrial(Point position) {
        grid[position.x][position.y] = new Industrial(position);
        int r = 5;
        finance.removeMoney(new Industrial(new Point(-1, -1)).getBuildPrice());

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        for (Person p : ((Residential)grid[i][j]).getPeople()) {
                            calculateMood(p);
                        }
                    }
                }
            }
        }
    }

    private void removeIndustrial(Point position) {
        grid[position.x][position.y] = null;
        int r = 5;

        for (int i = position.x - r; i <= position.x + r; ++i) {
            for (int j = position.y - r; j <= position.y + r; ++j) {
                if (i >= 0 && j >= 0 && i < gridSize && j < gridSize) {
                    if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                        for (Person p : ((Residential)grid[i][j]).getPeople()) {
                            calculateMood(p);
                        }
                    }
                }
            }
        }
    }

    private void placeRoad(Point position) {
        grid[position.x][position.y] = new Road(position);
        finance.removeMoney(new Road(new Point(-1, -1)).getBuildPrice());

        //recalculating mood for every person
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                    for (Person p : ((Residential)grid[i][j]).getPeople()) {
                        calculateMood(p);
                    }
                }
            }
        }
    }

    private Boolean removeRoad(Point position) {
        grid[position.x][position.y] = null;

        //todo : cannot be deleted

        return true;
    }

    private void placeService(Point position) {
        grid[position.x][position.y] = new Service(position);
        finance.removeMoney(new Service(new Point(-1, -1)).getBuildPrice());
    }

    private void removeService(Point position) {
        grid[position.x][position.y] = null;
    }

    private void placeResidential(Point position) {
        grid[position.x][position.y] = new Residential(position);
        finance.removeMoney(new Residential(new Point(-1, -1)).getBuildPrice());
    }

    private void removeResidential(Point position) {
        grid[position.x][position.y] = null;
    }

    private void placeSchool(Point position) {
        grid[position.x][position.y] = new School(position);
        finance.removeMoney(new School(new Point(-1, -1)).getBuildPrice());
    }

    private void removeSchool(Point position) {
        grid[position.x][position.y] = null;
    }

    private void placeForest(Point position) {
        //grid[position.x][position.y] = new Forest(position);
        //finance
    }

    private void removeForest(Point position) {
        grid[position.x][position.y] = null;
    }

    private void placeUniversity(Point position) {
        grid[position.x][position.y] = new University(position);
        finance.removeMoney(new University(new Point(-1, -1)).getBuildPrice());
    }

    private void removeUniversity(Point position) {
        grid[position.x][position.y] = null;
    }

    private Boolean searchForStadium(Person person) {
        //Searching around home first
        Residential home = person.getHome();
        Point homePosition = home.getPosition();
        int r = new Stadium(new Point(-1, -1)).getRadius();

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
        int r = new Police(new Point(-1, -1)).getRadius();

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

    private int getWorkplaceDistance(Person person) {
        Residential home = person.getHome();
        Point position = home.getPosition();

        Workplace workplace = person.getWorkplace();
        if (workplace == null) return -1;
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

        while(!queue.isEmpty()) {
            NodeCount nc = queue.remove();
            position = nc.position;
            System.out.println("Position : " + position.x + " " + position.y);

            if (!visited.contains(new Point(position.x + 1, position.y)) && position.x + 1 < gridSize && grid[position.x + 1][position.y] != null) {
                if (grid[position.x + 1][position.y].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x + 1, position.y), nc.count + 1));
                    visited.add(new Point(position.x + 1, position.y));
                }
                else if (grid[position.x + 1][position.y].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x, position.y + 1)) && position.y + 1 < gridSize && grid[position.x][position.y + 1] != null) {
                if (grid[position.x][position.y + 1].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x, position.y + 1), nc.count + 1));
                    visited.add(new Point(position.x, position.y + 1));
                }
                else if (grid[position.x][position.y + 1].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x, position.y - 1)) && position.y - 1 >= 0 && grid[position.x][position.y - 1] != null) {
                if (grid[position.x][position.y - 1].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x, position.y - 1), nc.count + 1));
                    visited.add(new Point(position.x, position.y - 1));
                }
                else if (grid[position.x][position.y - 1].getPosition().equals(workplacePosition)) {
                    return nc.count + 1;
                }
            }
            if (!visited.contains(new Point(position.x - 1, position.y)) && position.x - 1 >= 0 && grid[position.x - 1][position.y] != null) {
                if (grid[position.x - 1][position.y].getType() == FieldType.ROAD) {
                    queue.add(new NodeCount(new Point(position.x - 1, position.y), nc.count + 1));
                    visited.add(new Point(position.x - 1, position.y));
                }
                else if (grid[position.x - 1][position.y].getPosition().equals(workplacePosition)) {
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

    public int countPeople() {
        int count = 0;
        for (int i = 0; i < gridSize; ++i) {
            for (int j = 0; j < gridSize; ++j) {
                if (grid[i][j] != null && grid[i][j].getType() == FieldType.ZONE_RESIDENTIAL) {
                    count += ((Residential)grid[i][j]).getPeople().size();
                }
            }
        }
        return count;
    }

    public void calculateMood(Person person) {
        person.setMood(0);
        if (searchForStadium(person)) {
            person.setMood(person.getMood() + 5);
        }
        if (searchForPolice(person)) {
            person.setMood(person.getMood() + 5);
        }
        if (searchForIndustrial(person)) {
            person.setMood(person.getMood() + 5);
        }
        {
            if (getWorkplaceDistance(person) < 6) person.setMood(person.getMood() + 5);
            else if (getWorkplaceDistance(person) < 12) person.setMood(person.getMood() + 3);
            else person.setMood(person.getMood() + 1);
        }

        //todo : searchForForest && boost mood based on tax
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
