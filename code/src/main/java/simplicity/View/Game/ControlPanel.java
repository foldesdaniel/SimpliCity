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

    private final JButton infoBtn;
    private final JButton buildBtn;
    private final InfoTab infoTab;
    private final BuildTab buildTab;
    private final JPanel container;
    private boolean infoTabShowing = true;
    private final Font font;
    private final GameModel model;

    public ControlPanel() {
        this.model = GameModel.getInstance();
        infoTab = new InfoTab();
        buildTab = new BuildTab();
        container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        //container.setAlignmentX(Component.LEFT_ALIGNMENT);
        // container.setPreferredSize(new Dimension(10, 1000));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 24, 0, 24);
        this.setLayout(new GridBagLayout());
        //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JPanel btnContainer = new JPanel();
        //btnContainer.setLayout(new GridLayout(1,2));
        font = CFont.get(Font.PLAIN, 22);
        infoBtn = new ControlPanelTabButton("infobtn");
        buildBtn = new ControlPanelTabButton("buildbtn");
        infoBtn.setFont(font);
        buildBtn.setFont(font);
        infoBtn.addActionListener((e) -> showInfoTab());
        buildBtn.addActionListener((e) -> showBuildTab());
        //infoTab.init();
        //buildTab.init();
        container.add(infoTab);
        btnContainer.add(infoBtn);
        btnContainer.add(buildBtn);
        this.add(btnContainer, GamePanel.changeGbc(gbc, 0, 0, 1, 1, 1, 0));
        this.add(Box.createRigidArea(new Dimension(0, 20)));
        JScrollPane scrollPane = new JScrollPane(container,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        //UIManager.put("ScrollBar.thumb", new ColorUIResource(Color.RED));
        scrollPane.getVerticalScrollBar().setUI(new ScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new ScrollBarUI());
        this.add(scrollPane, GamePanel.changeGbc(gbc, 1, 0, 1, 1, 1, 1));
    }

    public void updateInfo(Placeable f) {
        infoTab.updateInfo(f);
        showInfoTab();
    }

    public void showInfoTab() {
        //if(!infoTabShowing){
        this.infoTabShowing = true;
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
