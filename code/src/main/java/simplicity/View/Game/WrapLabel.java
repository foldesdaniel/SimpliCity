package simplicity.View.Game;

import simplicity.View.Style.CFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;

public class WrapLabel extends JTextPane {

    private int baseHeight;

    public WrapLabel() {
        this.setOpaque(false);
        this.setFocusable(false);
        //this.setBackground(new Color(0,0,255));
        this.baseHeight = this.getPreferredSize().height;
        this.setBorder(new EmptyBorder(0, 0, 0, 0));
        this.setEditable(false);
        this.setFont(CFont.get(Font.PLAIN, 20));
        //this.setLineWrap(true);
        //this.setWrapStyleWord(true);
        StyledDocument doc = this.getStyledDocument();
        SimpleAttributeSet center = new SimpleAttributeSet();
        StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
        doc.setParagraphAttributes(0, doc.getLength(), center, false);
    }

    public WrapLabel(String text) {
        this();
        this.setText(text);
    }

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

    public void setBaseHeight(int baseHeight) {
        this.baseHeight = baseHeight;
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        this.setPreferredSize(d);
    }

    public void fitHeight(int width) {
        Dimension d = new Dimension(width, this.baseHeight * this.getLineCount());
        this.setSize(d);
        this.setMaximumSize(d);
    }

}
