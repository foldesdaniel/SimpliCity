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
        Dimension buttonSize = new Dimension(300, 40);
        this.setPreferredSize(buttonSize);
        this.setMinimumSize(buttonSize);
        this.setMaximumSize(buttonSize);
        this.transferFocus();
        MenuTextField self = this;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final JComponent component = self;
                component.requestFocusInWindow();
            }
        });
    }
}
