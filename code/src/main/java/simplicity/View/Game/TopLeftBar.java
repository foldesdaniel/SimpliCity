package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class TopLeftBar extends JPanel {

    private ArrayList<StartStopGameListener> stopGameListeners;

    public TopLeftBar(){
        this.stopGameListeners = new ArrayList<>();
        this.setLayout(new GridLayout(1,2));
        JButton backToMenuBtn = new JButton("Main menu");
        JButton saveBtn = new JButton("Save game");
        backToMenuBtn.setFont(CFont.get());
        saveBtn.setFont(CFont.get());
        backToMenuBtn.addActionListener((e) -> {
            int answer = GameModel.showDialog("Save game?", "Would you like to save your current progress?");
            switch(answer){
                case JOptionPane.YES_OPTION:
                    GameModel.showMessage("Not yet", "Feature not implemented yet");
                    for(StartStopGameListener l : stopGameListeners) l.onGameStop();
                    break;
                case JOptionPane.NO_OPTION:
                    for(StartStopGameListener l : stopGameListeners) l.onGameStop();
                    break;
                default:
                    break;
            }
        });
        saveBtn.addActionListener((e) -> {

        });
        this.add(backToMenuBtn);
        this.add(saveBtn);
        this.setBackground(new Color(50, 50, 50));
    }

    public void addStopGameListener(StartStopGameListener stopGameListener){
        this.stopGameListeners.add(stopGameListener);
    }

}
