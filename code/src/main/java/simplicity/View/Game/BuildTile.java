package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class BuildTile extends JPanel {

    private Placeable placeable;
    private final WrapLabel nameLabel;
    private final Dimension defaultImageSize = new Dimension(32, 32);
    private boolean isHovering = false;
    private final Cursor cursorNormal = Cursor.getDefaultCursor();
    private final Cursor cursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
    private boolean isDragging = false;
    private Point dragStart;
    private boolean tempNoHover = false;

    public static final int MINIMUM_WIDTH = 96;

    public BuildTile(Class pl) {
        this.placeable = newInstance(pl);
        //this.setMinimumSize(new Dimension(96, 10));
        //String displayName = "<html><p style=\"text-align:center;margin:0;padding:0;background-color:rgb(200,255,200)\">" + this.placeable.getDisplayName().replaceAll(" ","<br>") + "</p></html>";
        //this.nameLabel = new JLabel(displayName);
        this.nameLabel = new WrapLabel(this.placeable.getDisplayName());
        this.nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        //this.nameLabel.setBaseHeight();
        this.nameLabel.setSize(new Dimension(this.getPreferredSize().width,this.nameLabel.getPreferredSize().height));
        this.updateNameLabel();
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.setAlignmentY(Component.CENTER_ALIGNMENT);
        BuildTileImage img = new BuildTileImage();
        container.add(img);
        container.add(this.nameLabel);
        container.setOpaque(false);
        //container.setBackground(new Color(200,255,200));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.add(Box.createVerticalGlue());
        this.add(container);
        this.add(Box.createVerticalGlue());
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //if(!GamePanel.isPlacing()) {
                    GamePanel.setPlacing(placeable);
                    placeable = newInstance(pl);
                    setCursor(cursorNormal);
                    img.setCursor(cursorNormal);
                    tempNoHover = true;
                    repaint();
                //}
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging) {
                    isDragging = false;
                    Point dragPointer = new Point(e.getPoint().x - dragStart.x, e.getPoint().y - dragStart.y);
                    double distance = Math.sqrt(Math.pow(dragPointer.x, 2) + Math.pow(dragPointer.y, 2));
                    if (distance <= GameModel.DRAG_THRESHOLD) {
                        mouseClicked(e);
                    }
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                //if(!GamePanel.isPlacing()){
                    setCursor(cursorHand);
                    img.setCursor(cursorHand);
                //}
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();
            }
        });
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if(!isDragging){
                    isDragging = true;
                    dragStart = e.getPoint();
                }
            }
        });
        // this.repaint();
    }

    public static Placeable newInstance(Class pl) {
        try{
            return (Placeable) pl.getConstructor(Point.class).newInstance(GameModel.NO_SELECTION);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    public void updateNameLabel(){
        this.nameLabel.fitHeight(this.getPreferredSize().width);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //if(isHovering && !GamePanel.isPlacing()){
        if(isHovering && !tempNoHover){
            g.drawImage(GameModel.TILE_HOVER_IMG, 0, 0, this.getWidth(), this.getHeight(), null);
        }
        tempNoHover = false;
    }

    class BuildTileImage extends JPanel {

        BuildTileImage(){
            int pw = placeable.getSize().width;
            int ph = placeable.getSize().height;
            Dimension imageSize = (pw > ph) ? (
                new Dimension(defaultImageSize.width, (int)Math.round((ph/(double)pw)*defaultImageSize.height))
            ) : (
                new Dimension((int)Math.round((pw/(double)ph)*defaultImageSize.width), defaultImageSize.height)
            );
            this.setPreferredSize(imageSize);
            this.setSize(imageSize);
            this.setMaximumSize(imageSize);
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            g.drawImage(placeable.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }

}
