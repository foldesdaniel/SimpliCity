package simplicity.View.Game;

import simplicity.Model.GameModel;

import javax.swing.*;
import java.awt.*;

public class TopLeftBar extends JPanel {

    // will contain save button, etc

    public TopLeftBar(){
        JLabel tempLabel = new JLabel("Top left panel");
        Font font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        tempLabel.setFont(font);
        this.add(tempLabel);
        this.setBackground(new Color(50, 50, 50));
    }

}
