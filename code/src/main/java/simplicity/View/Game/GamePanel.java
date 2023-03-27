package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;
import simplicity.Model.Listeners.InGameTimeListener;
import simplicity.Model.Game.FieldData;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements FieldClickListener, InGameTimeListener {

    private GridBagConstraints gbc;

    private final JMenuBar menuBar;
    private final JPanel topLeftBar;
    private final JPanel topRightBar;
    private final JLabel timeLabel;
    private final ControlPanel controlPanel;
    private final PlayingFieldView playingField;
    private final JPanel bottomBar;

    private final InGameTime inGameTime;

    public GamePanel() {
        Dimension windowSize = new Dimension(GameWindow.getWindowWidth(), GameWindow.getWindowHeight());
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
        inGameTime = new InGameTime();
        inGameTime.setInGameTimeListener(this);
        inGameTime.startInGameTime(InGameSpeeds.NORMAL);

        this.gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        mainPanel.setLayout(layout);
        topLeftBar.add(new JLabel("Top left panel"));
        topLeftBar.setBackground(new Color(50, 50, 50));
        mainPanel.add(topLeftBar, changeGbc(
            0, 0,
            1, 1,
            -1, 0
        ));
        topRightBar.add(timeLabel);
        // topRightBar.setBackground(new Color(150, 0, 0));
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
        JLabel bottomTestLabel = new JLabel("Bottom bar");
        bottomTestLabel.setFont(GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18));
        bottomTestLabel.setForeground(Color.GREEN);
        bottomBar.add(bottomTestLabel);
        bottomBar.setBackground(new Color(20, 20, 20));
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
        controlPanel.setPreferredSize(new Dimension((int)Math.round(this.getWidth() * 0.25), controlPanel.getHeight()));
        /* playingField.setPreferredSize(new Dimension(playingField.getWidth(), playingField.getHeight()));
        bottomBar.setPreferredSize(new Dimension(bottomBar.getWidth(), bottomBar.getHeight())); */

        this.add(mainPanel);
        this.setBackground(new Color(0, 255, 0));
        this.repaint();
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

}
