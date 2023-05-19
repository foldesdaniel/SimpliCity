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

    private JLabel nameLabel;
    private JLabel timeLabel;

    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();

    public TopRightBar(){
        Font font = CFont.get();
        nameLabel = new JLabel(GameModel.getInstance().getCityName());
        nameLabel.setFont(CFont.get(Font.BOLD, 20));
        this.setLayout(new BorderLayout());
        JPanel nameContainer = new JPanel();
        nameContainer.setLayout(new BoxLayout(nameContainer, BoxLayout.X_AXIS));
        nameContainer.add(Box.createRigidArea(new Dimension(16, 0)));
        nameContainer.add(nameLabel);
        nameContainer.setOpaque(false);
        this.add(nameContainer, BorderLayout.LINE_START);
        timeLabel = new JLabel();
        timeLabel.setFont(CFont.get(Font.PLAIN,20));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(timeLabel);
        inGameTime.setInGameTimeListener(this);
        JPanel timeControls = new JPanel();
        timeControls.setLayout(new BoxLayout(timeControls, BoxLayout.X_AXIS));
        timeControls.setOpaque(false);
        JButton btn0 = new JButton("Finances");
        btn0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bts = GameModel.getInstance().getFinance().builtToString();
                String ysts = GameModel.getInstance().getFinance().yearlySpendToString();
                String its = GameModel.getInstance().getFinance().incomeToString();
                if(bts.length() == 0) bts = "No data on record\n";
                if(ysts.length() == 0) ysts = "No data on record\n";
                if(its.length() == 0) its = "No data on record\n";
                String bigStr = "Building expenses:\n" + bts + "\nYearly maintenance expenses:\n" + ysts + "\nIncome:\n" + its;
                GameModel.showMessage("Finances", bigStr);
            }
        });
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
        JButton btn6 = new JButton("Bored");
        btn6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.ULTRASUPERSONIC_DEV_ONLY);
            }
        });
        btn0.setFont(font);
        btn2.setFont(font);
        btn3.setFont(font);
        btn4.setFont(font);
        btn5.setFont(font);
        btn6.setFont(font);
        Dimension btnSize = new Dimension(56,28);
        btn1.setPreferredSize(btnSize);
        btn2.setPreferredSize(btnSize);
        btn3.setPreferredSize(btnSize);
        btn4.setPreferredSize(btnSize);
        btn5.setPreferredSize(btnSize);
        timeControls.add(btn0);
        timeControls.add(btn1);
        timeControls.add(btn2);
        timeControls.add(btn3);
        timeControls.add(btn4);
        timeControls.add(btn5);
        timeControls.add(btn6);
        this.add(timeControls, BorderLayout.LINE_END);
        timeChanged(0,0,0);
    }

    @Override
    public void timeChanged(int inGameYear, int inGameDay, int inGameHour) {
        java.time.LocalDate ld = java.time.Year.of(2023+inGameYear).atDay(inGameDay+1);
        timeLabel.setText(ld.toString().replace("-", ".") + ". " + String.format("%02d", inGameHour) + ":00");
    }


}
