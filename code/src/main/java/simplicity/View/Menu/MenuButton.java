package simplicity.View.Menu;

import javax.swing.*;

public class MenuButton extends JButton {
    /**
     * Constructor with initialized label.
     *
     * @param label
     *            This button will display this text.
     */
    public MenuButton(String label) {
        this.setText(label);
    }

    /**
     * Constructor.
     */
    public MenuButton() {
        this.setText("");
    }
}
