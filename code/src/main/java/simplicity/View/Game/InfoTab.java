package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class InfoTab extends JPanel {

    public InfoTab(){
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.init();
    }

    public void init(){
        this.removeAll();
        JLabel tempLabel = new JLabel("Empty selection");
        tempLabel.setFont(CFont.get(Font.PLAIN, 20));
        this.add(tempLabel);
    }

    public void updateInfo(Placeable f) {
        this.removeAll();
        if (f == null) {
            this.init();
        } else {
            JLabel tempLabel = new JLabel("Info: " + f.toString());
            tempLabel.setFont(CFont.get());
            this.add(tempLabel);
        }
    }

}
