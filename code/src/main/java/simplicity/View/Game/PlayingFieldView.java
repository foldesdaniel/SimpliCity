package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Listeners.AnimationTickListener;
import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.GameModel;
import simplicity.Model.Persistence.Persistence;
import simplicity.Model.Listeners.ModeChangeListener;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.*;
import simplicity.Model.Resource.Animation;
import simplicity.View.Listeners.MouseAdapter;
import simplicity.View.Style.InsetShadowBorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;

public class PlayingFieldView extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener, AnimationTickListener {

    // TODO: move to GameModel
    //private Placeable[][] grid;
    private final int gridSize;
    private final int defaultFieldSize = 32;
    private int fieldSize;
    private Point hoverField;
    private int offsetX;
    private int offsetY;
    private GameModel model;
    private final ArrayList<ModeChangeListener> modeListeners;

    public PlayingFieldView() {
        this.modeListeners = new ArrayList<>();
        this.model = GameModel.getInstance();
        this.gridSize = GameModel.GRID_SIZE;
        this.fieldSize = defaultFieldSize;
        this.hoverField = GameModel.NO_SELECTION;
        this.offsetX = 0;
        this.offsetY = 0;
        // resetPlayingField();
        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // System.out.println("clicked");
            }
        };
        this.addMouseListener(this);
        this.addMouseListener(mouseAdapter);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
        Animation.addAnimationTickListener(this);
        this.setBorder(new InsetShadowBorder(16));
        model.playAnim(Animation.createFireAnim(new Point(0,0)), 3200);
    }

    public void addModeListener(ModeChangeListener l){
        this.modeListeners.add(l);
    }

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
                g.drawImage(GameModel.GRASS_IMG, offsetX + coord.y, offsetY + coord.x, fieldSize, fieldSize, null);
            }
        }
        int shadowSize = fieldSize * 2;
        int shadowOpacity = 50;
        int shadowGap = (int)(fieldSize / 4.8);
        for(int i=0;i<shadowSize;i++){
            int alpha = shadowOpacity - (int)Math.round(i*(shadowOpacity/(double)shadowSize));
            if(alpha < 0) break;
            g.setColor(new Color(0, 0, 0, alpha));
            g.drawLine(
                offsetX - i - shadowGap, offsetY - i - shadowGap,
                offsetX + fieldSize * gridSize + i + shadowGap, offsetY - i - shadowGap
            );
            g.drawLine(
                offsetX - i - shadowGap, offsetY + fieldSize * gridSize + i + shadowGap,
                offsetX + fieldSize * gridSize + i + shadowGap, offsetY + fieldSize * gridSize + i + shadowGap
            );
            g.drawLine(
                offsetX - i - shadowGap, offsetY - i + 1 - shadowGap,
                offsetX - i - shadowGap, offsetY + fieldSize * gridSize + i - 1 + shadowGap
            );
            g.drawLine(
                offsetX + fieldSize * gridSize + i + shadowGap, offsetY - i + 1 - shadowGap,
                offsetX + fieldSize * gridSize + i + shadowGap, offsetY + fieldSize * gridSize + i - 1 + shadowGap
            );
        }
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Point coord = new Point(
                    fieldSize * i,
                    fieldSize * j
                );
                if(model.grid(j,i) != null){
                    Placeable leftNeighbor = (j > 0) ? model.grid(j - 1,i) : null;
                    Placeable rightNeighbor = (j < gridSize - 1) ? model.grid(j + 1,i) : null;
                    Placeable upNeighbor = (i > 0) ? model.grid(j,i - 1) : null;
                    Placeable downNeighbor = (i < gridSize - 1) ? model.grid(j,i + 1) : null;
                    Image img = model.grid(j,i).getImage(leftNeighbor, rightNeighbor, upNeighbor, downNeighbor);
                    Dimension size = model.grid(j,i).getSize();
                    g.drawImage(img, offsetX + coord.y, offsetY + coord.x - (fieldSize * (size.height - 1)), fieldSize * size.width, fieldSize * size.height, null);
                }
            }
        }
        for(Animation anim : model.getAnimations()){
            Point coord = new Point(
                fieldSize * anim.getPosition().x,
                fieldSize * anim.getPosition().y
            );
            g.drawImage(anim.getCurrentImage(), offsetX + coord.y, offsetY + coord.x, fieldSize, fieldSize, null);
        }
        g.setColor(Color.BLACK);
        if (hoverField != GameModel.NO_SELECTION) {
            Placeable p = GamePanel.isPlacing() ? GamePanel.getPlacee() : model.grid(hoverField.x,hoverField.y);
            Point placeholderPos = (p instanceof PlaceableTemp) ? new Point(hoverField.x - p.getDisplayPosition().x, p.getDisplayPosition().y - hoverField.y) : new Point(0,0);
            int placeHolderOffsetX = ((p instanceof PlaceableTemp) ? placeholderPos.x : 0) * fieldSize;
            int placeHolderOffsetY = ((p instanceof PlaceableTemp) ? placeholderPos.y : 0) * fieldSize;
            Dimension size = (p == null) ? new Dimension(1,1) : p.getDisplaySize();
            double mult = 26 / 16.0;
            int multSizeX = (int) Math.round(fieldSize * size.width * mult);
            int multSizeY = (int) Math.round(fieldSize * size.height * mult);
            double mult2 = 5 / 16.0;
            int selOffsetX = (int) Math.round(fieldSize * size.width * mult2);
            int selOffsetY = (int) Math.round(fieldSize * size.height * mult2 + (fieldSize * (size.height - 1)));
            if(GamePanel.isPlacing()){
                Image img = p.getImage();
                g.drawImage(img, offsetX + fieldSize * hoverField.x, offsetY + fieldSize * (hoverField.y - (size.height - 1)), fieldSize * size.width, fieldSize * size.height, null);
                if(model.canPlace(p, hoverField)){
                    g.drawImage(GameModel.SELECTION_VALID_IMG, offsetX + (fieldSize * hoverField.x) - selOffsetX - placeHolderOffsetX, offsetY + (fieldSize * hoverField.y) - selOffsetY + placeHolderOffsetY, multSizeX, multSizeY, null);
                }else{
                    g.drawImage(GameModel.SELECTION_INVALID_IMG, offsetX + (fieldSize * hoverField.x) - selOffsetX - placeHolderOffsetX, offsetY + (fieldSize * hoverField.y) - selOffsetY + placeHolderOffsetY, multSizeX, multSizeY, null);
                }
            }else if(GamePanel.isDeleteMode()){
                g.drawImage(GameModel.SELECTION_INVALID_IMG, offsetX + (fieldSize * hoverField.x) - selOffsetX - placeHolderOffsetX, offsetY + (fieldSize * hoverField.y) - selOffsetY + placeHolderOffsetY, multSizeX, multSizeY, null);
            }else{
                g.drawImage(GameModel.SELECTION_IMG, offsetX + (fieldSize * hoverField.x) - selOffsetX - placeHolderOffsetX, offsetY + (fieldSize * hoverField.y) - selOffsetY + placeHolderOffsetY, multSizeX, multSizeY, null);
            }

            // g.drawRect(offsetX + (fieldSize * hoverField.x), offsetY + (fieldSize * hoverField.y), fieldSize, fieldSize);
        }
        // g.drawRoundRect(offsetX, offsetY, fieldSize * gridSize, fieldSize * gridSize, 24, 24);
        // g.drawRoundRect(offsetX + 1, offsetY + 1, fieldSize * gridSize - 2, fieldSize * gridSize - 2, 24, 22);
    }

    @Override
    public void onAnimationTick(){
        this.repaint();
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

    private void placingClick(){
        Placeable placee = GamePanel.stopPlacing(true);
        if(placee instanceof Residential){
            model.placeResidential(hoverField);
        }else if(placee instanceof Service){
            model.placeService(hoverField);
        }else if(placee instanceof Industrial){
            model.placeIndustrial(hoverField);
        }else if(placee instanceof Road){
            model.placeRoad(hoverField);
        }else if(placee instanceof Police){
            model.placePolice(hoverField);
        }else if(placee instanceof Stadium){
            model.placeStadium(hoverField);
        }else if(placee instanceof School){
            model.placeSchool(hoverField);
        }else if(placee instanceof University){
            model.placeUniversity(hoverField);
        }else if(placee instanceof Forest){
            model.placeForest(hoverField);
        }
        this.repaint();
    }

    private void isDeleteClick(){
        Placeable h = model.grid(hoverField.x,hoverField.y);
        if(h == null) return;
        Placeable placee = h instanceof PlaceableTemp hh ? hh.getPlaceable() : h;
        Point position = h instanceof PlaceableTemp hh ? hh.getDisplayPosition() : h.getPosition();
        if(placee instanceof Residential){
            model.removeResidential(position);
        }else if(placee instanceof Service){
            model.removeService(position);
        }else if(placee instanceof Industrial){
            model.removeIndustrial(position, false);
        }else if(placee instanceof Road){
            model.removeRoad(position);
        }else if(placee instanceof Police){
            model.removePolice(position);
        }else if(placee instanceof Stadium){
            model.removeStadium(position);
        }else if(placee instanceof School){
            model.removeSchool(position);
        }else if(placee instanceof University){
            model.removeUniversity(position);
        }else if(placee instanceof Forest){
            model.removeForest(position);
        }
        this.repaint();
    }

    private void mouseLeftClicked(MouseEvent e) {
        boolean fieldHit = hoverField != GameModel.NO_SELECTION;
        if(GamePanel.isPlacing()){
            this.placingClick();
        }else if(GamePanel.isDeleteMode()){
            this.isDeleteClick();
        }else{
            fieldClickListener.fieldClicked(fieldHit ? model.grid(hoverField.x,hoverField.y) : null);
        }
        // this.repaint();
    }

    private void mouseRightClicked(MouseEvent e) {
        if(GamePanel.isPlacing()){
            GamePanel.stopPlacing();
        }else if(GamePanel.isDeleteMode()){
            GamePanel.stopDeleteMode();
        }else{
            boolean fieldHit = hoverField != GameModel.NO_SELECTION;
            // ...
        }
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
            if (distance <= GameModel.DRAG_THRESHOLD) {
                mouseRightClicked(e);
            }
        }
        if (SwingUtilities.isLeftMouseButton(e) && isLeftDragging) {
            isLeftDragging = false;
            Point dragPointer = new Point(e.getPoint().x - dragStart.x, e.getPoint().y - dragStart.y);
            double distance = Math.sqrt(Math.pow(dragPointer.x, 2) + Math.pow(dragPointer.y, 2));
            if (distance <= GameModel.DRAG_THRESHOLD) {
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
        if (hoverField != GameModel.NO_SELECTION) {
            hoverField = GameModel.NO_SELECTION;
            this.repaint();
        }
    }

    private boolean isDraggingGrid = false;
    private boolean isLeftDragging = false;
    private boolean isGridDraggedX = false;
    private boolean isGridDraggedY = false;
    private Point dragStart = GameModel.NO_SELECTION;
    private Point dragOffset = GameModel.NO_SELECTION;

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
        } else if (hoverField != GameModel.NO_SELECTION) {
            hoverField = GameModel.NO_SELECTION;
            if (repaint) this.repaint();
        }
    }

    private void onHoverChange() {
        boolean fieldHit = hoverField != GameModel.NO_SELECTION;
        if(isLeftDragging) {
            if(GamePanel.isPlacing()){
                this.placingClick();
            }else if(GamePanel.isDeleteMode()){
                this.isDeleteClick();
            }else{
                fieldClickListener.fieldClicked(fieldHit ? model.grid(hoverField.x, hoverField.y) : null);
            }
        }
    }

    private static final int SCROLL_STEP = 4;



    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        final int MIN_FIELD_SIZE = Math.max(SCROLL_STEP, (int) Math.round(this.getHeight()*0.793 / GameModel.GRID_SIZE));
        final int MAX_FIELD_SIZE = Math.max(64, MIN_FIELD_SIZE);
        int rot = e.getWheelRotation();
        int amount = (rot < 0 ? 1 : (rot > 0 ? -1 : 0)) * SCROLL_STEP;
        if ((amount > 0 && fieldSize < MAX_FIELD_SIZE) || (amount < 0 && fieldSize > MIN_FIELD_SIZE)) {
            fieldSize = Math.min(Math.max(fieldSize + amount, MIN_FIELD_SIZE), MAX_FIELD_SIZE);
            if (doesGridFitHorizontally()) isGridDraggedX = false;
            if (doesGridFitVertically()) isGridDraggedY = false;
            this.updateHover(e.getX(), e.getY(), false, false);
            this.repaint();
        }
    }

}
