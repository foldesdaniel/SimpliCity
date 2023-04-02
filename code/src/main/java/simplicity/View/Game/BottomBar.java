package simplicity.View.Game;

import simplicity.Model.GameModel;

import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel {

    // will implement listeners to change info about civilian count, moral, etc
    private final JLabel personCountLabel;
    private final JLabel moralLabel;
    private final JLabel separatorLabel;

    public BottomBar() {
        personCountLabel = new JLabel("Person count: 0");
        moralLabel = new JLabel("Overall moral: 10");
        separatorLabel = new JLabel(" | ");

        Font font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        personCountLabel.setFont(font);
        moralLabel.setFont(font);
        separatorLabel.setFont(font);

        Color textColor = new Color(0, 255, 0);
        personCountLabel.setForeground(textColor);
        moralLabel.setForeground(textColor);
        separatorLabel.setForeground(textColor);
        this.setBackground(new Color(20, 20, 20));

        this.add(personCountLabel);
        this.add(separatorLabel);
        this.add(moralLabel);
        //this.setLayout(new FlowLayout());
    }

    public void setPersonCount(int count) {
        personCountLabel.setText("Person count: " + count);
    }

    public void setMoral(int moral) {
        moralLabel.setText("Overall moral: " + moral);
    }

}
