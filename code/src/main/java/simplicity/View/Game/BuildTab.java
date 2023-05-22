package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Listeners.ModeChangeListener;
import simplicity.Model.Placeables.Forest;
import simplicity.Model.Placeables.Police;
import simplicity.Model.Placeables.Road;
import simplicity.Model.Placeables.Stadium;
import simplicity.Model.Placeables.Zones.Industrial;
import simplicity.Model.Placeables.Zones.Residential;
import simplicity.Model.Placeables.Zones.Service;
import simplicity.View.Style.CFont;
import simplicity.View.Style.WrapLayout;

import javax.swing.*;
import java.awt.*;

/**
 * A panel within the control panel that displays all
 * the available buildings and zones, and allows you
 * to enter/exit both build and delete mode.
 */
public class BuildTab extends JPanel implements ModeChangeListener {

    private static final String startDeleteText = "Enter delete mode";
    private static final String stopDeleteText = "Exit delete mode";
    private final JPanel zones;
    private final JPanel buildings;
    private final JPanel other;
    private JButton exitBuildBtn;
    private JButton deleteBtn;

    public BuildTab() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.zones = new JPanel();
        this.buildings = new JPanel();
        this.other = new JPanel();
        this.zones.setLayout(new WrapLayout(1, 4, 4));
        this.buildings.setLayout(new WrapLayout(1, 4, 4));
        this.other.setLayout(new WrapLayout(1, 4, 4));
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

        this.zones.add(new BuildTile(Residential.class));
        this.zones.add(new BuildTile(Service.class));
        this.zones.add(new BuildTile(Industrial.class));
        this.buildings.add(new BuildTile(Road.class));
        this.buildings.add(new BuildTile(Forest.class));
        this.buildings.add(new BuildTile(Police.class));
        this.buildings.add(new BuildTile(Stadium.class));
        this.buildings.add(new BuildTile(School.class));
        this.buildings.add(new BuildTile(University.class));

        exitBuildBtn = new JButton();
        exitBuildBtn.addActionListener((e) -> GamePanel.stopPlacing());
        exitBuildBtn.setText("Exit building mode");
        exitBuildBtn.setFont(CFont.get());
        exitBuildBtn.setEnabled(false);
        deleteBtn = new JButton();
        deleteBtn.setText(startDeleteText);
        deleteBtn.addActionListener((e) -> {
            if (GamePanel.isDeleteMode()) {
                GamePanel.stopDeleteMode();
                //deleteBtn.setText(startDeleteText);
            } else {
                GamePanel.startDeleteMode();
                //deleteBtn.setText(stopDeleteText);
            }
        });
        deleteBtn.setFont(CFont.get());
        this.other.add(exitBuildBtn);
        this.other.add(deleteBtn);
        this.other.add(Box.createRigidArea(new Dimension(0, 8)));

        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(zonesTitle);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.zones);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(buildingsTitle);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.buildings);
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(this.other);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.zones.setMaximumSize(this.zones.getLayout().preferredLayoutSize(this.zones));
        this.buildings.setMaximumSize(this.buildings.getLayout().preferredLayoutSize(this.buildings));
    }

    @Override
    public void onBuildModeChanged(boolean on) {
        exitBuildBtn.setEnabled(on);
    }

    @Override
    public void onDeleteModeChanged(boolean on) {
        if (on) {
            deleteBtn.setText(stopDeleteText);
        } else {
            deleteBtn.setText(startDeleteText);
        }
    }
}
