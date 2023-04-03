package simplicity.View.Game;

import simplicity.Model.Game.FieldData;
import simplicity.Model.GameModel;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    private JPanel infoTab;
    private JPanel buildTab;
    private JPanel container;
    private boolean infoTabShowing = true;
    private final Font font; // temporary
    private final Font fontBold; // temporary

    public ControlPanel() {
        infoTab = new JPanel();
        buildTab = new JPanel();
        container = new JPanel();
        infoTab.setLayout(new BoxLayout(infoTab, BoxLayout.Y_AXIS));
        buildTab.setLayout(new BoxLayout(buildTab, BoxLayout.Y_AXIS));
        GridBagConstraints gbc = new GridBagConstraints();
        this.setLayout(new GridBagLayout());
        JPanel btnContainer = new JPanel();
        // btnContainer.setLayout(new FlowLayout());
        font = GameModel.CUSTOM_FONT.deriveFont(Font.PLAIN, 18);
        fontBold = GameModel.CUSTOM_FONT.deriveFont(Font.BOLD, 20);
        JButton infoBtn = new JButton("infobtn");
        JButton buildBtn = new JButton("buildbtn");
        infoBtn.setFont(font);
        buildBtn.setFont(font);
        infoBtn.addActionListener((e) -> showInfoTab());
        buildBtn.addActionListener((e) -> showBuildTab());
        initInfoTab();
        initBuildTab();
        container.add(infoTab);
        btnContainer.add(infoBtn);
        btnContainer.add(buildBtn);
        this.add(btnContainer, GamePanel.changeGbc(gbc, 0, 0, 1, 1, 1, 0));
        this.add(container, GamePanel.changeGbc(gbc, 1, 0, 1, 1, 1, 1));
    }

    public void updateInfo(FieldData f) {
        infoTab.removeAll();
        if (f == null) {
            this.initInfoTab();
        } else {
            JLabel tempLabel = new JLabel("Info: " + f.toString());
            tempLabel.setFont(font);
            infoTab.add(tempLabel);
        }
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

    public void initInfoTab() {
        JLabel tempLabel = new JLabel("nothing selected");
        tempLabel.setFont(font);
        infoTab.add(tempLabel);
    }

    public void initBuildTab() {
        //Font f = new Font("Arial", Font.BOLD, 16);
        JLabel zonesTitle = new JLabel("Select zones:");
        JLabel buildingsTitle = new JLabel("Available buildings:");
        zonesTitle.setFont(fontBold);
        buildingsTitle.setFont(fontBold);
        zonesTitle.setHorizontalAlignment(JLabel.CENTER);
        buildingsTitle.setHorizontalAlignment(JLabel.CENTER);
        buildTab.add(zonesTitle);
        buildTab.add(buildingsTitle);

    }

}
