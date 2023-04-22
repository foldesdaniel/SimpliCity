package simplicity.View.Components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ControlPanelTabButton extends JButton {

    private final Color normalColor = new Color(96,98,100);
    private final Color hoverColor = new Color(96,98,100);

    public ControlPanelTabButton(String text){
        this.setText(text);
        this.setBorder(new EmptyBorder(5,5,5,5));
        this.setBackground(normalColor);
        this.setFocusPainted(false);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(normalColor);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(hoverColor);
            }
        });
    }

}
