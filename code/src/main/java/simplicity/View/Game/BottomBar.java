package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;

import javax.swing.*;
import java.awt.*;

public class BottomBar extends JPanel implements MoralChangeListener, PeopleChangeListener {

    // will implement listeners to change info about civilian count, moral, etc
    private final JLabel personCountLabel;
    private final JLabel moralLabel;
    private final JLabel separatorLabel;
    private final GameModel model;

    public BottomBar() {
        this.model = GameModel.getInstance();
        this.model.addMoralChangeListener(this);
        this.model.addPeopleChangeListener(this);
        this.personCountLabel = new JLabel("Person count: 0");
        this.moralLabel = new JLabel("Overall moral: 10");
        this.separatorLabel = new JLabel(" | ");

        Font font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        this.personCountLabel.setFont(font);
        this.moralLabel.setFont(font);
        this.separatorLabel.setFont(font);

        Color textColor = new Color(0, 255, 0);
        this.personCountLabel.setForeground(textColor);
        this.moralLabel.setForeground(textColor);
        this.separatorLabel.setForeground(textColor);
        this.setBackground(new Color(20, 20, 20));

        this.add(personCountLabel);
        this.add(separatorLabel);
        this.add(moralLabel);
        //this.setLayout(new FlowLayout());
    }

    @Override
    public void onMoralChanged() {
        moralLabel.setText("Overall moral: " + model.getCityMood());
    }

    @Override
    public void onPeopleCountChange() {
        personCountLabel.setText("Person count: " + model.countPeople());
    }
}
