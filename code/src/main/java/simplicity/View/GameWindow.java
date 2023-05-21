package simplicity.View;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.Model.Listeners.StartStopGameListener;
import simplicity.Model.Persistence.SaveEntries;
import simplicity.View.Game.GamePanel;
import simplicity.View.Menu.MainMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class GameWindow extends JFrame implements MenuEventListener, StartStopGameListener {

    @Getter
    private static int windowWidth;
    @Getter
    private static int windowHeight;
    private final MainMenu mainMenu;
    private GamePanel gamePanel;

    public GameWindow() {
        this.setIconImage(GameModel.LOGO_SMALL_IMG);
        try {
            Taskbar.getTaskbar().setIconImage(GameModel.LOGO_SMALL_IMG);
        } catch (UnsupportedOperationException ex) {
            ex.printStackTrace();
        }
        SaveEntries.loadEntries();
        this.setTitle(GameModel.GAME_TITLE);
        this.setUndecorated(true);
        this.changedFullscreen();
        //this.changedWindowed(1280,720);
        this.setResizable(false);
        this.getContentPane().setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu = new MainMenu();
        mainMenu.addMenuEventListener(this);
        mainMenu.addStartGameListener(this);
        this.add(mainMenu, BorderLayout.CENTER);

        this.pack();
    }

    @Override
    public void changedWindowed(int width, int height) {
        windowWidth = width;
        windowHeight = height;
        this.setExtendedState(Frame.NORMAL);
        this.setSize(new Dimension(width, height));
        this.setPreferredSize(new Dimension(width, height));
        this.updateSize();
        this.setLocationRelativeTo(null);
        this.setShape(new RoundRectangle2D.Double(0, 0, this.getWidth(), this.getHeight(), 20, 20));
    }

    @Override
    public void changedFullscreen() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        windowWidth = screenSize.width;
        windowHeight = screenSize.height;
        this.setExtendedState(Frame.MAXIMIZED_BOTH);
        this.setSize(new Dimension(windowWidth, windowHeight));
        this.setPreferredSize(new Dimension(windowWidth, windowHeight));
        this.updateSize();
        this.setLocationRelativeTo(null);
        this.setShape(null);
    }

    public void updateSize() {
        // called after setting the window size, so that
        // nothing will get cut off by e.g. the macOS menu bar
        windowWidth = this.getWidth();
        windowHeight = this.getHeight();
    }

    @Override
    public void onGameStart(boolean newGame) {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
        this.updateSize(); // needs to be called here as well
        gamePanel = new GamePanel();
        gamePanel.addStopGameListener(this);
        GameModel.addStopGameListener(this);
        this.add(gamePanel);
        gamePanel.revalidate();
    }

    @Override
    public void onGameStop() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
        gamePanel = null;
        this.add(mainMenu);
        mainMenu.displayButtons();
        this.revalidate();
        this.repaint();
    }

}
