package View.Menu;

import javax.swing.*;

public class MenuLabel extends JLabel {
    /**
     * Constructor with initialized label.
     *
     * @param label
     *            This label will display this text.
     */
    public MenuLabel(String label) {
        this.setText(label);
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }

    /**
     * Constructor.
     */
    public MenuLabel() {
        this.setText("");
        this.setVerticalAlignment(SwingConstants.CENTER);
        this.setHorizontalAlignment(SwingConstants.CENTER);
    }
}
