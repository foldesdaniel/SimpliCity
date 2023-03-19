package simplicity;

import com.formdev.flatlaf.FlatDarculaLaf;
import simplicity.View.Menu.MainMenu;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        /*InGameTime t = new InGameTime();
        t.startInGameTime(InGameSpeeds.FASTEST);
        try {
            Thread.sleep(3000);
            t.stopInGameTime();
            Thread.sleep(3000);
            t.startInGameTime(InGameSpeeds.NORMAL);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

        //*********************************
        try{
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        }catch(Exception ex){
            System.err.println("Failed to initialize FlatLaf");
        }

        JFrame gameWindow = new JFrame();

        // Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension screenSize = new Dimension(1280, 720);
        gameWindow.setPreferredSize(new Dimension(screenSize.width, screenSize.height));

        MainMenu mainMenu = new MainMenu(screenSize.width, screenSize.height);
        //gameWindow.setExtendedState(Frame.MAXIMIZED_BOTH);
        gameWindow.setLayout(null);
        gameWindow.setUndecorated(true);
        gameWindow.add(mainMenu);
        gameWindow.pack();
        gameWindow.setVisible(true);
        gameWindow.setLocationRelativeTo(null);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
