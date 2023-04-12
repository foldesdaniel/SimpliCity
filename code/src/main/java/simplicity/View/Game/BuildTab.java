package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.*;
import simplicity.Model.Zones.*;

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
        this.buildables.add(new BuildTile(new Residential(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Service(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Industrial(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Road(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Forest(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Police(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new Stadium(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new School(GameModel.NO_SELECTION)));
        this.buildables.add(new BuildTile(new University(GameModel.NO_SELECTION)));
    }

    public void init() {
        this.removeAll();
        //Font f = new Font("Arial", Font.BOLD, 16);
        JLabel zonesTitle = new JLabel("Select buildings:");
        zonesTitle.setFont(fontBold);
        zonesTitle.setHorizontalAlignment(JLabel.CENTER);
        zonesTitle.setOpaque(true);
        zonesTitle.setBackground(new Color(255, 50, 50));
        this.setBackground(new Color(255, 200, 200));
        updateBuildables();
        this.add(zonesTitle);
        this.add(this.buildables);
    }

}
