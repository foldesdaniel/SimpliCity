package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;

import javax.swing.*;
import java.awt.*;

public class InfoTab extends JPanel {

    private final Font font; // temporary

    public InfoTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
    }

    public void init(){
        this.removeAll();
        JLabel tempLabel = new JLabel("Empty selection");
        tempLabel.setFont(font);
        this.add(tempLabel);
    }

    public void updateInfo(Placeable f) {
        this.removeAll();
        if (f == null) {
            this.init();
        } else {
            JLabel tempLabel = new JLabel("Info: " + f.toString());
            tempLabel.setFont(font);
            this.add(tempLabel);
        }
    }

}
