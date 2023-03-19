package simplicity.View.PlayingField;

import simplicity.Model.Events.FieldClickListener;
import simplicity.Model.GameModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PlayingFieldView extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    private FieldView[][] grid;
    private final Dimension gridDimension;
    private int defaultFieldSize = 32;
    private int fieldSize;
    private Point hoverField;
    private int offsetX;
    private int offsetY;

    private static final Point NO_SELECTION = new Point(-1, -1);

    public PlayingFieldView(int width, int height) {
        this.gridDimension = new Dimension(width, height);
        this.fieldSize = defaultFieldSize;
        this.hoverField = NO_SELECTION;
        this.offsetX = 0;
        this.offsetY = 0;
        resetPlayingField();
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    private void resetPlayingField() {
        int width = this.gridDimension.width;
        int height = this.gridDimension.height;
        grid = new FieldView[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                FieldView fv = new FieldView(new Point(i, j), GameModel.GRASS_IMG);
                grid[i][j] = fv;
            }
        }
    }

    private boolean doesGridFitHorizontally(){
        int gridWidth = this.gridDimension.width;
        int panelWidth = this.getWidth();
        return fieldSize*gridWidth*zoomFactor <= panelWidth;
    }

    private boolean doesGridFitVertically(){
        int gridHeight = this.gridDimension.height;
        int panelHeight = this.getHeight();
        return fieldSize*gridHeight*zoomFactor <= panelHeight;
    }

    private boolean doesGridFit(){
        return doesGridFitHorizontally() && doesGridFitVertically();
    }

    private Point getDefaultOffset(){
        int gridWidth = this.gridDimension.width;
        int gridHeight = this.gridDimension.height;
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        return new Point((int)(panelWidth/2.0 - gridWidth*fieldSize/2.0), (int)(panelHeight/2.0 - gridHeight*fieldSize/2.0));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        int gridWidth = this.gridDimension.width;
        int gridHeight = this.gridDimension.height;
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        g.setColor(new Color(255,200,200));
        g.fillRect(0, 0, panelWidth, panelHeight);
        Point defaultOffset = this.getDefaultOffset();
        int gridOffsetX = 0;
        int gridOffsetY = 0;
        if(doesGridFitHorizontally() || !isGridDraggedX){
            offsetX = defaultOffset.x;
        }
        if(doesGridFitVertically() || !isGridDraggedY){
            offsetY = defaultOffset.y;
        }
        gridOffsetX = offsetX;
        gridOffsetY = offsetY;
        for (int i = 0; i < gridHeight; i++) {
            for (int j = 0; j < gridWidth; j++) {
                Point coord = new Point(
                        fieldSize * i,
                        fieldSize * j
                );
                //g.setColor(new Color(44, 99, 205));
                //g.drawRect(coord.y-1, coord.x-1, fieldDims.width, fieldDims.height);
                g.drawImage(grid[i][j].getImage(), gridOffsetX+coord.y, gridOffsetY+coord.x, fieldSize, fieldSize, null);
                if(i == hoverField.y && j == hoverField.x){
                    //g.setColor(new Color(255, 99, 205));
                    //g.fillRect(coord.y, coord.x, fieldDims.width, fieldDims.height);
                    g.drawImage(GameModel.SELECTION_IMG, gridOffsetX+coord.y, gridOffsetY+coord.x, fieldSize, fieldSize, null);
                }
            }
        }
        g.setColor(new Color(44, 99, 205));
        g.drawRect(gridOffsetX, gridOffsetY, fieldSize * gridWidth, fieldSize * gridHeight);
        g.fillRoundRect(testPoint.x, testPoint.y, 5, 5, 5, 5);
    }

    private FieldClickListener fieldClickListener;

    public void addInfoListener(FieldClickListener fieldClickListener){
        this.fieldClickListener = fieldClickListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(hoverField != NO_SELECTION){
            fieldClickListener.fieldClicked(grid[hoverField.x][hoverField.y]);
        }
        repaint();
    }

    @Override public void mousePressed(MouseEvent e) {}

    @Override public void mouseReleased(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e) && isDraggingGrid) isDraggingGrid = false;
    }

    @Override public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {
        if(hoverField != NO_SELECTION){
            hoverField = NO_SELECTION;
            this.repaint();
        }
    }

    private boolean isDraggingGrid = false;
    private boolean isGridDraggedX = false;
    private boolean isGridDraggedY = false;
    private Point dragStart = NO_SELECTION;
    private Point dragOffset = NO_SELECTION;

    @Override
    public void mouseDragged(MouseEvent e) {
        if(SwingUtilities.isRightMouseButton(e)){
            if(!isDraggingGrid){
                isDraggingGrid = true;
                isGridDraggedX = true;
                isGridDraggedY = true;
                dragStart = e.getPoint();
                dragOffset = new Point(offsetX, offsetY);
            }
            int gw = fieldSize*gridDimension.width;
            int gh = fieldSize*gridDimension.height;
            Point defaultOffset = this.getDefaultOffset();
            Point dragPointer = new Point(e.getPoint().x-dragStart.x, e.getPoint().y-dragStart.y);
            int newOffsetX = dragOffset.x + dragPointer.x;
            int newOffsetY = dragOffset.y + dragPointer.y;
            int feleX = (int)Math.round(gridDimension.width / 2.0);
            int feleY = (int)Math.round(gridDimension.height / 2.0);
            boolean canDragX = newOffsetX > -fieldSize*(gridDimension.width-feleX) && newOffsetX < this.getWidth() - fieldSize*feleX;
            boolean canDragY = newOffsetY > -fieldSize*(gridDimension.height-feleY) && newOffsetY < this.getHeight() - fieldSize*feleY;
            if(canDragX) offsetX = newOffsetX;
            if(canDragY) offsetY = newOffsetY;
            System.out.println(newOffsetX + " " + (this.getWidth() - fieldSize));
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseMoved(e.getX(), e.getY(), false, true);
    }

    private void mouseMoved(int x, int y, boolean dragged, boolean repaint){
        int xOffset = x-offsetX;
        int yOffset = y-offsetY;
        Point newHoverField = new Point(
            (xOffset >= 0) ? (xOffset / this.fieldSize) : -1,
            (yOffset >= 0) ? (yOffset / this.fieldSize) : -1
        );
        if(
            newHoverField.x >= 0 && newHoverField.x < this.gridDimension.width &&
            newHoverField.y >= 0 && newHoverField.y < this.gridDimension.height
        ){
            //if(newHoverField.x != hoverField.x || newHoverField.y != hoverField.y){
                hoverField = newHoverField;
                if(repaint) this.repaint();
                // System.out.println(x + " " + y);
            //}
        }else if(hoverField != NO_SELECTION){
            hoverField = NO_SELECTION;
            if(repaint) this.repaint();
        }
    }

    private static final int minFieldSize = 10;
    private static final int maxFieldSize = 64;
    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private static final double MIN_ZOOM_FACTOR = 0.1;
    private static final double MAX_ZOOM_FACTOR = 5.0;

    private Point testPoint = new Point(0,0);
    private double prevZoomDiv = 1.0;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = -e.getWheelRotation() * 4;
        if((amount > 0 && fieldSize < maxFieldSize) || (amount < 0 && fieldSize > minFieldSize)){
            Point anchor = e.getPoint();
            fieldSize += amount;
            double zoomDiv = fieldSize/(double)defaultFieldSize;
            /*if(!doesGridFitHorizontally()) int newOffsetX = (int)Math.round(
                    (amount > 0) ? (anchor.getX() - (anchor.getX() - offsetX)*zoomDiv) : ((offsetX - anchor.getX())/prevZoomDiv + anchor.getX())
            );*/
            /*if(!doesGridFitVertically()) int newOffsetY = (int)Math.round(
                    (amount > 0) ? (anchor.getY() - (anchor.getY() - offsetY)*zoomDiv) : ((offsetY - anchor.getY())/prevZoomDiv + anchor.getY())
            );*/
            testPoint = new Point(
                (int)Math.round(anchor.getX() - (anchor.getX() - offsetX)*zoomDiv),
                (int)Math.round(anchor.getY() - (anchor.getY() - offsetY)*zoomDiv)
            );
            // offsetX = newOffsetX;
            // offsetY = newOffsetY;
            if(doesGridFitHorizontally()) isGridDraggedX = false;
            if(doesGridFitVertically()) isGridDraggedY = false;
            mouseMoved(e.getX(), e.getY(), false, true);
            this.repaint();
            prevZoomDiv = zoomDiv;
        }
    }

}
