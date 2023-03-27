package simplicity.View.Game;

import simplicity.Model.Education.School;
import simplicity.Model.Education.University;
import simplicity.Model.Game.FieldData;
import simplicity.Model.Game.FieldType;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.GameTime.InGameTimeManager;
import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.Model.Person.Person;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements FieldClickListener, InGameTimeListener {

    private final JMenuBar menuBar;
    private final JPanel topLeftBar;
    private final JPanel topRightBar;
    private final JLabel timeLabel;
    private final ControlPanel controlPanel;
    private final PlayingFieldView playingField;
    private final JPanel bottomBar;
    private final InGameTimeManager inGameTimeManager = InGameTimeManager.getInstance();
    private final InGameTime inGameTime;
    private final GridBagConstraints gbc;

    public GamePanel(int windowWidth, int windowHeight) {
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        this.setSize(windowSize);
        this.setPreferredSize(windowSize);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        menuBar = new JMenuBar();
        topLeftBar = new JPanel();
        topRightBar = new JPanel();
        controlPanel = new ControlPanel();
        playingField = new PlayingFieldView(20, 20);
        bottomBar = new JPanel();
        timeLabel = new JLabel();
        inGameTime = inGameTimeManager.getInGameTime();
//        inGameTime = new InGameTime();
        inGameTime.setInGameTimeListener(this);
        inGameTime.startInGameTime(InGameSpeeds.ULTRASONIC_DEV_ONLY);

        this.gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        mainPanel.setLayout(layout);
        topLeftBar.add(new JLabel("Top left panel"));
        topLeftBar.setBackground(new Color(255, 0, 0));
        mainPanel.add(topLeftBar, changeGbc(
                0, 0,
                1, 1,
                -1, 0
        ));
        topRightBar.add(timeLabel);
        topRightBar.setBackground(new Color(150, 0, 0));
        JButton btn1 = new JButton("Stop");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
            }
        });
        JButton btn2 = new JButton(">");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.NORMAL);
            }
        });
        JButton btn3 = new JButton(">>");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FAST);
            }
        });
        JButton btn4 = new JButton(">>>");
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FASTEST);
            }
        });
        topRightBar.add(btn1);
        topRightBar.add(btn2);
        topRightBar.add(btn3);
        topRightBar.add(btn4);
        mainPanel.add(topRightBar, changeGbc(
                0, 1,
                1, 1,
                0.7, 0
        ));
        mainPanel.add(controlPanel, changeGbc(
                1, 0,
                2, 1,
                -1, 1
        ));
        playingField.setInfoListener(this);
        mainPanel.add(playingField, changeGbc(
                1, 1,
                1, 1,
                0.7, 1
        ));
        bottomBar.add(new JLabel("Bottom bar"));
        bottomBar.setBackground(new Color(255, 0, 0));
        mainPanel.add(bottomBar, changeGbc(
                2, 1,
                1, 1,
                1, 0
        ));
        mainPanel.setSize(windowSize);
        mainPanel.setPreferredSize(windowSize);
        mainPanel.setBackground(new Color(255, 0, 0, 150));

        /* menuBar.setPreferredSize(new Dimension(menuBar.getWidth(), menuBar.getHeight()));
        topLeftBar.setPreferredSize(new Dimension(topLeftBar.getWidth(), topLeftBar.getHeight()));
        topRightBar.setPreferredSize(new Dimension(topRightBar.getWidth(), topRightBar.getHeight())); */
        controlPanel.setPreferredSize(new Dimension((int) Math.round(this.getWidth() * 0.25), controlPanel.getHeight()));
        /* playingField.setPreferredSize(new Dimension(playingField.getWidth(), playingField.getHeight()));
        bottomBar.setPreferredSize(new Dimension(bottomBar.getWidth(), bottomBar.getHeight())); */

        this.add(mainPanel);
        this.setBackground(new Color(0, 255, 0));
        this.repaint();

        try {
            School s1 = new School(new Point(1, 1));
            Thread.sleep(3000);
            University u1 = new University(new Point(1, 1));
            Person p1 = new Person();
//            System.out.println(p1.getBorn()[2]);
            p1.goToSchool(s1);
//            Thread.sleep(2000);
            Person p2 = new Person();
//            System.out.println(p2.getBorn()[2]);
            p2.goToSchool(u1);
//            Thread.sleep(2000);
            Person p3 = new Person();
//            System.out.println(p3.getBorn()[2]);
//            p3.goToSchool(u1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
//        for (int i = 0; i < s1.getArrivalDates().size(); i++) {
//            for (int j = 0; j < 3; j++) {
//                System.out.println(s1.getArrivalDates().get(i)[j] + " ");
//            }
//            System.out.println("\n");
//        }
    }

    public static GridBagConstraints changeGbc(GridBagConstraints gbc, int row, int col, int rowSpan, int colSpan, double weightX, double weightY) {
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipady = 0;
        gbc.weightx = weightX > 0 ? weightX : 0;
        gbc.weighty = weightY > 0 ? weightY : 0;
        gbc.gridwidth = colSpan;
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridheight = rowSpan;
        return gbc;
    }

    @Override
    public void fieldClicked(FieldData f) {
        controlPanel.updateInfo(f);
    }

    @Override
    public void timeChanged(int inGameYear, int inGameDay, int inGameHour) {
        timeLabel.setText("Year: " + inGameYear + ", day: " + inGameDay + ", hour: " + inGameHour);
    }

    private GridBagConstraints changeGbc(int row, int col, int rowSpan, int colSpan, double weightX, double weightY) {
        return GamePanel.changeGbc(gbc, row, col, rowSpan, colSpan, weightX, weightY);
    }

}
