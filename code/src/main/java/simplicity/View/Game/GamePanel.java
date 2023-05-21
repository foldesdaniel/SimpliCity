package simplicity.View.Game;

import lombok.Getter;
import simplicity.Model.Listeners.FieldClickListener;
import simplicity.Model.Listeners.GameKeyListener;
import simplicity.Model.Listeners.ModeChangeListener;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.GameWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * The main panel that wraps together all the visual elements
 * and UI logic related to the game itself (not the menu)
 */
public class GamePanel extends JPanel implements FieldClickListener {

    private static final ArrayList<ModeChangeListener> modeListeners = new ArrayList<>();
    private static boolean isPlacing = false;
    private static Placeable placee;
    @Getter
    private static boolean deleteMode = false;
    private final TopLeftBar topLeftBar;
    private final TopRightBar topRightBar;
    private final ControlPanel controlPanel;
    private final PlayingFieldView playingField;
    private final BottomBar bottomBar;
    private final GridBagConstraints gbc;
    private final ArrayList<GameKeyListener> keyListeners;

    public GamePanel() {
        this.keyListeners = new ArrayList<>();
        Dimension windowSize = new Dimension(GameWindow.getWindowWidth(), GameWindow.getWindowHeight());
        this.setSize(windowSize);
        this.setPreferredSize(windowSize);
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        topLeftBar = new TopLeftBar();
        topRightBar = new TopRightBar();
        controlPanel = new ControlPanel();
        playingField = new PlayingFieldView();
        bottomBar = new BottomBar();

        KeyboardFocusManager kbmanager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kbmanager.addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE || e.getKeyCode() == KeyEvent.VK_E) {
                    if (isPlacing) {
                        stopPlacing();
                    } else if (deleteMode) {
                        stopDeleteMode();
                    }
                } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                    if (deleteMode) {
                        stopDeleteMode();
                    } else {
                        startDeleteMode();
                    }
                }
                repaint();
                for (GameKeyListener l : keyListeners) l.onKeyPressed(e);
            }
            return false;
        });

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

        controlPanel.setPreferredSize(new Dimension((int) Math.round(this.getWidth() * 0.25), controlPanel.getHeight()));

        addModeListener(this.controlPanel.getModeListener());
        this.add(mainPanel);
        this.setBackground(new Color(0, 255, 0));
        this.repaint();
    }

    public static void addModeListener(ModeChangeListener l) {
        modeListeners.add(l);
    }

    /**
     * Exits building mode (depending on the parameter) and returns the current Placeable-to-be-placed
     *
     * @param restart if we want to continue building immediately after placing an object
     * @return the Placeable to be placed
     */
    public static Placeable stopPlacing(boolean restart) {
        Placeable p = placee;
        isPlacing = restart;
        placee = restart ? BuildTile.newInstance(placee.getClass()) : null;
        for (ModeChangeListener l : modeListeners) l.onBuildModeChanged(isPlacing);
        return p;
    }

    /**
     * {@code restart} defaults to false
     *
     * @see GamePanel#stopPlacing(boolean)
     */
    public static Placeable stopPlacing() {
        return stopPlacing(false);
    }

    /**
     * @return if in building mode
     */
    public static boolean isPlacing() {
        return isPlacing;
    }

    /**
     * Enters building mode with the given Placeable.
     * Called by a {@link BuildTile} object
     *
     * @param p sets the Placeable that will be placed
     */
    public static void setPlacing(Placeable p) {
        if (deleteMode) stopDeleteMode();
        isPlacing = true;
        placee = p;
        for (ModeChangeListener l : modeListeners) l.onBuildModeChanged(isPlacing);
    }

    /**
     * @return the current Placeable-to-be-placed
     */
    public static Placeable getPlacee() {
        return placee;
    }

    /**
     * Enters delete mode
     */
    public static void startDeleteMode() {
        if (isPlacing) stopPlacing();
        deleteMode = true;
        for (ModeChangeListener l : modeListeners) l.onDeleteModeChanged(deleteMode);
    }

    /**
     * Exits delete mode
     */
    public static void stopDeleteMode() {
        deleteMode = false;
        for (ModeChangeListener l : modeListeners) l.onDeleteModeChanged(deleteMode);
    }

    /**
     * Sets up GridBagConstraints options for the next element
     *
     * @param gbc     the GridBagConstraints object
     * @param row     which row
     * @param col     which column
     * @param rowSpan rowspan
     * @param colSpan colspan
     * @param weightX width %
     * @param weightY height %
     * @return the changed GridBagConstraints object
     */
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

    public void addKeyListener(GameKeyListener l) {
        this.keyListeners.add(l);
    }

    public void addStopGameListener(StartStopGameListener stopGameListener) {
        this.topLeftBar.addStopGameListener(stopGameListener);
    }

    private GridBagConstraints changeGbc(int row, int col, int rowSpan, int colSpan, double weightX, double weightY) {
        return GamePanel.changeGbc(gbc, row, col, rowSpan, colSpan, weightX, weightY);
    }

    @Override
    public void fieldClicked(Placeable f) {
        controlPanel.updateInfo(f);
    }

}
