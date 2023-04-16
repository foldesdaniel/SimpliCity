package simplicity.View.Game;

import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements FieldClickListener {

    private final JMenuBar menuBar;
    private final TopLeftBar topLeftBar;
    private final TopRightBar topRightBar;
    private final ControlPanel controlPanel;
    private final PlayingFieldView playingField;
    private final BottomBar bottomBar;
    private final GridBagConstraints gbc;

    private static boolean isPlacing = false;
    private static Placeable placee;

    public GamePanel() {
        Dimension windowSize = new Dimension(GameWindow.getWindowWidth(), GameWindow.getWindowHeight());
        this.setSize(windowSize);
        this.setPreferredSize(windowSize);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        menuBar = new JMenuBar();
        topLeftBar = new TopLeftBar();
        topRightBar = new TopRightBar();
        controlPanel = new ControlPanel();
        playingField = new PlayingFieldView();
        bottomBar = new BottomBar();

        this.gbc = new GridBagConstraints();
        JPanel mainPanel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        mainPanel.setLayout(layout);
        mainPanel.add(topLeftBar, changeGbc(
                0, 0,
                1, 1,
                -1, 0
        ));
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
    }

    public static void setPlacing(Placeable p){
        isPlacing = true;
        placee = p;
    }

    public static Placeable stopPlacing(){
        Placeable p = placee;
        isPlacing = false;
        placee = null;
        return p;
    }

    public static boolean isPlacing(){
        return isPlacing;
    }

    public static Placeable getPlacee(){
        return placee;
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

    @Override
    public void fieldClicked(Placeable f) {
        controlPanel.updateInfo(f);
    }

}
