package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;

public class TopLeftBar extends JPanel {

    // will contain save button, etc

    public TopLeftBar(){
        this.setLayout(new GridLayout(1,2));
        JButton backToMenuBtn = new JButton("Main menu");
        JButton saveBtn = new JButton("Save game");
        backToMenuBtn.setFont(CFont.get());
        saveBtn.setFont(CFont.get());
        this.add(backToMenuBtn);
        this.add(saveBtn);
        this.setBackground(new Color(50, 50, 50));
    }

}
