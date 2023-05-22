package simplicity.View.Menu;

import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class MenuComboBox extends JComboBox {
    /**
     * Constructor with initialized values.
     *
     * @param list This ComboBox will contain these elements.
     */
    public MenuComboBox(String[] list) {
        super(list);
        this.setFont(CFont.get());
        Dimension buttonSize = new Dimension(300, 50);
        this.setPreferredSize(buttonSize);
        this.setMinimumSize(buttonSize);
        this.setMaximumSize(buttonSize);
    }
}
