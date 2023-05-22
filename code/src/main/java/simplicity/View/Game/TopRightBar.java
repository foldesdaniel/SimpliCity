package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.View.Style.CFont;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The top right part of the game area.
 * Contains info about the current
 * city, and also time controls
 */
public class TopRightBar extends JPanel implements InGameTimeListener {

    private final InGameTime inGameTime = InGameTimeManager.getInstance().getInGameTime();
    private final JLabel nameLabel;
    private final JLabel timeLabel;

    public TopRightBar() {
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
        timeLabel.setFont(CFont.get(Font.PLAIN, 20));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(timeLabel);
        inGameTime.setInGameTimeListener(this);
        JPanel timeControls = new JPanel();
        timeControls.setLayout(new BoxLayout(timeControls, BoxLayout.X_AXIS));
        timeControls.setOpaque(false);
        JButton btn0 = new JButton("Fin.");
        btn0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bts = GameModel.getInstance().getFinance().builtToString().replace("\n", "<br>");
                String ysts = GameModel.getInstance().getFinance().yearlySpendToString().replace("\n", "<br>");
                String its = GameModel.getInstance().getFinance().incomeToString().replace("\n", "<br>");
                if (bts.length() == 0) bts = "No data on record<br>";
                if (ysts.length() == 0) ysts = "No data on record<br>";
                if (its.length() == 0) its = "No data on record<br>";
                String bigStr = "<html># " + formatTime(inGameTime) + "<br><br><u>Building expenses:</u><br>" + bts + "<br><u>Yearly maintenance expenses:</u><br>" + ysts + "<br><u>Income:</u><br>" + its + "</html>";
                JLabel finLabel = new JLabel(bigStr);
                finLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                finLabel.setHorizontalAlignment(SwingConstants.CENTER);
                finLabel.setFont(CFont.get(Font.PLAIN, 20));
                finLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
                JDialog jd = new JDialog();
                JPanel jdPanel = new JPanel();
                jdPanel.setOpaque(false);
                jdPanel.setLayout(new BoxLayout(jdPanel, BoxLayout.Y_AXIS));
                jdPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
                JScrollPane scrollPane = new JScrollPane(finLabel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                scrollPane.getVerticalScrollBar().setUnitIncrement(8);
                scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
                scrollPane.getViewport().setAlignmentX(Component.CENTER_ALIGNMENT);
                jdPanel.add(scrollPane);
                jd.add(jdPanel);
                jd.pack();
                jd.setLocationRelativeTo(null);
                jd.setVisible(true);
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
        Dimension btnSize = new Dimension(56, 28);
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
        timeChanged(0, 0, 0);
    }

    /**
     * Formats given time to a readable format
     *
     * @param inGameYear in game year
     * @param inGameDay  in game day
     * @param inGameHour in game hour
     * @return formatted time
     */
    private String formatTime(int inGameYear, int inGameDay, int inGameHour) {
        java.time.LocalDate ld = java.time.Year.of(2023 + inGameYear).atDay(inGameDay + 1);
        return ld.toString().replace("-", ".") + ". " + String.format("%02d", inGameHour) + ":00";
    }

    /**
     * Formats {@link InGameTime} to a readable format
     *
     * @param igt InGameTime instance
     * @return formatted time
     */
    private String formatTime(InGameTime igt) {
        return formatTime(igt.getInGameYear(), igt.getInGameDay(), igt.getInGameHour());
    }

    @Override
    public void timeChanged(int inGameYear, int inGameDay, int inGameHour) {
        timeLabel.setText(formatTime(inGameYear, inGameDay, inGameHour));
    }


}
