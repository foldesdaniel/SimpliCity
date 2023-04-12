package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BuildTile extends JPanel {

    private final Placeable placeable;

    public BuildTile(Placeable p){
        this.placeable = p;
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("clicked on panel containing " + placeable);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                if(!GamePanel.isPlacing()){
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if(!GamePanel.isPlacing()) {
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });
        this.setPreferredSize(new Dimension(32, 32));
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics gr) {
        // super.paintComponent(gr);
        Graphics2D g = (Graphics2D) gr;
        g.drawImage(placeable.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
    }

}
