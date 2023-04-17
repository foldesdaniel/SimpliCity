package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class TopLeftBar extends JPanel {

    // will contain save button, etc

    public TopLeftBar(){
        JLabel tempLabel = new JLabel("Top left panel");
        tempLabel.setFont(CFont.get());
        this.add(tempLabel);
        this.setBackground(new Color(50, 50, 50));
    }

}
