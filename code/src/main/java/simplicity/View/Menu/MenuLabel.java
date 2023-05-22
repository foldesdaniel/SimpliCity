package simplicity.View.Menu;

import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class MenuLabel extends JLabel {
    /**
     * Constructor with initialized label.
     *
     * @param label This label will display this text.
     */
    public MenuLabel(String label) {
        this.setText(label);
        this.setFont(CFont.get(Font.BOLD, 24));
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

}
