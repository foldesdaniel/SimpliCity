package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;

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
        GridLayout layout = new GridLayout(0, 3);
        // layout.setVgap(4);
        // layout.setHgap(4);
        this.buildables.setLayout(layout);
        this.init();
    }

    public void updateBuildables(){
        this.buildables.add(new BuildTile(Residential.class));
        this.buildables.add(new BuildTile(Service.class));
        this.buildables.add(new BuildTile(Industrial.class));
        this.buildables.add(new BuildTile(Road.class));
        this.buildables.add(new BuildTile(Forest.class));
        this.buildables.add(new BuildTile(Police.class));
        this.buildables.add(new BuildTile(Stadium.class));
        this.buildables.add(new BuildTile(School.class));
        this.buildables.add(new BuildTile(University.class));
    }

    public void init() {
        this.removeAll();
        //Font f = new Font("Arial", Font.BOLD, 16);
        JLabel zonesTitle = new JLabel("Select buildings:");
        zonesTitle.setFont(fontBold);
        zonesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        updateBuildables();
        this.add(zonesTitle);
        this.add(this.buildables);
    }

}
