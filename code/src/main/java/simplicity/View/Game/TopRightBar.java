package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.View.Style.CFont;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TopRightBar extends JPanel implements InGameTimeListener {

    private JLabel timeLabel;

    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();

    public TopRightBar(){
        Font font = CFont.get();
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
        JButton btn2 = new JButton("x1");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.NORMAL);
            }
        });
        JButton btn3 = new JButton("x2");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FAST);
            }
        });
        JButton btn4 = new JButton("x3");
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FASTEST);
            }
        });
        JButton btn5 = new JButton("DEV");
        btn5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);
            }
        });
        btn2.setFont(font);
        btn3.setFont(font);
        btn4.setFont(font);
        btn5.setFont(font);
        Dimension btnSize = new Dimension(56,28);
        btn1.setPreferredSize(btnSize);
        btn2.setPreferredSize(btnSize);
        btn3.setPreferredSize(btnSize);
        btn4.setPreferredSize(btnSize);
        btn5.setPreferredSize(btnSize);
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
