package simplicity.View;

import lombok.Getter;
import simplicity.Model.GameModel;
import simplicity.Model.Listeners.MenuEventListener;
import simplicity.Model.Listeners.StartGameListener;
import simplicity.View.Game.GamePanel;
import simplicity.View.Menu.MainMenu;

import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame implements MenuEventListener, StartGameListener {

    @Getter
    private static int windowWidth;
    @Getter
    private static int windowHeight;
    private MainMenu mainMenu;
    private GamePanel gamePanel;
    private GameModel gameModel;

    public GameWindow() {
        this.setTitle(GameModel.GAME_TITLE);
        this.changedFullscreen();
        this.setUndecorated(true);
        this.setResizable(false);
        //this.changedWindowed(960,480);
        this.setLocationRelativeTo(null);
        this.getContentPane().setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenu = new MainMenu();
        mainMenu.addMenuEventListener(this);
        mainMenu.addStartGameListener(this);
        this.add(mainMenu, BorderLayout.CENTER);
        /*JMenu menuCategory1 = new JMenu("File");
        JMenuItem menuItem1 = new JMenuItem("opt1");
        JMenuItem menuItem2 = new JMenuItem("opt2");
        menuCategory1.add(menuItem1);
        menuCategory1.add(menuItem2);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menuCategory1);
        this.setJMenuBar(menuBar);*/ // it's a little broken on macos
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
    }

    public void updateSize() {
        // called after setting the window size, so that
        // nothing will get cut off by e.g. the macOS menu bar
        windowWidth = this.getWidth();
        windowHeight = this.getHeight();
    }

    @Override
    public void onGameStart() {
        this.getContentPane().removeAll();
        this.revalidate();
        this.repaint();
        this.updateSize(); // needs to be called here as well
        if (gamePanel == null) gamePanel = new GamePanel();
        gameModel = GameModel.getInstance();
        this.add(gamePanel);
    }

}
