package simplicity.View.Style;

import javax.swing.border.Border;
import java.awt.*;

/**
 * Border that creates the illusion
 * of a low quality inset shadow
 */
public class InsetShadowBorder implements Border {

    private final int gap;
    private int size;

    public InsetShadowBorder(int size, int gap) {
        this.size = size;
        this.gap = gap;
    }

    public InsetShadowBorder(int size) {
        this(size, 0);
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (this.size > height || this.size > width) this.size = Math.min(width, height);
        for (int i = 0; i < this.size; i++) {
            int alpha = 100 - (int) Math.round(i * (100 / (double) this.size));
            g.setColor(new Color(0, 0, 0, alpha));
            g.drawLine(i, i, width, i);
            g.drawLine(i, i + 1, i, height);
        }
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(gap, gap, gap, gap);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

}
