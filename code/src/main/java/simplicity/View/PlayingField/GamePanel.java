package simplicity.View.PlayingField;

import simplicity.Model.Events.FieldClickListener;
import simplicity.Model.GameTime.InGameSpeeds;
import simplicity.Model.GameTime.InGameTime;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GamePanel extends JPanel implements FieldClickListener {

    private GridBagConstraints gbc;

    private final JMenuBar menuBar;
    private final JPanel topLeftBar;
    private final JPanel topRightBar;
    private final JPanel controlPanel;
    private final PlayingFieldView playingField;
    private final JPanel bottomBar;

    private final InGameTime inGameTime;

    public GamePanel(int windowWidth, int windowHeight){
        Dimension windowSize = new Dimension(windowWidth, windowHeight);
        this.setSize(windowSize);
        this.setPreferredSize(windowSize);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.inGameTime = new InGameTime();
        this.inGameTime.startInGameTime(InGameSpeeds.NORMAL);

        menuBar = new JMenuBar();
        topLeftBar = new JPanel();
        topRightBar = new JPanel();
        controlPanel = new JPanel();
        playingField = new PlayingFieldView(20,20);
        bottomBar = new JPanel();

        JMenu menuCategory1 = new JMenu("File");
        JMenuItem menuItem1 = new JMenuItem("opt1");
        JMenuItem menuItem2 = new JMenuItem("opt2");

        menuCategory1.add(menuItem1);
        menuCategory1.add(menuItem2);
        menuBar.add(menuCategory1);
        //this.setJMenuBar(menuBar);

        this.gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        mainPanel.setLayout(layout);
        topLeftBar.add(new JLabel("Top left panel")); topLeftBar.setBackground(new Color(255,0,0));
        mainPanel.add(topLeftBar, changeGbc(
                0, 0,
                1, 1,
                0.3, 0
        ));
        JLabel lbl = new JLabel("Top right panel");
        topRightBar.add(lbl); topRightBar.setBackground(new Color(150,0,0));
        JButton btn1 = new JButton("Stop");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                lbl.setText("stopped");
            }
        });
        JButton btn2 = new JButton("2");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.NORMAL);
                lbl.setText("normal");
            }
        });
        JButton btn3 = new JButton("3");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FAST);
                lbl.setText("fast");
            }
        });
        JButton btn4 = new JButton("4");
        btn4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inGameTime.stopInGameTime();
                inGameTime.startInGameTime(InGameSpeeds.FASTEST);
                lbl.setText("fastest");
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
        controlPanel.add(new JLabel("nothing selected"));
        mainPanel.add(controlPanel, changeGbc(
                1, 0,
                2, 1,
                0.3, 1
        ));
        playingField.addInfoListener(this);
        mainPanel.add(playingField, changeGbc(
                1, 1,
                1, 1,
                0.7, 1
        ));
        bottomBar.add(new JLabel("Bottom bar")); bottomBar.setBackground(new Color(255,0,0));
        mainPanel.add(bottomBar, changeGbc(
                2, 1,
                1, 1,
                1, 0
        ));
        mainPanel.setSize(windowSize);
        mainPanel.setPreferredSize(windowSize);
        mainPanel.setBackground(new Color(255, 0, 0, 150));

        //this.setLayout(new FlowLayout());
        this.add(mainPanel);
        this.setBackground(new Color(0, 255, 0));
    }

    @Override
    public void fieldClicked(FieldView f){
        controlPanel.removeAll();
        controlPanel.revalidate();
        controlPanel.repaint();
        if(f == null){
            controlPanel.add(new JLabel("nothing selected"));
        }else{
            controlPanel.add(new JLabel(f.toString()));
        }
    }

    private GridBagConstraints changeGbc(int row, int col, int rowSpan, int colSpan, double weightX, double weightY){
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.ipady = 0;
        gbc.weightx = weightX;
        gbc.weighty = weightY;
        gbc.gridwidth = colSpan;
        gbc.gridx = col;
        gbc.gridy = row;
        gbc.gridheight = rowSpan;
        return gbc;
    }

}
