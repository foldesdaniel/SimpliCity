package simplicity.View.Game;

import simplicity.Model.Game.FieldType;
import simplicity.Model.Game.RoadType;
import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Listeners.MouseAdapter;
import simplicity.View.Style.InsetShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class PlayingFieldView extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {

    // TODO: move to GameModel
    //private Placeable[][] grid;
    private final int gridSize;
    private final int defaultFieldSize = 32;
    private int fieldSize;
    private Point hoverField;
    private int offsetX;
    private int offsetY;
    private GameModel model;

    private static final Point NO_SELECTION = new Point(-1, -1);

    public PlayingFieldView() {
        this.model = GameModel.getInstance();
        this.gridSize = model.getGridSize();
        this.fieldSize = defaultFieldSize;
        this.hoverField = NO_SELECTION;
        this.offsetX = 0;
        this.offsetY = 0;
        // resetPlayingField();
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked");
            }
        };
        this.addMouseListener(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        this.setBorder(new InsetShadowBorder(16));
    }

    /*private void resetPlayingField() {
        int width = this.gridDimension.width;
        int height = this.gridDimension.height;
        grid = new Placeable[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Placeable fv = new Placeable(new Point(i, j), GameModel.GRASS_IMG);
                grid[i][j] = fv;
            }
        }
    }*/

    private boolean doesGridFitHorizontally() {
        int panelWidth = this.getWidth();
        return fieldSize * gridSize <= panelWidth;
    }

    private boolean doesGridFitVertically() {
        int panelHeight = this.getHeight();
        return fieldSize * gridSize <= panelHeight;
    }

    private boolean doesGridFit() {
        return doesGridFitHorizontally() && doesGridFitVertically();
    }

    private Point getDefaultOffset() {
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        return new Point((int) (panelWidth / 2.0 - gridSize * fieldSize / 2.0), (int) (panelHeight / 2.0 - gridSize * fieldSize / 2.0));
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        int panelWidth = this.getWidth();
        int panelHeight = this.getHeight();
        g.setColor(new Color(255, 200, 200));
        g.fillRect(0, 0, panelWidth, panelHeight);
        Point centerOffset = this.getDefaultOffset();
        if (doesGridFitHorizontally() || !isGridDraggedX) offsetX = centerOffset.x;
        if (doesGridFitVertically() || !isGridDraggedY) offsetY = centerOffset.y;
        int tempX1 = -(int) Math.ceil(offsetX / (double) fieldSize);
        int tempX2 = (int) Math.ceil((this.getWidth() - (offsetX + gridSize * fieldSize)) / (double) fieldSize);
        int tempY1 = -(int) Math.ceil(offsetY / (double) fieldSize);
        int tempY2 = (int) Math.ceil((this.getHeight() - (offsetY + gridSize * fieldSize)) / (double) fieldSize);
        for (int i = tempY1; i < gridSize + tempY2; i++) {
            for (int j = tempX1; j < gridSize + tempX2; j++) {
                Point coord = new Point(
                        fieldSize * i,
                        fieldSize * j
                );
                if ((i < 0 || i >= gridSize) || (j < 0 || j >= gridSize)) {
                    g.drawImage(GameModel.GRASS_IMG, offsetX + coord.y, offsetY + coord.x, fieldSize, fieldSize, null);
                }
            }
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Point coord = new Point(
                        fieldSize * i,
                        fieldSize * j
                );
                // instead of this, a method of a grid element will be called to determine what image will be used
                g.drawImage(GameModel.GRASS_IMG, offsetX + coord.y, offsetY + coord.x, fieldSize, fieldSize, null);
                if(model.grid(j,i) != null){
                    Placeable leftNeighbor = (j > 0) ? model.grid(j - 1,i) : null;
                    Placeable rightNeighbor = (j < gridSize - 1) ? model.grid(j + 1,i) : null;
                    Placeable upNeighbor = (i > 0) ? model.grid(j,i - 1) : null;
                    Placeable downNeighbor = (i < gridSize - 1) ? model.grid(j,i + 1) : null;
                    Image img = model.grid(j,i).getImage(leftNeighbor, rightNeighbor, upNeighbor, downNeighbor);
                    g.drawImage(img, offsetX + coord.y, offsetY + coord.x, fieldSize, fieldSize, null);
                }
            }
        }
        g.setColor(Color.BLACK);
        if (hoverField != NO_SELECTION) {
            double mult = 26 / 16.0;
            int multSize = (int) Math.round(fieldSize * mult);
            double mult2 = 5 / 16.0;
            int selOffset = (int) Math.round(fieldSize * mult2);
            g.drawImage(GameModel.SELECTION_2_IMG, offsetX + (fieldSize * hoverField.x) - selOffset, offsetY + (fieldSize * hoverField.y) - selOffset, multSize, multSize, null);
            g.drawRect(offsetX + (fieldSize * hoverField.x), offsetY + (fieldSize * hoverField.y), fieldSize, fieldSize);
        }
        g.drawRoundRect(offsetX, offsetY, fieldSize * gridSize, fieldSize * gridSize, 24, 24);
        g.drawRoundRect(offsetX + 1, offsetY + 1, fieldSize * gridSize - 2, fieldSize * gridSize - 2, 24, 22);
        // g.fillRoundRect(testPoint.x, testPoint.y, 5, 5, 5, 5);
    }

    private FieldClickListener fieldClickListener;

    public void setInfoListener(FieldClickListener fieldClickListener) {
        this.fieldClickListener = fieldClickListener;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            this.mouseLeftClicked(e);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            this.mouseRightClicked(e);
        }
    }

    private void mouseLeftClicked(MouseEvent e) {
        boolean fieldHit = hoverField != NO_SELECTION;
        fieldClickListener.fieldClicked(fieldHit ? model.grid(hoverField.x,hoverField.y) : null);
        // this.repaint();
    }

    private void mouseRightClicked(MouseEvent e) {
        boolean fieldHit = hoverField != NO_SELECTION;
        // if (fieldHit) model.grid(hoverField.x,hoverField.y).toggleTeszt();
        this.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e) && isDraggingGrid) {
            isDraggingGrid = false;
            Point dragPointer = new Point(e.getPoint().x - dragStart.x, e.getPoint().y - dragStart.y);
            double distance = Math.sqrt(Math.pow(dragPointer.x, 2) + Math.pow(dragPointer.y, 2));
            if (distance <= 5) {
                mouseRightClicked(e);
            }
        }
        if (SwingUtilities.isLeftMouseButton(e) && isLeftDragging) {
            isLeftDragging = false;
            Point dragPointer = new Point(e.getPoint().x - dragStart.x, e.getPoint().y - dragStart.y);
            double distance = Math.sqrt(Math.pow(dragPointer.x, 2) + Math.pow(dragPointer.y, 2));
            if (distance <= 5) {
                mouseLeftClicked(e);
            } else {
                // System.out.println("actual drag");
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (hoverField != NO_SELECTION) {
            hoverField = NO_SELECTION;
            this.repaint();
        }
    }

    private boolean isDraggingGrid = false;
    private boolean isLeftDragging = false;
    private boolean isGridDraggedX = false;
    private boolean isGridDraggedY = false;
    private Point dragStart = NO_SELECTION;
    private Point dragOffset = NO_SELECTION;

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            if (!isDraggingGrid) {
                isDraggingGrid = true;
                isGridDraggedX = true;
                isGridDraggedY = true;
                dragStart = e.getPoint();
                dragOffset = new Point(offsetX, offsetY);
            }
            Point defaultOffset = this.getDefaultOffset();
            Point dragPointer = new Point(e.getPoint().x - dragStart.x, e.getPoint().y - dragStart.y);
            int newOffsetX = dragOffset.x + dragPointer.x;
            int newOffsetY = dragOffset.y + dragPointer.y;
            int feleX = (int) Math.round(this.getWidth() / 2.0);
            int feleY = (int) Math.round(this.getHeight() / 2.0);
            boolean canDragX = newOffsetX > -(fieldSize * gridSize - feleX) && newOffsetX < feleX;
            boolean canDragY = newOffsetY > -(fieldSize * gridSize - feleY) && newOffsetY < feleY;
            if (canDragX && !doesGridFitHorizontally()) offsetX = newOffsetX;
            if (canDragY && !doesGridFitVertically()) offsetY = newOffsetY;
            this.updateHover(e.getX(), e.getY(), false, false);
            this.repaint();
        } else {
            if (!isLeftDragging) {
                isLeftDragging = true;
                dragStart = e.getPoint();
                this.onHoverChange();
            }
            this.updateHover(e.getX(), e.getY(), true, true);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.updateHover(e.getX(), e.getY(), false, true);
    }

    private void updateHover(int x, int y, boolean dragged, boolean repaint) {
        int xOffset = x - offsetX;
        int yOffset = y - offsetY;
        Point newHoverField = new Point(
                (xOffset >= 0) ? (xOffset / this.fieldSize) : -1,
                (yOffset >= 0) ? (yOffset / this.fieldSize) : -1
        );
        if (
                newHoverField.x >= 0 && newHoverField.x < gridSize &&
                        newHoverField.y >= 0 && newHoverField.y < gridSize
        ) {
            if (newHoverField.x != hoverField.x || newHoverField.y != hoverField.y) {
                hoverField = newHoverField;
                this.onHoverChange();
                if (repaint) this.repaint();
                // System.out.println(x + " " + y);
            }
        } else if (hoverField != NO_SELECTION) {
            hoverField = NO_SELECTION;
            if (repaint) this.repaint();
        }
    }

    private void onHoverChange() {
        boolean fieldHit = hoverField != NO_SELECTION;
        if (isLeftDragging) fieldClickListener.fieldClicked(fieldHit ? model.grid(hoverField.x,hoverField.y) : null);
    }

    private static final int minFieldSize = 10;
    private static final int maxFieldSize = 64;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int amount = -e.getWheelRotation() * 4;
        if ((amount > 0 && fieldSize < maxFieldSize) || (amount < 0 && fieldSize > minFieldSize)) {
            fieldSize += amount;
            if (doesGridFitHorizontally()) isGridDraggedX = false;
            if (doesGridFitVertically()) isGridDraggedY = false;
            this.updateHover(e.getX(), e.getY(), false, false);
            this.repaint();
        }
    }

}
