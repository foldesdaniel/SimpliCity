package simplicity.View.Game;

import simplicity.View.Style.CFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

/**
 * A hack to create a label that
 */
public class WrapLabel extends JTextPane {

    private final int baseHeight;

    /**
     * {@code text} defaults to ""
     *
     * @see WrapLabel#WrapLabel(String)
     */
    public WrapLabel() {
        this.setOpaque(false);
        this.setFocusable(false);
        this.baseHeight = this.getPreferredSize().height;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setEditable(false);
        this.setFont(CFont.get(Font.PLAIN, 20));
        StyledDocument doc = this.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    /**
     * Initializes the label
     *
     * @param text initializer string
     */
    public WrapLabel(String text) {
        this();
        this.setText(text);
    }

    /**
     * @return how many lines are in the wrapped text
     */
    public int getLineCount() {
        int totalCharacters = this.getText().length();
        int lineCount = (totalCharacters == 0) ? 1 : 0;
        try {
            int offset = totalCharacters;
            while (offset > 0) {
                offset = Utilities.getRowStart(this, offset) - 1;
                lineCount++;
            }
        } catch (BadLocationException e) {
            // e.printStackTrace();
        }
        return lineCount;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.setPreferredSize(d);
    }

    /**
     * Fits label to size based on the height, line count and given width
     *
     * @param width the width
     */
    public void fitHeight(int width) {
        Dimension d = new Dimension(width, this.baseHeight * this.getLineCount());
        this.setSize(d);
        this.setMaximumSize(d);
    }

}
