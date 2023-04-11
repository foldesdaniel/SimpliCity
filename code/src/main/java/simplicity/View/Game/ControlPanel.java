package simplicity.View.Game;

import simplicity.Model.Game.FieldData;
import simplicity.Model.GameModel;
import simplicity.Model.Placeables.Placeable;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private InfoTab infoTab;
    private BuildTab buildTab;
    private JPanel container;
    private boolean infoTabShowing = true;
    private final Font font; // temporary
    private final GameModel model;

    public ControlPanel() {
        this.model = GameModel.getInstance();
        infoTab = new InfoTab();
        buildTab = new BuildTab();
        container = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        JPanel btnContainer = new JPanel();
        font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        JButton infoBtn = new JButton("infobtn");
        JButton buildBtn = new JButton("buildbtn");
        JButton testBtn = new JButton("print");
        infoBtn.setFont(font);
        buildBtn.setFont(font);
        testBtn.setFont(font);
        infoBtn.addActionListener((e) -> showInfoTab());
        buildBtn.addActionListener((e) -> showBuildTab());
        testBtn.addActionListener((e) -> model.printGrid());
        //infoTab.init();
        //buildTab.init();
        container.add(infoTab);
        btnContainer.add(infoBtn);
        btnContainer.add(buildBtn);
        btnContainer.add(testBtn);
        this.add(btnContainer, GamePanel.changeGbc(gbc, 0, 0, 1, 1, 1, 0));
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
