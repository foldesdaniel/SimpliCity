package simplicity.View.Menu;

import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class MenuTextField extends JTextField {
    /**
     * Constructor.
     */
    public MenuTextField() {
        this.setFont(CFont.get());
        Dimension buttonSize = new Dimension(300,40); // TODO: media query-like sizing based on resolution
        this.setPreferredSize(buttonSize);
        this.setMinimumSize(buttonSize);
        this.setMaximumSize(buttonSize);
    }
}
