package simplicity.View.Style;

import com.formdev.flatlaf.ui.FlatScrollBarUI;

import java.awt.*;

/**
 * Custom styled FlatScrollBarUI for the load menu
 */
public class MenuScrollBarUI extends FlatScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        super.configureScrollBarColors();
        this.thumbColor = new Color(255, 255, 255, 64);
        this.trackColor = new Color(33, 33, 33);
    }
}
