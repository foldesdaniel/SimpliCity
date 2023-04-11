package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopRightBar extends JPanel implements InGameTimeListener {

    private JLabel timeLabel;

    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();

    public TopRightBar(){
        Font font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        timeLabel = new JLabel();
        timeLabel.setFont(font);
        this.add(timeLabel);
        // inGameTime.startInGameTime(InGameSpeeds.NORMAL);
        inGameTime.setInGameTimeListener(this);
        // this.setBackground(new Color(150, 0, 0));
        JButton btn1 = new JButton("⏹");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
            }
        });
        JButton btn2 = new JButton("⏵");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.NORMAL);
            }
        });
        JButton btn3 = new JButton("⏵⏵");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FAST);
            }
        });
        JButton btn4 = new JButton("⏵⏵⏵");
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FASTEST);
            }
        });
        JButton btn5 = new JButton("BRRR");
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
            }
        });
        this.add(btn1);
        this.add(btn2);
        this.add(btn3);
        this.add(btn4);
        this.add(btn5);
    }

    @Override
    public void timeChanged(int inGameYear, int inGameDay, int inGameHour) {
        timeLabel.setText("Year: " + inGameYear + ", day: " + inGameDay + ", hour: " + inGameHour);
    }


}
