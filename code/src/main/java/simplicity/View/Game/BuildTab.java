package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Placeables.*;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class BuildTab extends JPanel {

    private final JPanel zones;
    private final JPanel buildings;

    public BuildTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.zones = new JPanel();
        this.buildings = new JPanel();
        GridLayout layout = new GridLayout(0, 3);
        // layout.setVgap(4);
        // layout.setHgap(4);
        this.zones.setLayout(layout);
        this.buildings.setLayout(layout);
        this.init();
    }

    public void init() {
        this.removeAll();
        JLabel zonesTitle = new JLabel("Select zones:");
        zonesTitle.setFont(CFont.get(Font.BOLD, 24));
        zonesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel buildingsTitle = new JLabel("Select buildings:");
        buildingsTitle.setFont(CFont.get(Font.BOLD, 24));
        buildingsTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        BuildTile bt = new BuildTile(Residential.class);
        this.zones.add(bt);
        this.zones.add(new BuildTile(Service.class));
        this.zones.add(new BuildTile(Industrial.class));
        this.buildings.add(new BuildTile(Road.class));
        this.buildings.add(new BuildTile(Forest.class));
        this.buildings.add(new BuildTile(Police.class));
        this.buildings.add(new BuildTile(Stadium.class));
        this.buildings.add(new BuildTile(School.class));
        this.buildings.add(new BuildTile(University.class));

        this.add(zonesTitle);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.zones);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(buildingsTitle);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.buildings);
    }

}
