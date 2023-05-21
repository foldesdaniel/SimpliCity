package simplicity.View.Game;

import simplicity.Model.Listeners.ModeChangeListener;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Components.ControlPanelTabButton;
import simplicity.View.Style.CFont;
import simplicity.View.Style.ScrollBarUI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A left part of the game area.
 * It stores the info and build panels, and the
 * buttons that allow you to switch between them
 */
public class ControlPanel extends JPanel {

    private final ControlPanelTabButton infoBtn;
    private final ControlPanelTabButton buildBtn;
    private final InfoTab infoTab;
    private final BuildTab buildTab;
    private final JPanel container;

    public ControlPanel() {
        infoTab = new InfoTab();
        buildTab = new BuildTab();
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 24, 0, 24);
        this.setLayout(new GridBagLayout());
        JPanel btnContainer = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        gridLayout.setHgap(8);
        btnContainer.setLayout(gridLayout);
        Font font = CFont.get(Font.PLAIN, 22);
        infoBtn = new ControlPanelTabButton("Info");
        buildBtn = new ControlPanelTabButton("Build");
        infoBtn.setFont(font);
        buildBtn.setFont(font);
        infoBtn.addActionListener((e) -> showInfoTab());
        infoBtn.select();
        buildBtn.addActionListener((e) -> showBuildTab());
        container.add(infoTab);
        btnContainer.add(infoBtn);
        btnContainer.add(buildBtn);
        btnContainer.setBorder(new EmptyBorder(8, 0, 0, 0));
        this.add(btnContainer, GamePanel.changeGbc(gbc, 0, 0, 1, 1, 1, 0));
        JScrollPane scrollPane = new JScrollPane(container, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(8);
        this.add(scrollPane, GamePanel.changeGbc(gbc, 1, 0, 1, 1, 1, 1));
    }

    /**
     * Updates the info tab with the given Placeable's info
     *
     * @param f the Placeable we want to see the details of
     */
    public void updateInfo(Placeable f) {
        infoTab.updateInfo(f);
        showInfoTab();
    }

    /**
     * Hides the build tab and shows the info tab
     */
    public void showInfoTab() {
        infoBtn.select();
        buildBtn.unselect();
        container.removeAll();
        container.revalidate();
        container.repaint();
        container.add(infoTab);
        infoBtn.requestFocus();
    }

    /**
     * Hides the info tab and shows the build tab
     */
    public void showBuildTab() {
        infoBtn.unselect();
        buildBtn.select();
        container.removeAll();
        container.revalidate();
        container.repaint();
        container.add(buildTab);
    }

    public ModeChangeListener getModeListener() {
        return buildTab;
    }

}
