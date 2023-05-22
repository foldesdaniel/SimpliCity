package simplicity.View.Components;

import simplicity.Model.GameModel;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A modified JButton used in the control panel
 */
public class ControlPanelTabButton extends JButton {

    private boolean selected = false;
    private boolean hovering = false;

    public ControlPanelTabButton(String text) {
        this.setText(text);
        this.setBorder(new CompoundBorder(
                new MatteBorder(2, 2, 0, 2, new Color(255, 255, 255, 64)),
                new EmptyBorder(5, 5, 5, 5)
        ));
        this.setBackground(GameModel.BG_DARK);
        this.setFocusPainted(false);
        this.setContentAreaFilled(false);
        this.setRolloverEnabled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hovering = false;
                repaint();
            }
        });
    }

    public void select() {
        this.selected = true;
        this.repaint();
    }

    public void unselect() {
        this.selected = false;
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.hovering) {
            int startingAlpha = 70;
            int amount = (int) (startingAlpha / (double) (this.getHeight() - 7));
            for (int i = 2, j = 0; i < this.getHeight(); i++, j++) {
                g.setColor(new Color(255, 255, 255, startingAlpha - (j + 1) * amount));
                g.drawLine(2, i, this.getWidth() - 2, i);
            }
        } else if (!this.selected) {
            int startingAlpha = 70;
            int amount = (int) (startingAlpha / (double) (this.getHeight() - 7));
            for (int i = 2, j = 0; i < this.getHeight(); i++, j++) {
                g.setColor(new Color(0, 0, 0, startingAlpha - (j + 1) * amount));
                g.drawLine(2, i, this.getWidth() - 2, i);
            }
        }
    }
}
