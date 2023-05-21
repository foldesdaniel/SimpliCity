package simplicity.View.Style;

import com.formdev.flatlaf.ui.FlatScrollBarUI;

import java.awt.*;

/**
 * Custom styled FlatScrollBarUI for the control panel
 */
public class ScrollBarUI extends FlatScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
        this.thumbColor = new Color(255, 255, 255, 16);
        this.trackColor = new Color(0, 0, 0, 0);
    }
}
