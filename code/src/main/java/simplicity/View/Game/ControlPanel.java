package simplicity.View.Game;

import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;
import simplicity.View.Components.ControlPanelTabButton;
import simplicity.View.Style.CFont;

import javax.swing.*;
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
        container.setPreferredSize(new Dimension(10, 1000));
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        JPanel btnContainer = new JPanel();
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
        this.add(Box.createRigidArea(new Dimension(0, 16)));
        this.add(container, GamePanel.changeGbc(gbc, 1, 0, 1, 1, 1, 1));
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

}
