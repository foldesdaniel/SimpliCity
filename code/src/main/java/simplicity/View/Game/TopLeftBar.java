package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.View.Style.CFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

/**
 * The top left part of the game area.
 * Contains buttons allowing you to save and/or exit the game
 */
public class TopLeftBar extends JPanel {

    private final ArrayList<StartStopGameListener> stopGameListeners;

    public TopLeftBar() {
        this.stopGameListeners = new ArrayList<>();
        this.setLayout(new GridLayout(1, 2));
        JButton backToMenuBtn = new JButton("Main menu");
        JButton saveBtn = new JButton("Save game");
        backToMenuBtn.setFont(CFont.get());
        backToMenuBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
        saveBtn.setFont(CFont.get());
        saveBtn.setBorder(new EmptyBorder(0, 0, 0, 0));
        backToMenuBtn.addActionListener((e) -> {
            int answer = GameModel.showDialog("Save game?", "Would you like to save your current progress?");
            switch (answer) {
                case JOptionPane.YES_OPTION -> {
                    GameModel.getInstance().saveGame();
                    for (StartStopGameListener l : stopGameListeners) l.onGameStop();
                }
                case JOptionPane.NO_OPTION -> {
                    for (StartStopGameListener l : stopGameListeners) l.onGameStop();
                }
                default -> {
                }
            }
        });
        saveBtn.addActionListener((e) -> {
            GameModel.getInstance().saveGame();
            GameModel.showMessage("Success", "Game has been saved");
        });
        this.add(backToMenuBtn);
        this.add(saveBtn);
        this.setBackground(new Color(50, 50, 50));
    }

    public void addStopGameListener(StartStopGameListener stopGameListener) {
        this.stopGameListeners.add(stopGameListener);
    }

}
