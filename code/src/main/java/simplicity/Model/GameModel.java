package simplicity.Model;

import simplicity.Model.Education.School;
import simplicity.Model.Finances.Finance;
import simplicity.Model.GameTime.Date;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeTickListener;
import simplicity.Model.Placeables.Placeable;
import simplicity.Model.Placeables.Road;
import simplicity.Model.Resource.ResourceLoader;

import java.awt.*;

public class GameModel implements InGameTimeTickListener {

    public static final Image MISSING_IMG = ResourceLoader.loadImage("missing.png");
    public static final Image GRASS_IMG = ResourceLoader.loadImage("grass.png");
    public static final Image SELECTION_IMG = ResourceLoader.loadImage("selection.png");
    public static final Image ROAD_STRAIGHT_IMG = ResourceLoader.loadImage("road.png");
    public static final Image ROAD_TURN_IMG = ResourceLoader.loadImage("road_turn.png");
    public static final Image ROAD_T = ResourceLoader.loadImage("road_t.png");
    public static final Image ROAD_ALL = ResourceLoader.loadImage("road_all.png");
    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    //just for testing purposes
    private final int gridSize = 3;
    private int mood;
    private Date nextDisaster;
    private int secondaryPercentage;
    private int uniPercentage;
    private Placeable grid[][];
    private Finance finance;

    public GameModel() {
        inGameTime.addInGameTimeTickListener(this);
        inGameTime.startInGameTime(InGameSpeeds.NORMAL);
        this.finance = new Finance(10000); //starting wealth
        this.secondaryPercentage = 70;
        this.uniPercentage = 22;
        this.mood = 0;

        //Initialize grid
//        this.grid = new Placeable[this.gridSize][this.gridSize];
//        for (int i = 0; i < this.gridSize; ++i) {
//            for (int j = 0; j < this.gridSize; ++j) {
//                this.grid[i][j] = null; //null == not initialized block
//            }
//        }
//        this.grid = new Placeable[][]{
//                {new School(new Point(0, 0)), new School(new Point(0, 1)), new School(new Point(0, 2))},
//                {new School(new Point(1, 0)), new Road(new Point(1, 1)), new School(new Point(1, 2))},
//                {new School(new Point(2, 0)), new School(new Point(2, 1)), new School(new Point(2, 2))}
//        };
//        this.grid = new Placeable[][]{
//                {new School(new Point(0, 0)), new School(new Point(0, 1)), new School(new Point(0, 2))},
//                {new School(new Point(1, 0)), new School(new Point(1, 1)), new School(new Point(1, 2))},
//                {new School(new Point(2, 0)), new School(new Point(2, 1)), new School(new Point(2, 2))}
//        };
//        this.grid = new Placeable[][]{
//                {new School(new Point(0, 0)), new School(new Point(0, 1)), new Road(new Point(0, 2))},
//                {new School(new Point(1, 0)), new School(new Point(1, 1)), new Road(new Point(1, 2))},
//                {new School(new Point(2, 0)), new School(new Point(2, 1)), new Road(new Point(2, 2))}
//        };
        this.grid = new Placeable[][]{
                {new School(new Point(0, 0)), new School(new Point(0, 1)), new Road(new Point(0, 2))},
                {new School(new Point(1, 0)), new Road(new Point(1, 1)), new Road(new Point(1, 2))},
                {new School(new Point(2, 0)), new School(new Point(2, 1)), new Road(new Point(2, 2))}
        };

        System.out.println(canRoadBeDestroyed(grid[0][1], grid[2][1], grid[1][1]));
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
}
