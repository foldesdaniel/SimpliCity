package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuildTile extends JPanel {

    private Placeable placeable;
    private final JLabel nameLabel;
    private final Dimension imageSize = new Dimension(32, 32);
    private boolean isHovering = false;
    private final Cursor cursorNormal = Cursor.getDefaultCursor();
    private final Cursor cursorHand = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);

    public BuildTile(Class pl) {
        this.placeable = newInstance(pl);
        //String displayName = "<html><p style=\"text-align:center;margin:0;padding:0;background-color:rgb(200,255,200)\">" + this.placeable.getDisplayName().replaceAll(" ","<br>") + "</p></html>";
        //this.nameLabel = new JLabel(displayName);
        this.nameLabel = new JLabel(this.placeable.getDisplayName());
        this.nameLabel.setBackground(new Color(200,255,200));
        this.nameLabel.setFont(CFont.get(Font.PLAIN, 20));
        this.nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        BuildTileImage img = new BuildTileImage();
        this.add(img);
        this.add(nameLabel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(!GamePanel.isPlacing()) {
                    GamePanel.setPlacing(placeable);
                    placeable = newInstance(pl);
                    setCursor(cursorNormal);
                    repaint();
                }
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                isHovering = true;
                if(!GamePanel.isPlacing()) setCursor(cursorHand);
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                isHovering = false;
                repaint();
            }
        });
        // this.setPreferredSize(new Dimension(imageSize.width, imageSize.height + nameLabel.getFont().getSize()*2));
        // this.repaint();
    }

    private Placeable newInstance(Class pl) {
        try{
            return (Placeable) pl.getConstructor(Point.class).newInstance(GameModel.NO_SELECTION);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(isHovering && !GamePanel.isPlacing()){
            g.drawImage(GameModel.TILE_HOVER_IMG, 0, 0, this.getWidth(), this.getHeight(), null);
        }
    }

    class BuildTileImage extends JPanel {

        BuildTileImage(){
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
