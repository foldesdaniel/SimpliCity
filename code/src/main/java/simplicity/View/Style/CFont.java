package simplicity.View.Style;

import simplicity.Model.GameModel;

import java.awt.*;

/**
 * A class that help to create styled fonts
 * based on the loaded font type
 */
public class CFont {

    /**
     * Create a styled Font
     *
     * @param style font style
     * @param size  font size
     * @return styled Font
     */
    public static Font get(int style, int size) {
        return GameModel.CUSTOM_FONT.deriveFont(style, size);
    }

    /**
     * Create a styled Font
     *
     * @param style font style
     * @return styled Font
     */
    public static Font get(int style) {
        return CFont.get(style, 18);
    }

    /**
     * Create a styled Font
     *
     * @return styled Font
     */
    public static Font get() {
        return CFont.get(Font.PLAIN);
    }

}
