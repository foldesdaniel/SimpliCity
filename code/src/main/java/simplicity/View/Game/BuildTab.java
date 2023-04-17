package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class BuildTab extends JPanel {

    private final JPanel buildables;

    public BuildTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.buildables = new JPanel();
        GridLayout layout = new GridLayout(0, 3);
        // layout.setVgap(4);
        // layout.setHgap(4);
        this.buildables.setLayout(layout);
        this.init();
    }

    public void init() {
        this.removeAll();
        //Font f = new Font("Arial", Font.BOLD, 16);
        JLabel zonesTitle = new JLabel("Select buildings:");
        zonesTitle.setFont(CFont.get(Font.BOLD, 24));
        zonesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.buildables.add(new BuildTile(Residential.class));
        this.buildables.add(new BuildTile(Service.class));
        this.buildables.add(new BuildTile(Industrial.class));
        this.buildables.add(new BuildTile(Road.class));
        this.buildables.add(new BuildTile(Forest.class));
        this.buildables.add(new BuildTile(Police.class));
        this.buildables.add(new BuildTile(Stadium.class));
        this.buildables.add(new BuildTile(School.class));
        this.buildables.add(new BuildTile(University.class));

        this.add(zonesTitle);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.buildables);
    }

}
