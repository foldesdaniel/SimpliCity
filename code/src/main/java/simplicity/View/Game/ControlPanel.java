package simplicity.View.Game;

import com.formdev.flatlaf.ui.FlatScrollBarUI;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.ModeChangeListener;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Components.ControlPanelTabButton;
import simplicity.View.Style.CFont;
import simplicity.View.Style.ScrollBarUI;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class ControlPanel extends JPanel {

    private final ControlPanelTabButton infoBtn;
    private final ControlPanelTabButton buildBtn;
    private final InfoTab infoTab;
    private final BuildTab buildTab;
    private final JPanel container;
    private boolean infoTabShowing = true;
    private final Font font;

    public ControlPanel() {
        infoTab = new InfoTab();
        buildTab = new BuildTab();
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 24, 0, 24);
        this.setLayout(new GridBagLayout());
        JPanel btnContainer = new JPanel();
        GridLayout gridLayout = new GridLayout(1,2);
        gridLayout.setHgap(8);
        btnContainer.setLayout(gridLayout);
        font = CFont.get(Font.PLAIN, 22);
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
        this.add(btnContainer, GamePanel.changeGbc(gbc, 0, 0, 1, 1, 1, 0));
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        JScrollPane scrollPane = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getVerticalScrollBar().setUnitIncrement(8);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(8);
        this.add(scrollPane, GamePanel.changeGbc(gbc, 1, 0, 1, 1, 1, 1));
    }

    public void updateInfo(Placeable f) {
        infoTab.updateInfo(f);
        showInfoTab();
    }

    public void showInfoTab() {
        //if(!infoTabShowing){
        this.infoTabShowing = true;
        infoBtn.select();
        buildBtn.unselect();
        container.removeAll();
        container.revalidate();
        container.repaint();
        container.add(infoTab);
        infoBtn.requestFocus();
        //}
    }

    public void showBuildTab() {
        //if(infoTabShowing){
        this.infoTabShowing = false;
        infoBtn.unselect();
        buildBtn.select();
        container.removeAll();
        container.revalidate();
        container.repaint();
        container.add(buildTab);
        //}
    }

    public ModeChangeListener getModeListener(){
        return buildTab;
    }

}
