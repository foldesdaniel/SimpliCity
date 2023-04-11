package simplicity.View.Game;

import simplicity.Model.GameModel;

import javax.swing.*;
import java.awt.*;

public class BuildTab extends JPanel {

    private final Font font; // temporary
    private final Font fontBold; // temporary
    private final JPanel buildables;

    public BuildTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        this.fontBold = GameModel.CUSTOM_FONT.deriveFont(Font.BOLD, 20);
        this.buildables = new JPanel();
        this.init();
    }

    public void updateBuildables(){
        this.buildables.add(new JLabel("hi1"));
        this.buildables.add(new JLabel("hi2"));
        this.buildables.add(new JLabel("hi3"));
        this.buildables.add(new JLabel("hi4"));
    }

    public void init() {
        this.removeAll();
        //Font f = new Font("Arial", Font.BOLD, 16);
        JLabel zonesTitle = new JLabel("Select zones:");
        JLabel buildingsTitle = new JLabel("Available buildings:");
        zonesTitle.setFont(fontBold);
        buildingsTitle.setFont(fontBold);
        zonesTitle.setHorizontalAlignment(JLabel.CENTER);
        buildingsTitle.setHorizontalAlignment(JLabel.CENTER);
        zonesTitle.setOpaque(true);
        buildingsTitle.setOpaque(true);
        zonesTitle.setBackground(new Color(255,200, 200));
        buildingsTitle.setBackground(new Color(255,200, 200));
        updateBuildables();
        this.add(zonesTitle);
        this.add(this.buildables);
        this.add(buildingsTitle);
    }

}
