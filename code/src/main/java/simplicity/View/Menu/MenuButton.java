package simplicity.View.Menu;

import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class MenuButton extends JButton {

    /**
     * Constructor with initialized label.
     *
     * @param label This button will display this text.
     */
    public MenuButton(String label) {
        this.setText(label);
        this.setFont(CFont.get(Font.BOLD, 28));
        Dimension buttonSize = new Dimension(200, 50);
        this.setPreferredSize(buttonSize);
        this.setMinimumSize(buttonSize);
        this.setMaximumSize(buttonSize);
    }

}
