package simplicity.View;

import lombok.Getter;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.View.Menu.MainMenu;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements MenuEventListener {

    @Getter private static int windowWidth;
    @Getter private static int windowHeight;

    public GameWindow(){
        this.changedFullscreen();
        this.setUndecorated(true);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MainMenu mainMenu = new MainMenu();
        mainMenu.setMenuEventListener(this);
        this.add(mainMenu);
        this.pack();
    }

    @Override
    public void changedWindowed(int width, int height) {
        GameWindow.windowWidth = width;
        GameWindow.windowHeight = height;
        this.setExtendedState(Frame.NORMAL);
        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
    }

    @Override
    public void changedFullscreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GameWindow.windowWidth = screenSize.width;
        GameWindow.windowHeight = screenSize.height;
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setSize(new Dimension(screenSize.width, screenSize.height));
        this.setPreferredSize(new Dimension(screenSize.width, screenSize.height));
    }

}
