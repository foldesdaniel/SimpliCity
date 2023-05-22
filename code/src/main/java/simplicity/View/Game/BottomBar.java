package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MoralChangeListener;
import simplicity.Model.Listeners.PeopleChangeListener;
import simplicity.Model.Listeners.WealthChangeListener;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

/**
 * The bottom part of the game area.
 * It is used to display information about the population,
 * overall mood/moral and the financial situation of the city
 */
public class BottomBar extends JPanel implements MoralChangeListener, PeopleChangeListener, WealthChangeListener {

    // will implement listeners to change info about civilian count, moral, etc
    private final JLabel personCountLabel;
    private final JLabel moralLabel;
    private final JLabel wealthLabel;

    public BottomBar() {
        this.personCountLabel = new JLabel("Population: " + GameModel.getInstance().getPeople().size() + " |");
        this.moralLabel = new JLabel("Overall moral: " + GameModel.getInstance().getCityMood() + " |");
        this.wealthLabel = new JLabel("Wealth: " + GameModel.getInstance().getCurrentWealth());

        Font font = CFont.get();
        this.personCountLabel.setFont(font);
        this.moralLabel.setFont(font);
        this.wealthLabel.setFont(font);

        Color textColor = new Color(0, 255, 0);
        this.personCountLabel.setForeground(textColor);
        this.moralLabel.setForeground(textColor);
        this.wealthLabel.setForeground(textColor);
        this.setBackground(new Color(20, 20, 20));

        this.add(personCountLabel);
        this.add(moralLabel);
        this.add(wealthLabel);

        GameModel.getInstance().addMoralChangeListener(this);
        GameModel.getInstance().addPeopleChangeListener(this);
        GameModel.getInstance().addWealthChangeListener(this);
    }

    @Override
    public void onPeopleCountChange() {
        personCountLabel.setText("Population: " + GameModel.getInstance().getPeople().size() + " |");
    }

    @Override
    public void onMoralChanged() {
        moralLabel.setText("Overall moral: " + GameModel.getInstance().getCityMood() + " |");
    }

    @Override
    public void onWealthChange() {
        wealthLabel.setText("Wealth: " + GameModel.getInstance().getCurrentWealth());
    }

}
